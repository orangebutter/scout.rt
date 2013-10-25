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
package org.eclipse.scout.rt.spec.client.out;

import java.util.List;

/**
 *
 */
public class FormDescriptor {

  private final String m_id;
  private final String m_title;
  private final TableDescriptor m_formProperties;
  private final TableDescriptor m_fieldProperties;
  private final List<TableFieldDescriptor> m_tableFields;

  public FormDescriptor(String id, String title, TableDescriptor formProperties, TableDescriptor fieldProperties, List<TableFieldDescriptor> tableFields) {
    m_fieldProperties = fieldProperties;
    m_formProperties = formProperties;
    m_title = title;
    m_id = id;
    m_tableFields = tableFields;
  }

  public TableDescriptor getFormProperties() {
    return m_formProperties;
  }

  public TableDescriptor getFieldProperties() {
    return m_fieldProperties;
  }

  public String getTitle() {
    return m_title;
  }

  public String getId() {
    return m_id;
  }

  public List<TableFieldDescriptor> getTableFields() {
    return m_tableFields;
  }
}
