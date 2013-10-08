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
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;

/**
 *
 */
public class ColumnHeaderDataLayer extends DataLayer {

  private ITable m_scoutTable;

  /**
   * @param dataProvider
   */
  public ColumnHeaderDataLayer(IDataProvider dataProvider, ITable scoutTable) {
    super(dataProvider);
    m_scoutTable = scoutTable;
//    setDefaultColumnWidth(10);
  }

  @Override
  public int getColumnWidthByPosition(int columnPosition) {
    return m_scoutTable.getColumnSet().getVisibleColumn(columnPosition).getWidth();
//    return super.getColumnWidthByPosition(columnPosition);
  }

}
