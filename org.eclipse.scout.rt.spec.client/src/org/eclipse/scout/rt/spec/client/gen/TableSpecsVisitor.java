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
package org.eclipse.scout.rt.spec.client.gen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IFormFieldVisitor;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.ITableField;
import org.eclipse.scout.rt.spec.client.out.TableDescriptor;
import org.eclipse.scout.rt.spec.client.out.TableFieldDescriptor;
import org.eclipse.scout.rt.spec.client.property.DocPropertyUtility;
import org.eclipse.scout.rt.spec.client.property.IDocFilter;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

/**
 *
 */
public class TableSpecsVisitor implements IFormFieldVisitor {
  private final List<IDocProperty<IColumn>> m_columnProperties;
  private final List<IDocProperty<IMenu>> m_menuProperties;
  private final IDocProperty<ITableField<?>> m_titleProperty;
  private final List<IDocFilter<IColumn>> m_column_filters;
  private final List<TableFieldDescriptor> m_tableFields = new ArrayList<TableFieldDescriptor>();

  public TableSpecsVisitor(List<IDocProperty<IColumn>> columnProperties, List<IDocFilter<IColumn>> column_filters, List<IDocProperty<IMenu>> menuProperties, IDocProperty<ITableField<?>> titleProperty) {
    m_columnProperties = columnProperties;
    m_titleProperty = titleProperty;
    m_menuProperties = menuProperties;
    m_column_filters = column_filters;
  }

  @Override
  public boolean visitField(IFormField field, int level, int fieldIndex) {
    if (field instanceof ITableField<?>) {
      TableFieldDescriptor fieldDesc = createFieldDescriptor((ITableField) field);
      m_tableFields.add(fieldDesc);
    }
    return true;
  }

  private TableFieldDescriptor createFieldDescriptor(ITableField field) {
    IColumn<?>[] columns = field.getTable().getColumns();
    IMenu[] menus = field.getTable().getMenus();
    TableDescriptor tableDesc = DocPropertyUtility.createTableDesc(columns, m_columnProperties, m_column_filters);
    TableDescriptor menuDesc = DocPropertyUtility.createTableDesc(menus, m_menuProperties, null);
    String title = m_titleProperty.getText(field);
    return new TableFieldDescriptor(field.getClass().getName(), title, tableDesc, menuDesc);
  }

  public List<TableFieldDescriptor> getTableFieldDescriptors() {
    return m_tableFields;
  }

}
