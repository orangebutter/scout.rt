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
public class TableDescriptor {
  private final String[][] m_cellTexts;
  private final String[] m_headerTexts;

  public TableDescriptor(String[][] cellTexts, String[] headerTexts) {
    m_cellTexts = cellTexts;
    m_headerTexts = headerTexts;
  }

  public String[][] getCellTexts() {
    return m_cellTexts;
  }

  public String[] getHeaderTexts() {
    return m_headerTexts;
  }

}
