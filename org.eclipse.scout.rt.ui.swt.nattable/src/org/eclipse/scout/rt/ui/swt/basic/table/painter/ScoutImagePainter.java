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
package org.eclipse.scout.rt.ui.swt.basic.table.painter;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.ui.swt.ISwtEnvironment;
import org.eclipse.swt.graphics.Image;

/**
 *
 */
public class ScoutImagePainter extends ImagePainter {

  private ITable m_scoutTable;
  private ISwtEnvironment m_environment;

  public ScoutImagePainter(ITable scoutTable, ISwtEnvironment environment) {
    m_scoutTable = scoutTable;
    m_environment = environment;

  }

  public ITable getScoutTable() {
    return m_scoutTable;
  }

  public ISwtEnvironment getEnvironment() {
    return m_environment;
  }

  @Override
  protected Image getImage(ILayerCell cell, IConfigRegistry configRegistry) {
    ICell scoutCell = getScoutTable().getVisibleCell(cell.getRowIndex(), cell.getColumnIndex());
    String iconId = scoutCell.getIconId();
    if (StringUtility.isNullOrEmpty(iconId) && cell.getColumnPosition() == 0) {
      iconId = getScoutTable().getRow(cell.getRowIndex()).getIconId();
    }
    if (!StringUtility.isNullOrEmpty(iconId)) {
      return getEnvironment().getIcon(iconId);
    }
    return null;
  }

}
