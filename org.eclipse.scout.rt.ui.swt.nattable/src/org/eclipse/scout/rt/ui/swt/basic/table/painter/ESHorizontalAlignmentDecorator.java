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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.CellPainterWrapper;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 *
 */
public class ESHorizontalAlignmentDecorator extends CellPainterWrapper implements IDynamicCellSizePainter {

  private ITable m_scoutTable;
  private Map<Point /*cell Index*/, P_CellPadding> m_cache;

  /**
   * @param painter
   */
  public ESHorizontalAlignmentDecorator(ICellPainter painter, ITable scoutTable) {
    super(painter);
    m_scoutTable = scoutTable;
    m_cache = new HashMap<Point, ESHorizontalAlignmentDecorator.P_CellPadding>();
  }

  public ITable getScoutTable() {
    return m_scoutTable;
  }

  @Override
  public int getPreferredHeight(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
    return super.getPreferredHeight(cell, gc, configRegistry);
  }

  @Override
  public int getPreferredWidth(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
    P_CellPadding cellPadding = m_cache.get(new Point(cell.getColumnIndex(), cell.getRowIndex()));
    if (cellPadding != null) {
      return super.getPreferredWidth(cell, gc, configRegistry) + cellPadding.getPaddingLeft() + cellPadding.getPaddingRight();
    }
    else {
      return super.getPreferredWidth(cell, gc, configRegistry);
    }
  }

  @Override
  public Point computeSize(int wHint, int hHint, ILayerCell cell, IConfigRegistry configRegistry, GC gc) {
    ICell scoutCell = getScoutTable().getCell(cell.getRowIndex(), cell.getColumnIndex());
    Point cellSize = ((IDynamicCellSizePainter) getWrappedPainter()).computeSize(wHint, hHint, cell, configRegistry, gc);

    Point cacheKey = new Point(cell.getColumnIndex(), cell.getRowIndex());
    P_CellPadding cellPadding = m_cache.get(cacheKey);
    if (cellPadding == null) {
      cellPadding = new P_CellPadding(cell.getColumnIndex(), cell.getRowIndex());
      m_cache.put(cacheKey, cellPadding);
    }
    if (cellSize.x < wHint) {
      int hAglin = scoutCell.getHorizontalAlignment();
      if (hAglin < 0) {
        // left
        cellPadding.setPaddingLeft(0);
        cellPadding.setPaddingRight(wHint - cellSize.x);
      }
      else if (hAglin == 0) {
        cellPadding.setPaddingLeft((wHint - cellSize.x) / 2);
        cellPadding.setPaddingRight((wHint - cellSize.x) / 2);
      }
      else {
        cellPadding.setPaddingLeft(wHint - cellSize.x);
        cellPadding.setPaddingRight(0);
      }
    }
    else {
      cellPadding.setPaddingLeft(0);
      cellPadding.setPaddingRight(0);
    }
    Point size = new Point(Math.max(cellSize.x, wHint), Math.max(hHint, cellSize.y));
//    System.out.println("-----");
//    System.out.println("compute size of: " + cell.getDataValue());
//    System.out.println("computed size = " + cellSize.toString());
//    System.out.println("w/hHint: " + wHint + "/" + hHint);
//    System.out.println(cellPadding.toString());
//    System.out.println("returned size = " + size.toString());
//    System.out.println("______________");
    cellPadding.setWidthHint(wHint);
    return size;
  }

  @Override
  public void paintCell(ILayerCell cell, GC gc, Rectangle adjustedCellBounds, IConfigRegistry configRegistry) {
    int paddingLeft = 0;
    int paddingRight = 0;
    P_CellPadding cellPadding = m_cache.get(new Point(cell.getColumnIndex(), cell.getRowIndex()));
    if (cellPadding != null) {
      paddingLeft = cellPadding.getPaddingLeft();
      paddingRight = cellPadding.getPaddingRight();
    }
//    System.out.println("*** " + getClass().getName() + " paintCell: " + adjustedCellBounds.toString() + " padding r/l " + paddingLeft + "/" + paddingRight);
    Rectangle interiorBounds = new Rectangle(adjustedCellBounds.x + paddingLeft, adjustedCellBounds.y, adjustedCellBounds.width - (paddingRight + paddingLeft), adjustedCellBounds.height);
    super.paintCell(cell, gc, interiorBounds, configRegistry);
  }

  private class P_CellPadding {
    private final int m_columnIndex;
    private final int m_rowIndex;
    private int m_widthHint;
    private int m_paddingLeft;
    private int m_paddingRight;

    public P_CellPadding(int columnIndex, int rowIndex) {
      m_columnIndex = columnIndex;
      m_rowIndex = rowIndex;
    }

    public int getColumnIndex() {
      return m_columnIndex;
    }

    public int getRowIndex() {
      return m_rowIndex;
    }

    public void setWidthHint(int widthHint) {
      m_widthHint = widthHint;
    }

    public int getWidthHint() {
      return m_widthHint;
    }

    public void setPaddingRight(int paddingRight) {
      m_paddingRight = paddingRight;
    }

    public int getPaddingRight() {
      return m_paddingRight;
    }

    public void setPaddingLeft(int paddingLeft) {
      m_paddingLeft = paddingLeft;
    }

    public int getPaddingLeft() {
      return m_paddingLeft;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("paddingLeft/Right: ").append(getPaddingLeft()).append("/").append(getPaddingRight());
      return builder.toString();
    }
  }
}
