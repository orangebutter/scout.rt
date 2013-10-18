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
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.PaddingDecorator;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 *
 */
public class EsPaddingDecorator extends PaddingDecorator implements IDynamicCellSizePainter {

  /**
   * @param interiorPainter
   * @param topPadding
   * @param rightPadding
   * @param bottomPadding
   * @param leftPadding
   */
  public EsPaddingDecorator(ICellPainter interiorPainter, int topPadding, int rightPadding, int bottomPadding, int leftPadding) {
    super(interiorPainter, topPadding, rightPadding, bottomPadding, leftPadding);
  }

  /**
   * @param interiorPainter
   * @param padding
   */
  public EsPaddingDecorator(ICellPainter interiorPainter, int padding) {
    super(interiorPainter, padding);
  }

  /**
   * @param interiorPainter
   */
  public EsPaddingDecorator(ICellPainter interiorPainter) {
    super(interiorPainter);
  }

  @Override
  public Point computeSize(int wHint, int hHint, ILayerCell cell, IConfigRegistry configRegistry, GC gc) {
    Point size;
    Rectangle padding = getInteriorBounds(new Rectangle(0, 0, 0, 0));
    ICellPainter wrappedPainter = getWrappedPainter();
    if (wrappedPainter != null) {
      if (wHint > 0) {
        wHint = wHint + padding.width;
      }
      if (hHint > 0) {
        hHint = hHint + padding.height;
      }
      Point wrappedBounds = ((IDynamicCellSizePainter) wrappedPainter).computeSize(wHint, hHint, cell, configRegistry, gc);
      size = new Point(wrappedBounds.x - padding.width, wrappedBounds.y - padding.height);
    }
    else {

      size = new Point(Math.max(-padding.width, wHint), Math.max(-padding.height, hHint));
    }
    return size;
  }

}
