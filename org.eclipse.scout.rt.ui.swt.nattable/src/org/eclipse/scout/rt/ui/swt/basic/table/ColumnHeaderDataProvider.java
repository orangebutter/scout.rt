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
package org.eclipse.scout.rt.ui.swt.basic.table;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;

/**
 *
 */
public class ColumnHeaderDataProvider implements IDataProvider {
  private ITable m_scoutTable;

  public ColumnHeaderDataProvider(ITable scoutTable) {
    m_scoutTable = scoutTable;

  }

  @Override
  public int getColumnCount() {
    int visibleColumnCount = m_scoutTable.getColumnSet().getVisibleColumnCount();
    System.out.println("visible column count " + visibleColumnCount);
    return visibleColumnCount;
  }

  @Override
  public Object getDataValue(int columnIndex, int rowIndex) {
    if (columnIndex >= 0) {
      System.out.println("getHeader TEXT for " + columnIndex + " / " + rowIndex);
      return m_scoutTable.getVisibleHeaderCell(columnIndex).getText();
    }
    return "blubber";
  }

  @Override
  public int getRowCount() {
    return 1;
  }

  @Override
  public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
    throw new UnsupportedOperationException();
  }
}
