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

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatLayerPainter;
import org.eclipse.scout.rt.ui.swt.basic.table.command.ComputeRowHeightCommand;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 *
 */
public class EsNatLayerPainter extends NatLayerPainter {

  private NatTable m_natTable;

  /**
   * @param natTable
   */
  public EsNatLayerPainter(NatTable natTable) {
    super(natTable);
    m_natTable = natTable;
  }

  @Override
  public void paintLayer(ILayer natLayer, GC gc, int xOffset, int yOffset, Rectangle rectangle, IConfigRegistry configRegistry) {
    ComputeRowHeightCommand computeRowHeightCommand = new ComputeRowHeightCommand(gc, configRegistry);
    natLayer.doCommand(computeRowHeightCommand);

    // do resize
    super.paintLayer(natLayer, gc, xOffset, yOffset, rectangle, configRegistry);
  }

}
