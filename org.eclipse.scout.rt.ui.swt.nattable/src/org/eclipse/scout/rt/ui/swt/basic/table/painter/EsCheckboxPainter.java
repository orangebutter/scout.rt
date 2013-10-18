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
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;
import org.eclipse.scout.rt.ui.swt.ISwtEnvironment;
import org.eclipse.scout.rt.ui.swt.SwtIcons;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

/**
 *
 */
public class EsCheckboxPainter extends CheckBoxPainter implements IDynamicCellSizePainter {

  public EsCheckboxPainter(ISwtEnvironment environment) {
    super(environment.getIcon(SwtIcons.CheckboxYes), environment.getIcon(SwtIcons.CheckboxNo));
  }

  @Override
  public Point computeSize(int wHint, int hHint, ILayerCell cell, IConfigRegistry configRegistry, GC gc) {
    Point size = new Point(0, 0);
    Image image = getImage(cell, configRegistry);
    if (image != null) {
      size.x += image.getBounds().width;
      size.y += image.getBounds().height;
    }
    return size;
  }

  @Override
  protected Boolean convertDataType(ILayerCell cell, IConfigRegistry configRegistry) {
    ICell scoutCell = (ICell) cell.getDataValue();
    Object value = scoutCell.getValue();
    return value instanceof Boolean && ((Boolean) value).booleanValue();
  }
}
