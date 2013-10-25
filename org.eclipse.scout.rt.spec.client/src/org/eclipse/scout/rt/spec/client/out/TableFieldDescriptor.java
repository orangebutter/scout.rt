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

/**
 *
 */
public class TableFieldDescriptor {
  private final String m_id;
  private final String m_title;
  private final TableDescriptor m_columnProperties;

  public TableFieldDescriptor(String id, String title, TableDescriptor columnProperties) {
    m_id = id;
    m_title = title;
    m_columnProperties = columnProperties;
  }

  public String getId() {
    return m_id;
  }

  public String getTitle() {
    return m_title;
  }

  public TableDescriptor getColumnProperties() {
    return m_columnProperties;
  }

}
