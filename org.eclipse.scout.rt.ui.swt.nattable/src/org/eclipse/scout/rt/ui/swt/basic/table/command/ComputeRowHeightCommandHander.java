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
package org.eclipse.scout.rt.ui.swt.basic.table.command;

import org.eclipse.nebula.widgets.nattable.command.ILayerCommandHandler;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.resize.command.RowResizeCommand;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.IDynamicCellSizePainter;
import org.eclipse.swt.graphics.Point;

/**
 *
 */
public class ComputeRowHeightCommandHander implements ILayerCommandHandler<ComputeRowHeightCommand> {

  public ComputeRowHeightCommandHander() {
  }

  @Override
  public Class<ComputeRowHeightCommand> getCommandClass() {
    return ComputeRowHeightCommand.class;
  }

  @Override
  public boolean doCommand(ILayer targetLayer, ComputeRowHeightCommand command) {
    System.out.println("do command from:'" + command.getViewPortMinIndex() + "' to:'" + command.getViewPortMaxIndex() + "'");
//    int rowCount = targetLayer.getRowCount();
    int columnCount = targetLayer.getColumnCount();
    for (int rowIndex = command.getViewPortMinIndex(); rowIndex < command.getViewPortMaxIndex(); rowIndex++) {
      int currentRowHeight = targetLayer.getRowHeightByPosition(rowIndex);
      int height = 20;
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        ILayerCell cell = targetLayer.getCellByPosition(columnIndex, rowIndex);
        ICellPainter cellPainter = targetLayer.getCellPainter(columnIndex, rowIndex, cell, command.getConfigRegistry());
//        if (cellPainter instanceof IDynamicCellSizePainter) {
        Point cellSize = ((IDynamicCellSizePainter) cellPainter).computeSize(cell.getBounds().width - 1, cell.getBounds().height - 1, cell, command.getConfigRegistry(), command.getGc());

        height = Math.max(height, cellSize.y);
//        }

      }

      if (currentRowHeight != height) {
        // do resize
        targetLayer.doCommand(new RowResizeCommand(targetLayer, rowIndex, height));
      }
    }
    return true;
  }

}
