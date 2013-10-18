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

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.ui.swt.ISwtEnvironment;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 *
 */
public class ScoutCellPainter extends TextPainter implements IDynamicCellSizePainter {

  private HashMap<Point /*cell position*/, P_CellSize> m_sizeCache;
  private ITable m_scoutTable;
  private ISwtEnvironment m_swtEnvironment;

  /**
   *
   */
  public ScoutCellPainter(ITable scoutTable, ISwtEnvironment swtEnvironment) {
    super(scoutTable.isMultilineText(), true, false);
    m_scoutTable = scoutTable;
    m_swtEnvironment = swtEnvironment;
    m_sizeCache = new HashMap<Point, P_CellSize>();
  }

  public ITable getScoutTable() {
    return m_scoutTable;
  }

  public ISwtEnvironment getSwtEnvironment() {
    return m_swtEnvironment;
  }

  @Override
  public Point computeSize(int wHint, int hHint, ILayerCell cell, IConfigRegistry configRegistry, GC gc) {
    Point cacheKey = new Point(cell.getColumnPosition(), cell.getRowPosition());
    P_CellSize cellSize = m_sizeCache.get(cacheKey);
    if (cellSize == null) {
      cellSize = new P_CellSize(cell.getColumnPosition(), cell.getRowPosition());
      m_sizeCache.put(cacheKey, cellSize);
    }
    if (cellSize.getWidthHint() != wHint) {
      String text = convertDataType(cell, configRegistry);
      text = getTextToDisplay(cell, gc, wHint, text);

      int fontHeight = gc.getFontMetrics().getHeight();
      String[] lines = text.split(NEW_LINE_REGEX);
      int maxLenght = 0;
      for (String line : lines) {
        maxLenght = Math.max(maxLenght, Math.min(getLengthFromCache(gc, line), wHint));
      }
      int contentHeight = (fontHeight * lines.length) + (spacing * 2);
//      System.out.println("contentheith: " + contentHeight);
      cellSize.setSize(new Point(maxLenght, contentHeight));
      cellSize.setWidthHint(wHint);
    }
    return cellSize.getSize();
  }

  @Override
  public void paintCell(ILayerCell cell, GC gc, Rectangle rectangle, IConfigRegistry configRegistry) {
    P_GcBackup gcBackup = new P_GcBackup(gc);
    try {
      IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
      ICell scoutCell = getScoutTable().getVisibleCell(cell.getRowIndex(), cell.getColumnIndex());

      setupGC(scoutCell, gc, cellStyle);
      if (paintBg) {
        gc.fillRectangle(rectangle);
      }
      super.paintCell(cell, gc, rectangle, configRegistry);

    }
    finally {
      gcBackup.restore(gc);
    }
  }

  @Override
  protected Color getBackgroundColour(ILayerCell cell, IConfigRegistry configRegistry) {
    // avoid background painter from painting background
    return null;
  }

  public void setupGC(ICell scoutCell, GC gc, IStyle cellStyle) {
    super.setupGCFromConfig(gc, cellStyle);
    if (StringUtility.hasText(scoutCell.getForegroundColor())) {
      gc.setForeground(getSwtEnvironment().getColor(scoutCell.getForegroundColor()));
    }
    if (StringUtility.hasText(scoutCell.getBackgroundColor())) {
      gc.setBackground(getSwtEnvironment().getColor(scoutCell.getBackgroundColor()));
    }
    if (scoutCell.getFont() != null) {
      gc.setFont(getSwtEnvironment().getFont(scoutCell.getFont(), gc.getFont()));
    }
  }

  @Override
  public void setupGCFromConfig(GC gc, IStyle cellStyle) {
    // ensure scout cell style are taken -> void here
  }

  @Override
  protected boolean performRowResize(int contentHeight, Rectangle rectangle) {
    return false;
  }

  private class P_GcBackup {
    private Font m_font;
    private Color m_bg;
    private Color m_fg;

    public P_GcBackup(GC gc) {
      store(gc);
    }

    public void store(GC gc) {
      m_font = gc.getFont();
      m_bg = gc.getBackground();
      m_fg = gc.getForeground();

    }

    public void restore(GC gc) {
      gc.setFont(m_font);
      gc.setBackground(m_bg);
      gc.setForeground(m_fg);
    }
  }

  private class P_CellSize {
    private final int m_columnIndex;
    private final int m_rowIndex;
    private int m_WidthHint;
    private Point m_size;

    public P_CellSize(int columnIndex, int rowIndex) {
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
      m_WidthHint = widthHint;
    }

    public int getWidthHint() {
      return m_WidthHint;
    }

    public void setSize(Point size) {
      m_size = size;
    }

    public Point getSize() {
      return m_size;
    }
  }
}
