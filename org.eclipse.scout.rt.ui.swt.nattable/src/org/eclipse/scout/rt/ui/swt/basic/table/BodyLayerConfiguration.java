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
package org.eclipse.scout.rt.ui.swt.basic.table;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.IConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;

/**
 *
 */
public class BodyLayerConfiguration extends AbstractRegistryConfiguration implements IConfigLabelAccumulator {

  private static final String LABEL_ALIGNMENT_HORIZONTAL_LEFT = "alignmentHorizontalLeft";
  private static final String LABEL_ALIGNMENT_HORIZONTAL_CENTER = "alignmentHorizontalCenter";
  private static final String LABEL_ALIGNMENT_HORIZONTAL_RIGHT = "alignmentHorizontalRight";

  private ITable m_scoutTable;

  public BodyLayerConfiguration(ITable scoutTable) {
    m_scoutTable = scoutTable;
  }

  public ITable getScoutTable() {
    return m_scoutTable;
  }

  @Override
  public void accumulateConfigLabels(LabelStack configLabels, int columnPosition, int rowPosition) {
    ICell cell = getScoutTable().getVisibleCell(rowPosition, columnPosition);
    int horizontalAlignment = cell.getHorizontalAlignment();
    if (horizontalAlignment == 0) {
      configLabels.addLabel(LABEL_ALIGNMENT_HORIZONTAL_CENTER);
    }
    else if (horizontalAlignment > 0) {
      configLabels.addLabel(LABEL_ALIGNMENT_HORIZONTAL_RIGHT);
    }
    else {
      configLabels.addLabel(LABEL_ALIGNMENT_HORIZONTAL_LEFT);
    }
  }

  @Override
  public void configureRegistry(IConfigRegistry configRegistry) {
    Style cellStyle = new Style();
    cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.LEFT);
    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, LABEL_ALIGNMENT_HORIZONTAL_LEFT);

    cellStyle = new Style();
    cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, LABEL_ALIGNMENT_HORIZONTAL_CENTER);

    cellStyle = new Style();
    cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.RIGHT);
    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, LABEL_ALIGNMENT_HORIZONTAL_RIGHT);
  }

}
