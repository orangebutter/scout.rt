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
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.ui.swt.ISwtEnvironment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 *
 */
public class StringColumnPainter extends AbstractStringColumnPainter {

  private final ITable m_scoutTable;
  private final ISwtEnvironment m_swtEnvironment;

  private final HashMap<Point /*cell position*/, P_CellSize> m_sizeCache;
  private final boolean m_multilineText;

  /**
   *
   */
  public StringColumnPainter(ITable scoutTable, ISwtEnvironment swtEnvironment) {
    this(scoutTable, swtEnvironment, null);
  }

  public StringColumnPainter(ITable scoutTable, ISwtEnvironment swtEnvironment, ICellPainter wrappedPainter) {
    super(wrappedPainter, 0);
    m_scoutTable = scoutTable;
    m_swtEnvironment = swtEnvironment;
    m_sizeCache = new HashMap<Point, P_CellSize>();
    m_multilineText = m_scoutTable.isMultilineText();

  }

  public ITable getScoutTable() {
    return m_scoutTable;
  }

  public ISwtEnvironment getSwtEnvironment() {
    return m_swtEnvironment;
  }

  public boolean isMultilineText() {
    return m_multilineText;
  }

  @Override
  public Point computeSize(int wHint, int hHint, ILayerCell cell, IConfigRegistry configRegistry, GC gc) {
    Point cacheKey = new Point(cell.getColumnIndex(), cell.getRowIndex());
    P_CellSize cellSize = m_sizeCache.get(cacheKey);
    if (cellSize == null) {
      cellSize = new P_CellSize(cell.getColumnIndex(), cell.getRowIndex());
      m_sizeCache.put(cacheKey, cellSize);
    }
    if (cellSize.getWidthHint() != wHint) {
      P_GcBackup gcBackup = new P_GcBackup(gc);
      try {
        IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
        ICell scoutCell = getScoutTable().getVisibleCell(cell.getRowIndex(), cell.getColumnIndex());
        setupGC(scoutCell, gc, cellStyle);

        String text = convertDataType(cell, configRegistry);

        // Draw Text
        text = getTextToDisplay(cell, gc, wHint, text, isMultilineText(), isWrapText(cellStyle));
        int fontHeight = gc.getFontMetrics().getHeight();
        String[] lines = getLines(text);
        int maxLenght = 0;
        for (String line : lines) {
          maxLenght = Math.max(maxLenght, Math.min(getLengthFromCache(gc, line) + 2 * getSpacing(), wHint));
        }
        int contentHeight = (fontHeight * lines.length) + (getSpacing() * 2);

        cellSize.setSize(new Point(maxLenght, contentHeight));
        cellSize.setWidthHint(wHint);
      }
      finally {
        gcBackup.restore(gc);
      }

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
      super.paintCell(cell, gc, rectangle, configRegistry);
      // background
      gc.fillRectangle(rectangle);

      // foreground
      Rectangle originalClipping = gc.getClipping();
      gc.setClipping(rectangle.intersection(originalClipping));

      int fontHeight = gc.getFontMetrics().getHeight();
      String text = convertDataType(cell, configRegistry);

      // Draw Text
      text = getTextToDisplay(cell, gc, rectangle.width - 2 * getSpacing(), text, isMultilineText(), isWrapText(cellStyle));

      int numberOfNewLines = getLines(text).length;

      //if the content height is bigger than the available row height
      //we're extending the row height (only if word wrapping is enabled)
      int spacing = getSpacing();
      int contentHeight = (fontHeight * numberOfNewLines) + (spacing * 2);

      if (numberOfNewLines == 1) {
        int contentWidth = Math.min(getLengthFromCache(gc, text), rectangle.width);
        gc.drawText(
            text,
            rectangle.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, rectangle, contentWidth + 2 * spacing) + spacing,
            rectangle.y + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, rectangle, contentHeight) + spacing,
            SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER
            );

      }
      else {
        //draw every line by itself because of the alignment, otherwise the whole text
        //is always aligned right
        int yStartPos = rectangle.y
            + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, rectangle, contentHeight);
        String[] lines = text.split("\n"); //$NON-NLS-1$
        for (String line : lines) {
          int lineContentWidth = Math.min(getLengthFromCache(gc, line), rectangle.width);

          gc.drawText(
              line,
              rectangle.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, rectangle, lineContentWidth) + spacing,
              yStartPos + spacing,
              SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER
              );

          //after every line calculate the y start pos new
          yStartPos += fontHeight;
        }
      }

      gc.setClipping(originalClipping);

    }
    finally {
      gcBackup.restore(gc);
    }
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

//  @Override
//  protected int calculatePadding(ILayerCell cell, int availableLength) {
//    return cell.getBounds().width - availableLength;
//  }

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
