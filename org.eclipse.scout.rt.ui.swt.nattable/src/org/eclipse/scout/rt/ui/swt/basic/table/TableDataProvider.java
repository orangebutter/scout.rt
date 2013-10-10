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

import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;

/**
 *
 */
public class TableDataProvider implements IRowDataProvider<ITableRow> {
  private ITable m_scoutTable;

  public TableDataProvider(ITable scoutTable) {
    m_scoutTable = scoutTable;
  }

  public ITable getScoutTable() {
    return m_scoutTable;
  }

  @Override
  public Object getDataValue(int columnIndex, int rowIndex) {
    return getScoutTable().getVisibleCell(rowIndex, columnIndex).getText();
  }

  @Override
  public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
  }

  @Override
  public int getColumnCount() {
    return getScoutTable().getVisibleColumnCount();
  }

  @Override
  public int getRowCount() {
    ITableRow[] filteredRows = getScoutTable().getFilteredRows();
    return getScoutTable().getFilteredRowCount();
  }

  @Override
  public ITableRow getRowObject(int rowIndex) {
    return getScoutTable().getFilteredRow(rowIndex);
  }

  @Override
  public int indexOfRowObject(ITableRow rowObject) {
    return getScoutTable().getFilteredRowIndex(rowObject);
  }

}
