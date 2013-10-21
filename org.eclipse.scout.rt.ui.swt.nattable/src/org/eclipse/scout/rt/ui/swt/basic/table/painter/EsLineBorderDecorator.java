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
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.LineBorderDecorator;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 *
 */
public class EsLineBorderDecorator extends LineBorderDecorator implements IDynamicCellSizePainter {

  private BorderStyle m_defaultBorderStyle;

  /**
   * @param interiorPainter
   * @param defaultBorderStyle
   */
  public EsLineBorderDecorator(ICellPainter interiorPainter, BorderStyle defaultBorderStyle) {
    super(interiorPainter, defaultBorderStyle);
    m_defaultBorderStyle = defaultBorderStyle;
  }

  /**
   * @param interiorPainter
   */
  public EsLineBorderDecorator(ICellPainter interiorPainter) {
    super(interiorPainter);
  }

  @Override
  public Point computeSize(int wHint, int hHint, ILayerCell cell, IConfigRegistry configRegistry, GC gc) {
    IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
    BorderStyle borderStyle = cellStyle.getAttributeValue(CellStyleAttributes.BORDER_STYLE);
    if (borderStyle == null) {
      borderStyle = m_defaultBorderStyle;
    }
    int borderThickness = borderStyle != null ? borderStyle.getThickness() : 0;
    Point size;
    ICellPainter wrappedPainter = getWrappedPainter();
    if (wrappedPainter != null) {
      if (wHint > 0) {
        wHint = wHint - (borderThickness * 2);
      }
      if (hHint > 0) {
        hHint = hHint - (borderThickness * 2);
      }
      Point wrappedBounds = ((IDynamicCellSizePainter) wrappedPainter).computeSize(wHint, hHint, cell, configRegistry, gc);
      size = new Point(wrappedBounds.x + (borderThickness * 2), wrappedBounds.y + (borderThickness * 2));
    }
    else {

      size = new Point(Math.max((borderThickness * 2), wHint), Math.max((borderThickness * 2), hHint));
    }
    return size;
  }

  @Override
  public void paintCell(ILayerCell cell, GC gc, Rectangle rectangle, IConfigRegistry configRegistry) {
//    System.out.println("*** " + getClass().getName() + " paintCell: " + rectangle.toString());
    super.paintCell(cell, gc, rectangle, configRegistry);
  }

}
