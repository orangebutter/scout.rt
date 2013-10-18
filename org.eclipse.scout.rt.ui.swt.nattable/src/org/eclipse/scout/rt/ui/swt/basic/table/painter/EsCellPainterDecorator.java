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
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.CellPainterDecorator;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 *
 */
public class EsCellPainterDecorator extends CellPainterDecorator implements IDynamicCellSizePainter {

  private final ICellPainter m_decoratorCellPainter;
  private final ICellPainter m_baseCellPainter;
  private int m_spacing;

  public EsCellPainterDecorator(ICellPainter baseCellPainter, CellEdgeEnum cellEdge, ICellPainter decoratorCellPainter) {
    this(baseCellPainter, cellEdge, 2, decoratorCellPainter);
  }

  public EsCellPainterDecorator(ICellPainter baseCellPainter, CellEdgeEnum cellEdge, ICellPainter decoratorCellPainter, boolean paintDecorationDependent) {
    this(baseCellPainter, cellEdge, 2, decoratorCellPainter, paintDecorationDependent);
  }

  public EsCellPainterDecorator(ICellPainter baseCellPainter, CellEdgeEnum cellEdge, int spacing, ICellPainter decoratorCellPainter) {
    this(baseCellPainter, cellEdge, spacing, decoratorCellPainter, true);
  }

  public EsCellPainterDecorator(ICellPainter baseCellPainter, CellEdgeEnum cellEdge, int spacing, ICellPainter decoratorCellPainter, boolean paintDecorationDependent) {
    super(baseCellPainter, cellEdge, spacing, decoratorCellPainter, paintDecorationDependent);
    m_baseCellPainter = baseCellPainter;
    m_spacing = spacing;
    m_decoratorCellPainter = decoratorCellPainter;
  }

  @Override
  public Point computeSize(int wHint, int hHint, ILayerCell cell, IConfigRegistry configRegistry, GC gc) {
    int decoWidth = m_decoratorCellPainter.getPreferredWidth(cell, gc, configRegistry);
    if (wHint > 0) {
      wHint = wHint - decoWidth - m_spacing;
    }

    Point size = ((IDynamicCellSizePainter) m_baseCellPainter).computeSize(wHint, hHint, cell, configRegistry, gc);
    size.x += (decoWidth + m_spacing);
    return size;
  }
}
