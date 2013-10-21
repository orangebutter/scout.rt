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
import org.eclipse.nebula.widgets.nattable.painter.cell.CellPainterWrapper;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.ui.swt.ISwtEnvironment;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 *
 */
public class ScoutImagePainter extends CellPainterWrapper implements IDynamicCellSizePainter {

  private final ITable m_scoutTable;
  private final ISwtEnvironment m_environment;

  public ScoutImagePainter(ICellPainter wrappedPainter, ITable scoutTable, ISwtEnvironment environment) {
    super(wrappedPainter);
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
  public Point computeSize(int wHint, int hHint, ILayerCell cell, IConfigRegistry configRegistry, GC gc) {
    Image image = getImage(cell, configRegistry);
    Point imageSize = new Point(0, 0);
    if (image != null) {
      Rectangle imageBounds = image.getBounds();
      imageSize.x += imageBounds.width + 2;
    }
    ICellPainter wrappedPainter = getWrappedPainter();
    Point size = new Point(wHint, hHint);
    if (wrappedPainter != null) {
      size = ((IDynamicCellSizePainter) wrappedPainter).computeSize(wHint - imageSize.x, hHint, cell, configRegistry, gc);
      size.x += imageSize.x;

    }
    return size;

  }

  protected Rectangle computeInteriorBounds(Rectangle bounds, Image image) {
    Rectangle interiorBounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
    if (image != null) {
      Rectangle imgBounds = image.getBounds();
      interiorBounds.x += imgBounds.width + 2;
      interiorBounds.width -= imgBounds.width + 2;
    }
    return interiorBounds;
  }

  @Override
  public void paintCell(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
    Image image = getImage(cell, configRegistry);
    if (image != null) {
      gc.drawImage(
          image,
          bounds.x,
          bounds.y);
    }
    super.paintCell(cell, gc, computeInteriorBounds(bounds, image), configRegistry);

  }

  protected Image getImage(ILayerCell cell, IConfigRegistry configRegistry) {
    ICell scoutCell = getScoutTable().getVisibleCell(cell.getRowIndex(), cell.getColumnIndex());//(ICell) cell.getDataValue();
    String iconId = scoutCell.getIconId();
    if (StringUtility.isNullOrEmpty(iconId) && cell.getColumnPosition() == 0) {
      iconId = getScoutTable().getRow(cell.getRowIndex()).getIconId();
    }
    if (!StringUtility.isNullOrEmpty(iconId)) {
      System.out.println("+getImage " + cell.getRowIndex() + "/" + cell.getColumnIndex());
      return getEnvironment().getIcon(iconId);
    }
    System.out.println("-getImage " + cell.getRowIndex() + "/" + cell.getColumnIndex());
    return null;
  }

  @Override
  public ICellPainter getCellPainterAt(int x, int y, ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
    Image image = getImage(cell, configRegistry);
    if (image != null) {
      Rectangle imageBounds = image.getBounds();
      IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
      int x0 = bounds.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, bounds, imageBounds.width);
      int y0 = bounds.y + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, bounds, imageBounds.height);
      if (x >= x0 &&
          x < x0 + imageBounds.width &&
          y >= y0 &&
          y < y0 + imageBounds.height) {
        return super.getCellPainterAt(x, y, cell, gc, bounds, configRegistry);
      }
    }
    return null;
  }

}
