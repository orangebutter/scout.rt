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

import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.ITableField;
import org.eclipse.scout.rt.spec.client.out.FormDescriptor;
import org.eclipse.scout.rt.spec.client.out.TableDescriptor;
import org.eclipse.scout.rt.spec.client.out.TableFieldDescriptor;
import org.eclipse.scout.rt.spec.client.property.DocPropertyUtility;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;
import org.eclipse.scout.rt.spec.client.property.template.ISpecTemplate;

/**
 * Creates Specification data from Form fields
 */
public class FormSpecGenerator {
  private final ISpecTemplate m_template;

  public FormSpecGenerator(ISpecTemplate template) {
    m_template = template;
  }

  public FormDescriptor getSpecData(IForm form) {
    TableDescriptor formSpec = getFormSpec(form, m_template.getFormProperties());
    TableDescriptor fieldSpec = getFieldSpec(form, m_template.getFieldProperties());
    List<TableFieldDescriptor> tableFields = getTableFields(form, m_template.getColumnProperties(), m_template.getTableTitleProperty());
    String title = m_template.getFormTitleProperty().getText(form);
    String id = m_template.getFormIdProperty().getText(form);
    return new FormDescriptor(id, title, formSpec, fieldSpec, tableFields);
  }

  private String getProperty(IForm form, IDocProperty<IForm> property) {
    return property.getText(form);
  }

  private TableDescriptor getFormSpec(IForm form, List<IDocProperty<IForm>> properties) {
    List<List<String>> rows = new ArrayList<List<String>>();
    List<String> row = new ArrayList<String>();
    for (IDocProperty<IForm> p : properties) {
      row.add(p.getText(form));
    }
    rows.add(row);
    List<String> headers = DocPropertyUtility.getHeaders(properties);
    return new TableDescriptor(rows, headers);
  }

  private List<TableFieldDescriptor> getTableFields(IForm form, List<IDocProperty<IColumn>> columnProperties, IDocProperty<ITableField<?>> tableTitleProperty) {
    TableSpecsVisitor visitor = new TableSpecsVisitor(columnProperties, tableTitleProperty);
    form.visitFields(visitor);
    return visitor.getTableFieldDescriptors();
  }

  private TableDescriptor getFieldSpec(IForm form, List<IDocProperty<IFormField>> properties) {
    FormFieldSpecsVisitor visitor = new FormFieldSpecsVisitor(properties);
    form.visitFields(visitor);
    List<List<String>> rows = visitor.getRows();
    List<String> headers = DocPropertyUtility.getHeaders(properties);
    return new TableDescriptor(rows, headers);
  }

}
