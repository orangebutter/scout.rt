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
package org.eclipse.scout.rt.ui.swt.extension.internal;

public class UiRendererExtension implements IUiRendererExtension {

  private String m_name;
  private boolean m_active;
  private int m_scope = SCOPE_DEFAULT;
  private String /* fully qualified name of a Class<? extends IFormField> */m_modelClassName;
  private String /*
                  * fully qualified name of a Class<? extends
                  * ISwtScoutFormField>
                  */m_directRenderer;
  private String /*
                  * fully qualified name of a Class<? extends
                  * IFormFieldFactory>
                  */m_rendererFactory;
  private String m_contibuterBundleId;
  private Double m_priority;

  public UiRendererExtension(String name) {
    m_name = name;
  }

  @Override
  public String getContibuterBundleId() {
    return m_contibuterBundleId;
  }

  public void setContibuterBundleId(String contibuterBundleId) {
    m_contibuterBundleId = contibuterBundleId;
  }

  @Override
  public String getName() {
    return m_name;
  }

  public void setName(String name) {
    m_name = name;
  }

  @Override
  public boolean isActive() {
    return m_active;
  }

  public void setActive(boolean active) {
    m_active = active;
  }

  @Override
  public int getScope() {
    return m_scope;
  }

  public void setScope(int scope) {
    m_scope = scope;
  }

  @Override
  public String getModelClassName() {
    return m_modelClassName;
  }

  public void setModelClassName(String modelClassName) {
    m_modelClassName = modelClassName;
  }

  @Override
  public String getUiClassName() {
    return m_directRenderer;
  }

  public void setUiClassName(String uiClassName) {
    m_directRenderer = uiClassName;
  }

  @Override
  public String getFactoryClassName() {
    return m_rendererFactory;
  }

  public void setFactoryClassName(String rendererFactory) {
    m_rendererFactory = rendererFactory;
  }

  public void setPriority(Double priority) {
    m_priority = priority;
  }

  @Override
  public Double getPriority() {
    return m_priority;
  }
}
