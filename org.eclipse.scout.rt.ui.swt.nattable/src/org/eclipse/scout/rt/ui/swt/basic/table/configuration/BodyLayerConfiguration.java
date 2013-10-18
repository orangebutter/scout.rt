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
package org.eclipse.scout.rt.ui.swt.basic.table.configuration;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.IConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IStringColumn;
import org.eclipse.scout.rt.ui.swt.ISwtEnvironment;
import org.eclipse.scout.rt.ui.swt.basic.table.converter.StringColumnDisplayConverter;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.ESHorizontalAlignmentDecorator;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.EsCheckboxPainter;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.EsLineBorderDecorator;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.EsPaddingDecorator;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.StringColumnPainter;

/**
 *
 */
public class BodyLayerConfiguration extends AbstractRegistryConfiguration implements IConfigLabelAccumulator {

  private static final String LABEL_ALIGNMENT_HORIZONTAL_LEFT = "alignmentHorizontalLeft";
  private static final String LABEL_ALIGNMENT_HORIZONTAL_CENTER = "alignmentHorizontalCenter";
  private static final String LABEL_ALIGNMENT_HORIZONTAL_RIGHT = "alignmentHorizontalRight";
  private static final String LABEL_TEXT_WRAP = "textWrap";
  private static final String LABEL_PAINTER_BOOLEAN_COLUMN = "painterBooleanColumn";
  private static final String LABEL_PAINTER_STRING_COLUMN = "painterStringColumn";
  private static final String LABEL_PAINTER_UNKNOWN_COLUMN = "painterUnknownColumn";

  private ITable m_scoutTable;
  private ICellPainter m_stringColumnPainter;
  private ICellPainter m_booleanColumnPainter;

  public BodyLayerConfiguration(ITable scoutTable, ISwtEnvironment environment) {
    m_scoutTable = scoutTable;
    // init painters
    m_stringColumnPainter = createDefaultPainterStack(new StringColumnPainter(scoutTable, environment));
    m_booleanColumnPainter = createDefaultPainterStack(new EsCheckboxPainter(environment));
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
    IColumn scoutColumn = getScoutTable().getColumnSet().getVisibleColumn(columnPosition);
    if (scoutColumn instanceof IBooleanColumn) {
      configLabels.addLabel(LABEL_PAINTER_BOOLEAN_COLUMN);
    }
    else if (scoutColumn instanceof IStringColumn) {
      configLabels.addLabel(LABEL_PAINTER_STRING_COLUMN);
      if (((IStringColumn) scoutColumn).isTextWrap()) {
        configLabels.addLabel(LABEL_TEXT_WRAP);
      }
    }
    else {
      configLabels.addLabel(LABEL_PAINTER_UNKNOWN_COLUMN);
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

    cellStyle = new Style();
    cellStyle.setAttributeValue(EsCellStyleAttributes.TEXT_WRAP, Boolean.TRUE);
    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, LABEL_TEXT_WRAP);

    cellStyle = new Style();
    cellStyle.setAttributeValue(EsCellStyleAttributes.FOREGROUND_COLOR, GUIHelper.COLOR_RED);
    cellStyle.setAttributeValue(EsCellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_YELLOW);
    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, LABEL_PAINTER_UNKNOWN_COLUMN);

    configureRegsitryPainter(configRegistry);
  }

  protected void configureRegsitryPainter(IConfigRegistry configRegistry) {
    configureRegistryBooleanColumn(configRegistry);
    configureRegistryStringColumn(configRegistry);
    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, m_stringColumnPainter, DisplayMode.NORMAL, LABEL_PAINTER_UNKNOWN_COLUMN);
  }

  protected void configureRegistryBooleanColumn(IConfigRegistry configRegistry) {
    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, m_booleanColumnPainter, DisplayMode.NORMAL, LABEL_PAINTER_BOOLEAN_COLUMN);
  }

  protected void configureRegistryStringColumn(IConfigRegistry configRegistry) {
    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, m_stringColumnPainter, DisplayMode.NORMAL, LABEL_PAINTER_STRING_COLUMN);
    configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new StringColumnDisplayConverter());
  }

  private ICellPainter createDefaultPainterStack(ICellPainter dataPainter) {
    ICellPainter painter = new ESHorizontalAlignmentDecorator(dataPainter, getScoutTable());
    painter = new EsPaddingDecorator(painter, 2);
    painter = new EsLineBorderDecorator(painter);
    return painter;
  }
}
