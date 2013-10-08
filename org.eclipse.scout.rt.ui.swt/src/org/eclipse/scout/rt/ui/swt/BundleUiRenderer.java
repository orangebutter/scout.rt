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
package org.eclipse.scout.rt.ui.swt;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.core.runtime.Platform;
import org.eclipse.scout.commons.CompositeObject;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.ui.swt.basic.IUiRenderable;
import org.eclipse.scout.rt.ui.swt.extension.IFormFieldExtension;
import org.eclipse.scout.rt.ui.swt.extension.IFormFieldFactory;
import org.eclipse.scout.rt.ui.swt.extension.IUiRendererFactory;
import org.eclipse.scout.rt.ui.swt.extension.UiRenderableExtensionPoint;
import org.eclipse.scout.rt.ui.swt.extension.internal.IUiRendererExtension;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;

public class BundleUiRenderer implements IUiRendererFactory {
  private static IScoutLogger LOG = ScoutLogManager.getLogger(BundleUiRenderer.class);

  private LinkedHashMap<Class<?>, IUiRendererFactory> m_rendererFactories;

  /**
   * @param bundle
   *          the ui.swt bundle of the requesting application (e.g.
   *          com.bsiag.crm.ui.swt)
   */
  @SuppressWarnings("unchecked")
  public BundleUiRenderer(Bundle requesterBundle) {
    TreeMap<CompositeObject, P_RendererExtension> sortedMap = new TreeMap<CompositeObject, P_RendererExtension>();
    for (IUiRendererExtension extension : UiRenderableExtensionPoint.getUiRendererExtensions()) {
      if (extension.isActive()) {
        Bundle loaderBundle;
        if (extension.getScope() == IFormFieldExtension.SCOPE_LOCAL) {
          loaderBundle = requesterBundle;
        }
        else {
          loaderBundle = Platform.getBundle(extension.getContibuterBundleId());
        }
        if (loaderBundle != null) {
          Class<?> modelClazz;
          Class<?> uiClazz = null;
          Class<? extends IUiRendererFactory> factoryClazz = null;
          try {
            modelClazz = loaderBundle.loadClass(extension.getModelClassName());
            if (!StringUtility.isNullOrEmpty(extension.getUiClassName())) {
              uiClazz = loaderBundle.loadClass(extension.getUiClassName());
              if (!IUiRenderable.class.isAssignableFrom(uiClazz)) {
                LOG.warn("extension '" + extension.getName() + "' contributed by '" + extension.getContibuterBundleId() + "' has an ui class not instanceof " + IUiRenderable.class.getName() + ".");
                uiClazz = null;
              }
            }
            else if (!StringUtility.isNullOrEmpty(extension.getFactoryClassName())) {
              factoryClazz = (Class<? extends IUiRendererFactory>) loaderBundle.loadClass(extension.getFactoryClassName());
              if (!IFormFieldFactory.class.isAssignableFrom(factoryClazz)) {
                LOG.warn("extension '" + extension.getName() + "' contributed by '" + extension.getContibuterBundleId() + "' has a facotry class not instanceof " + IFormFieldFactory.class.getName() + ".");
                factoryClazz = null;
              }
            }
            IUiRendererFactory factory = null;
            if (uiClazz != null) {
              factory = new P_DirectLinkRendererFactory((Class<? extends IUiRenderable>) uiClazz);
            }
            else if (factoryClazz != null) {
              try {
                factory = factoryClazz.newInstance();
              }
              catch (Exception e) {
                LOG.warn("could not create a factory instance of '" + factoryClazz.getName() + "' ", e);
              }
            }
            else {
              LOG.debug("extension '" + extension.getName() + "' contributed by '" + extension.getContibuterBundleId() + "' has neither an UiClass nor a factory defined! Skipping extension.");
              continue;
            }
            int distance = -distanceToIFormField(modelClazz, 0);
            CompositeObject key = new CompositeObject(distance, extension.getPriority(), modelClazz.getName());
            if (sortedMap.containsKey(key)) {
              P_RendererExtension existingExt = sortedMap.get(key);
              // check scope
              if (existingExt.getFormFieldExtension().getScope() == extension.getScope()) {
                LOG.warn("The bundles '" + extension.getContibuterBundleId() + "' and '" + existingExt.getFormFieldExtension().getContibuterBundleId() + "' are both providing " + "an form field extension to '" + extension.getModelClassName() + "' with the same scope.");
              }
              else if (existingExt.getFormFieldExtension().getScope() < extension.getScope()) {
                // replace
                sortedMap.put(key, new P_RendererExtension(modelClazz, factory, extension));
              }
            }
            else {
              sortedMap.put(key, new P_RendererExtension(modelClazz, factory, extension));
            }
          }
          catch (ClassNotFoundException e) {
            LOG.debug("local extension '" + extension.getName() + "' contributed by '" + extension.getContibuterBundleId() + "' is not visible from bundle: '" + loaderBundle.getSymbolicName() + "'.");
          }
        }

      }
    }

    m_rendererFactories = new LinkedHashMap<Class<?>, IUiRendererFactory>();
    for (P_RendererExtension ext : sortedMap.values()) {
      m_rendererFactories.put(ext.getModelClazz(), ext.getFactory());
    }

  }

  private static int distanceToIFormField(Class<?> visitee, int dist) {
    if (visitee == IFormField.class) {
      return dist;
    }
    else {
      int locDist = 100000;
      Class<?> superclass = visitee.getSuperclass();
      if (superclass != null) {
        locDist = distanceToIFormField(superclass, (dist + 1));
      }
      Class[] interfaces = visitee.getInterfaces();
      if (interfaces != null) {
        for (Class<?> i : interfaces) {
          locDist = Math.min(locDist, distanceToIFormField(i, (dist + 1)));
        }
      }
      dist = locDist;
      return dist;
    }
  }

  @Override
  public IUiRenderable<?> create(Composite parent, Object model, ISwtEnvironment environment) {
    return createUiRendererByExtension(parent, model, environment);
  }

  private IUiRenderable<?> createUiRendererByExtension(Composite parent, Object model, ISwtEnvironment environment) {
    IUiRendererFactory factory = null;
    for (Entry<Class<?>, IUiRendererFactory> link : m_rendererFactories.entrySet()) {
      if (link.getKey().isAssignableFrom(model.getClass())) {
        // create instance
        factory = link.getValue();
        try {
          return factory.create(parent, model, environment);
        }
        catch (Throwable e) {
          LOG.error("could not create form field for: [model = '" + model.getClass().getName() + "'; ui = '" + factory.toString() + "'].", e);
        }
      }
    }
    if (factory != null) {
      try {
        return factory.create(parent, model, environment);
      }
      catch (Throwable t) {
        t.printStackTrace();
        return null;
      }
    }
    return null;
  }

  private class P_DirectLinkRendererFactory implements IUiRendererFactory {
    private final Class<? extends IUiRenderable> m_uiClazz;

    public P_DirectLinkRendererFactory(Class<? extends IUiRenderable> uiClazz) {
      m_uiClazz = uiClazz;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IUiRenderable<?> create(Composite parent, Object model, ISwtEnvironment environment) {
      try {
        IUiRenderable newInstance = m_uiClazz.newInstance();
        newInstance.create(parent, model, environment);
        return newInstance;
      }
      catch (Exception e) {
        LOG.warn(null, e);
        return null;
      }
    }

    @Override
    public String toString() {
      return "DirectLinkFactory to: " + m_uiClazz.getName();
    }
  }// end class P_DirectLinkFormFieldFactory

  private class P_RendererExtension {
    private final Class<?> m_modelClazz;
    private final IUiRendererFactory m_factory;
    private final IUiRendererExtension m_formFieldExtension;

    public P_RendererExtension(Class<?> modelClazz, IUiRendererFactory factory, IUiRendererExtension rendererExtension) {
      m_modelClazz = modelClazz;
      m_factory = factory;
      m_formFieldExtension = rendererExtension;
    }

    public Class<?> getModelClazz() {
      return m_modelClazz;
    }

    public IUiRendererFactory getFactory() {
      return m_factory;
    }

    public IUiRendererExtension getFormFieldExtension() {
      return m_formFieldExtension;
    }
  } // end class P_FormFieldExtension

}
