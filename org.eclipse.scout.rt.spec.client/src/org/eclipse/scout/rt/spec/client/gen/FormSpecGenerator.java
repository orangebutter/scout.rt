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

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.spec.client.out.FormDescriptor;
import org.eclipse.scout.rt.spec.client.out.TableDescriptor;
import org.eclipse.scout.rt.spec.client.out.TableFieldDescriptor;
import org.eclipse.scout.rt.spec.client.property.DocPropertyUtility;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;
import org.eclipse.scout.rt.spec.client.property.template.IDocConfig;
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
    TableDescriptor fieldSpec = getFieldSpec(form, m_template.getFieldConfig());
    List<TableFieldDescriptor> tableFields = getTableFields(form);
    String title = m_template.getFormTitleProperty().getText(form);
    String id = m_template.getFormIdProperty().getText(form);
    return new FormDescriptor(id, title, formSpec, fieldSpec, tableFields);
  }

  private String getProperty(IForm form, IDocProperty<IForm> property) {
    return property.getText(form);
  }

  private TableDescriptor getFormSpec(IForm form, List<IDocProperty<IForm>> properties) {
    List<String[]> rows = new ArrayList<String[]>();
    rows.add(DocPropertyUtility.getTexts(form, properties));
    String[][] rowArray = CollectionUtility.toArray(rows, String[].class);
    String[] headers = DocPropertyUtility.getHeaders(properties);
    return new TableDescriptor(rowArray, headers);
  }

  private List<TableFieldDescriptor> getTableFields(IForm form) {
    TableSpecsVisitor visitor = new TableSpecsVisitor(m_template.getColumnConfig(), m_template.getMenuConfig(), m_template.getTableTitleProperty());
    form.visitFields(visitor);
    return visitor.getTableFieldDescriptors();
  }

  private TableDescriptor getFieldSpec(IForm form, IDocConfig<IFormField> fieldConfig) {
    FormFieldSpecsVisitor visitor = new FormFieldSpecsVisitor(fieldConfig);
    form.visitFields(visitor);
    String[][] rows = visitor.getRows();
    String[] headers = DocPropertyUtility.getHeaders(fieldConfig.getProperties());
    return new TableDescriptor(rows, headers);
  }

}