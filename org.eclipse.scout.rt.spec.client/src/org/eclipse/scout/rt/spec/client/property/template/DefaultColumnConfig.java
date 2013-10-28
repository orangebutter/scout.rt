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
package org.eclipse.scout.rt.spec.client.property.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.annotations.Doc;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.spec.client.property.DocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocFilter;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;
import org.eclipse.scout.rt.spec.client.property.IgnoreDocFilter;
import org.eclipse.scout.rt.spec.client.property.SimpleTypeProperty;
import org.eclipse.scout.rt.spec.client.property.column.ColumnWidthProperty;
import org.eclipse.scout.rt.spec.client.property.column.DisplayableColumnFilter;
import org.eclipse.scout.rt.spec.client.property.column.HeaderTooltipProperty;
import org.eclipse.scout.rt.spec.client.property.column.LabelColumnProperty;
import org.eclipse.scout.rt.spec.client.property.column.SortColumnProperty;

/**
 * The default configuration for {@link IColumn}
 */
public class DefaultColumnConfig implements IDocConfig<IColumn> {

  /**
   * Default properties for {@link IColumn} with
   * <p>
   * Sort,Label,Type,Width,Tooltip,Description
   * </p>
   */
  @Override
  public List<IDocProperty<IColumn>> getProperties() {
    List<IDocProperty<IColumn>> propertyTemplate = new ArrayList<IDocProperty<IColumn>>();
    propertyTemplate.add(new SortColumnProperty());
    propertyTemplate.add(new LabelColumnProperty());
    propertyTemplate.add(new SimpleTypeProperty<IColumn>());
    propertyTemplate.add(new ColumnWidthProperty());
    propertyTemplate.add(new HeaderTooltipProperty());
    propertyTemplate.add(new DocProperty<IColumn>());
    return propertyTemplate;
  }

  /**
   * Default filters for {@link IColumn}: Ignores Types annotated with {@link Doc#ignore()}==false and columns that are
   * not displayable
   */
  @Override
  public List<IDocFilter<IColumn>> getFilters() {
    List<IDocFilter<IColumn>> columnFilters = new ArrayList<IDocFilter<IColumn>>();
    columnFilters.add(new IgnoreDocFilter<IColumn>());
    columnFilters.add(new DisplayableColumnFilter());
    return columnFilters;
  }

}
