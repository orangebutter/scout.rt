/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.commons.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.WeakEventListener;
import org.eclipse.scout.commons.eventlistprofiler.EventListenerProfiler;
import org.eclipse.scout.commons.eventlistprofiler.IEventListenerSnapshot;
import org.eclipse.scout.commons.eventlistprofiler.IEventListenerSource;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;

public class BasicPropertySupport implements IEventListenerSource {
  private static final IScoutLogger LOG = ScoutLogManager.getLogger(BasicPropertySupport.class);

  public static final int DEFAULT_INT_VALUE = 0;
  public static final int DEFAULT_DOUBLE_VALUE = 0;
  public static final Integer DEFAULT_INT = Integer.valueOf(DEFAULT_INT_VALUE);
  public static final Double DEFAULT_DOUBLE = Double.valueOf(DEFAULT_DOUBLE_VALUE);
  public static final long DEFAULT_LONG_VALUE = DEFAULT_INT_VALUE;
  public static final Long DEFAULT_LONG = Long.valueOf(DEFAULT_LONG_VALUE);
  private static final Boolean DEFAULT_BOOL = Boolean.FALSE;
  private final Map<String, Object> m_props = new HashMap<String, Object>();
  private Object m_source;
  // observer
  private final Object m_listenerLock = new Object();
  private List<Object> m_listeners;
  private Map<String, List<Object>> m_childListeners;
  private int m_propertiesChanging;
  private List<PropertyChangeEvent> m_propertyEventBuffer;

  public BasicPropertySupport(Object sourceBean) {
    m_source = sourceBean;
    if (EventListenerProfiler.getInstance().isEnabled()) {
      EventListenerProfiler.getInstance().registerSourceAsWeakReference(this);
    }
  }

  @Override
  public void dumpListenerList(IEventListenerSnapshot snapshot) {
    synchronized (m_listenerLock) {
      if (m_listeners != null) {
        for (Object o : m_listeners) {
          if (o instanceof WeakReference) {
            snapshot.add(PropertyChangeListener.class, null, ((WeakReference) o).get());
          }
          else {
            snapshot.add(PropertyChangeListener.class, null, o);
          }
        }
      }
      if (m_childListeners != null) {
        for (Map.Entry<String, List<Object>> e : m_childListeners.entrySet()) {
          String context = e.getKey();
          for (Object o : e.getValue()) {
            if (o instanceof WeakReference) {
              snapshot.add(PropertyChangeListener.class, context, ((WeakReference) o).get());
            }
            else {
              snapshot.add(PropertyChangeListener.class, context, o);
            }
          }
        }
      }
    }
  }

  public boolean isPropertiesChanging() {
    return m_propertiesChanging > 0;
  }

  public void setPropertiesChanging(boolean b) {
    // use a stack counter because setTableChanging might be called in nested
    // loops
    if (b) {
      m_propertiesChanging++;
    }
    else {
      if (m_propertiesChanging > 0) {
        m_propertiesChanging--;
        if (m_propertiesChanging == 0) {
          processChangeBuffer();
        }
      }
    }
  }

  public void clearProperties() {
    m_props.clear();
  }

  public Map<String, Object> getPropertiesMap() {
    // loop and catch exception instead of using lock (better performance)
    for (int i = 0; i < 10; i++) {
      try {
        return new HashMap<String, Object>(m_props);
      }
      catch (ConcurrentModificationException cme) {
        if (LOG.isDebugEnabled()) {
          LOG.debug(null, cme);
        }
      }
    }
    return new HashMap<String, Object>(m_props);
  }

  public void putPropertiesMap(Map<String, Object> map) {
    m_props.putAll(map);
  }

  /**
   * DESIGN: should return false if the asked property is set to null - see
   * setProperty() as well. (tha, 16.2.6)
   */
  public boolean hasProperty(String name) {
    return m_props.containsKey(name);
  }

  public void setPropertyInt(String name, int i) {
    setProperty(name, Integer.valueOf(i), DEFAULT_INT);
  }

  public int getPropertyInt(String name) {
    Number n = (Number) getProperty(name);
    return n != null ? n.intValue() : 0;
  }

  public void setPropertyDouble(String name, double d) {
    setProperty(name, new Double(d), DEFAULT_DOUBLE);
  }

  public double getPropertyDouble(String name) {
    Number n = (Number) getProperty(name);
    return n != null ? n.doubleValue() : 0;
  }

  public void setPropertyLong(String name, long i) {
    setProperty(name, Long.valueOf(i), DEFAULT_LONG);
  }

  public long getPropertyLong(String name) {
    Number n = (Number) getProperty(name);
    return n != null ? n.longValue() : DEFAULT_LONG.longValue();
  }

  public boolean setPropertyBool(String name, boolean b) {
    return setProperty(name, Boolean.valueOf(b), DEFAULT_BOOL);
  }

  public boolean getPropertyBool(String name) {
    Boolean b = (Boolean) getProperty(name);
    return b != null ? b.booleanValue() : DEFAULT_BOOL.booleanValue();
  }

  public void setPropertyString(String name, String s) {
    setProperty(name, s);
  }

  public void setPropertyStringAlwaysFire(String name, String s) {
    setPropertyAlwaysFire(name, s);
  }

  public String getPropertyString(String name) {
    String s = (String) getProperty(name);
    return s;
  }

  public Object getProperty(String name) {
    return m_props.get(name);
  }

  public boolean setProperty(String name, Object newValue) {
    return setProperty(name, newValue, null);
  }

  public boolean/* changed */setPropertyNoFire(String name, Object newValue) {
    Object oldValue = m_props.get(name);
    m_props.put(name, newValue);
    if (CompareUtility.equals(oldValue, newValue)) {
      // no change
      return false;
    }
    else {
      return true;
    }
  }

  /**
   * DESIGN: should remove property if set to null - see hasProperty() as well
   * (tha, 16.2.6)
   */
  public boolean setProperty(String name, Object newValue, Object defaultOldValueWhenNull) {
    Object oldValue = m_props.get(name);
    if (oldValue == null) {
      oldValue = defaultOldValueWhenNull;
    }
    m_props.put(name, newValue);
    if (CompareUtility.equals(oldValue, newValue)) {
      // no change
      return false;
    }
    else {
      firePropertyChangeImpl(name, oldValue, newValue);
      return true;
    }
  }

  public void setPropertyAlwaysFire(String name, Object newValue) {
    Object oldValue = m_props.get(name);
    m_props.put(name, newValue);
    firePropertyChangeImpl(name, oldValue, newValue);
  }

  /**
   * Implementation
   */

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    if (listener instanceof PropertyChangeListenerProxy) {
      PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy) listener;
      // Call two argument add method.
      addPropertyChangeListener(proxy.getPropertyName(), (PropertyChangeListener) proxy.getListener());
    }
    else {
      synchronized (m_listenerLock) {
        if (m_listeners == null) {
          m_listeners = new ArrayList<Object>();
        }
        if (listener instanceof WeakEventListener) {
          m_listeners.add(new WeakReference<PropertyChangeListener>(listener));
        }
        else {
          m_listeners.add(listener);
        }
      }
    }
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    if (listener instanceof PropertyChangeListenerProxy) {
      PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy) listener;
      // Call two argument remove method.
      removePropertyChangeListener(proxy.getPropertyName(), (PropertyChangeListener) proxy.getListener());
    }
    else {
      synchronized (m_listenerLock) {
        removeFromListNoLock(m_listeners, listener);
        if (m_childListeners != null) {
          for (List childList : m_childListeners.values()) {
            removeFromListNoLock(childList, listener);
          }
        }
      }
    }
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    synchronized (m_listenerLock) {
      if (m_childListeners == null) {
        m_childListeners = new HashMap<String, List<Object>>();
      }
      List<Object> children = m_childListeners.get(propertyName);
      if (children == null) {
        children = new ArrayList<Object>();
        m_childListeners.put(propertyName, children);
      }
      children.add(listener);
    }
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    synchronized (m_listenerLock) {
      if (m_childListeners != null) {
        List childList = m_childListeners.get(propertyName);
        if (childList != null) {
          removeFromListNoLock(childList, listener);
        }
      }
    }
  }

  private void removeFromListNoLock(List listeners, PropertyChangeListener listener) {
    if (listeners == null) {
      return;
    }
    if (listener instanceof WeakEventListener) {
      for (int i = 0, n = listeners.size(); i < n; i++) {
        Object o = listeners.get(i);
        if (o instanceof WeakReference) {
          if (((WeakReference) o).get() == listener) {
            listeners.remove(i);
            break;
          }
        }
      }
    }
    else {
      listeners.remove(listener);
    }
    if (listeners.size() == 0 && listeners instanceof ArrayList) {
      ((ArrayList) listeners).trimToSize();
    }
  }

  public void firePropertyChange(PropertyChangeEvent e) {
    firePropertyChangeImpl(e);
  }

  public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    if (oldValue == newValue) {
      return;
    }
    firePropertyChangeImpl(propertyName, Integer.valueOf(oldValue), Integer.valueOf(newValue));
  }

  public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    if (oldValue == newValue) {
      return;
    }
    firePropertyChangeImpl(propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
  }

  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    if (CompareUtility.equals(oldValue, newValue)) {
      return;
    }
    firePropertyChangeImpl(propertyName, oldValue, newValue);
  }

  private void firePropertyChangeImpl(String propertyName, Object oldValue, Object newValue) {
    List l = m_listeners;
    Map m = m_childListeners;
    if ((l != null && l.size() > 0) || (m != null && m.size() > 0)) {
      PropertyChangeEvent e = new PropertyChangeEvent(m_source, propertyName, oldValue, newValue);
      firePropertyChangeImpl(e);
    }
  }

  private void firePropertyChangeImpl(PropertyChangeEvent e) {
    if (e == null) {
      return;
    }
    //
    if (isPropertiesChanging()) {
      // buffer the event for later batch firing
      synchronized (m_listenerLock) {
        if (m_propertyEventBuffer == null) {
          m_propertyEventBuffer = new ArrayList<PropertyChangeEvent>();
        }
        m_propertyEventBuffer.add(e);
      }
    }
    else {
      ArrayList<PropertyChangeListener> targets = new ArrayList<PropertyChangeListener>(4);
      synchronized (m_listenerLock) {
        if (m_listeners != null) {
          for (Object o : m_listeners) {
            if (o instanceof WeakReference) {
              o = ((WeakReference) o).get();
            }
            if (o != null) {
              targets.add((PropertyChangeListener) o);
            }
          }
        }
        String propertyName = e.getPropertyName();
        if (propertyName != null && m_childListeners != null) {
          List childListeners = m_childListeners.get(propertyName);
          if (childListeners != null) {
            for (Object o : childListeners) {
              if (o instanceof WeakReference) {
                o = ((WeakReference) o).get();
              }
              if (o != null) {
                targets.add((PropertyChangeListener) o);
              }
            }
          }
        }
      }// end synchronized
      if (targets.size() > 0) {
        for (PropertyChangeListener listener : targets) {
          listener.propertyChange(e);
        }
      }
    }
  }

  private void processChangeBuffer() {
    /*
     * fire events property changes are finished now fire all buffered events in
     * one batch
     */
    PropertyChangeEvent[] a = null;
    synchronized (m_listenerLock) {
      if (m_propertyEventBuffer != null) {
        a = m_propertyEventBuffer.toArray(new PropertyChangeEvent[m_propertyEventBuffer.size()]);
      }
      m_propertyEventBuffer = null;
    }
    if (a != null && a.length > 0) {
      // coalesce by names
      LinkedList<PropertyChangeEvent> coalesceList = new LinkedList<PropertyChangeEvent>();
      HashSet<String> names = new HashSet<String>();
      // reverse traversal
      for (int i = a.length - 1; i >= 0; i--) {
        if (!names.contains(a[i].getPropertyName())) {
          coalesceList.add(0, a[i]);
          names.add(a[i].getPropertyName());
        }
      }
      for (PropertyChangeEvent e : coalesceList) {
        firePropertyChangeImpl(e);
      }
    }
  }

  public boolean hasListeners(String propertyName) {
    synchronized (m_listenerLock) {
      List l0 = m_listeners;
      List l1 = null;
      if (propertyName != null) {
        l1 = m_childListeners.get(propertyName);
      }
      int count = (l0 != null ? l0.size() : 0) + (l1 != null ? l1.size() : 0);
      return count > 0;
    }
  }
}
