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

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDisplayConverter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.ui.swt.ISwtEnvironment;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.ESHorizontalAlignmentDecorator;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.EsLineBorderDecorator;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.EsPaddingDecorator;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.ScoutCellPainter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 *
 */
public class TableStyleConfiguration extends AbstractRegistryConfiguration {

  public Color bgColor = GUIHelper.COLOR_WHITE;
  public Color fgColor = GUIHelper.COLOR_BLACK;
  public Color gradientBgColor = GUIHelper.COLOR_WHITE;
  public Color gradientFgColor = GUIHelper.getColor(136, 212, 215);
  public Font font = GUIHelper.DEFAULT_FONT;
  public HorizontalAlignmentEnum hAlign = HorizontalAlignmentEnum.CENTER;
  public VerticalAlignmentEnum vAlign = VerticalAlignmentEnum.TOP;
  public BorderStyle borderStyle = null;

  public ICellPainter cellPainter;
  private ITable m_scoutTable;
  private ISwtEnvironment m_environment;

  public TableStyleConfiguration(NatTable table, ITable scoutTable, ISwtEnvironment environment) {
    m_scoutTable = scoutTable;
    m_environment = environment;

    ICellPainter painter = new ScoutCellPainter(getScoutTable(), getEnvironment());
    painter = new ESHorizontalAlignmentDecorator(painter, getScoutTable());
    painter = new EsPaddingDecorator(painter, 2);
    painter = new EsLineBorderDecorator(painter);

    cellPainter = painter;

  }

  public ITable getScoutTable() {
    return m_scoutTable;
  }

  public ISwtEnvironment getEnvironment() {
    return m_environment;
  }

  @Override
  public void configureRegistry(IConfigRegistry configRegistry) {
    configureBody(configRegistry);

  }

  /**
   * @param configRegistry
   */
  protected void configureBody(IConfigRegistry configRegistry) {
    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, cellPainter);

    Style cellStyle = new Style();
    cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, bgColor);
    cellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, fgColor);
    cellStyle.setAttributeValue(CellStyleAttributes.GRADIENT_BACKGROUND_COLOR, gradientBgColor);
    cellStyle.setAttributeValue(CellStyleAttributes.GRADIENT_FOREGROUND_COLOR, gradientFgColor);
    cellStyle.setAttributeValue(CellStyleAttributes.FONT, font);
    cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, hAlign);
    cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, vAlign);
    cellStyle.setAttributeValue(CellStyleAttributes.BORDER_STYLE, borderStyle);

    configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle);

    configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultDisplayConverter());
  }

}
