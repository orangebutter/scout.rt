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
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.BeveledBorderDecorator;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 *
 */
public class ESBeveledBorderDecorator extends BeveledBorderDecorator implements IDynamicCellSizePainter {

  /**
   * @param interiorPainter
   * @param uplift
   */
  public ESBeveledBorderDecorator(ICellPainter interiorPainter, boolean uplift) {
    super(interiorPainter, uplift);
  }

  /**
   * @param interiorPainter
   */
  public ESBeveledBorderDecorator(ICellPainter interiorPainter) {
    super(interiorPainter);
  }

  @Override
  public Point computeSize(int wHint, int hHint, ILayerCell cell, IConfigRegistry configRegistry, GC gc) {
    Point size = new Point(Math.max(0, wHint), Math.max(0, hHint));
    ICellPainter wrappedPainter = getWrappedPainter();
    if (wrappedPainter != null) {
      if (wHint > 0) {
        wHint -= 4;
      }
      if (hHint > 0) {
        hHint -= 4;
      }
      size = ((IDynamicCellSizePainter) wrappedPainter).computeSize(wHint, hHint, cell, configRegistry, gc);
      size.x += 4;
      size.y += 4;
    }
    return size;
  }

}
