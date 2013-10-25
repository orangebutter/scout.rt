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

import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.IStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.ITableField;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.spec.client.property.DocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;
import org.eclipse.scout.rt.spec.client.property.SimpleTypeProperty;
import org.eclipse.scout.rt.spec.client.property.form.TitleFormProperty;
import org.eclipse.scout.rt.spec.client.property.form.field.BooleanFormFieldProperty;
import org.eclipse.scout.rt.spec.client.property.form.field.IdFormFieldProperty;
import org.eclipse.scout.rt.spec.client.property.form.field.TableFieldTypeAndLabelProperty;
import org.eclipse.scout.rt.spec.client.property.form.field.TextFormFieldProperty;

/**
   *
   */
public class LargeSpecTemplate implements ISpecTemplate {

  @Override
  public List<IDocProperty<IFormField>> getFieldProperties() {
    List<IDocProperty<IFormField>> propertyTemplate = new ArrayList<IDocProperty<IFormField>>();
    propertyTemplate.add(new IdFormFieldProperty(TEXTS.get("org.eclipse.scout.rt.spec.id")));
    propertyTemplate.add(new TextFormFieldProperty(IFormField.PROP_LABEL, TEXTS.get("org.eclipse.scout.rt.spec.label")));
    propertyTemplate.add(new SimpleTypeProperty<IFormField>(TEXTS.get("org.eclipse.scout.rt.spec.type")));
    propertyTemplate.add(new TextFormFieldProperty(IStringField.PROP_MAX_LENGTH, TEXTS.get("org.eclipse.scout.rt.spec.length")));
    propertyTemplate.add(new BooleanFormFieldProperty(IFormField.PROP_MANDATORY, TEXTS.get("org.eclipse.scout.rt.spec.mandatory")));
    propertyTemplate.add(new BooleanFormFieldProperty(IFormField.PROP_ENABLED, TEXTS.get("org.eclipse.scout.rt.spec.enabled")));
    propertyTemplate.add(new TextFormFieldProperty(IFormField.PROP_TOOLTIP_TEXT, TEXTS.get("org.eclipse.scout.rt.spec.tooltip")));
    propertyTemplate.add(new DocProperty<IFormField>());
    return propertyTemplate;
  }

  @Override
  public List<IDocProperty<IForm>> getFormProperties() {
    List<IDocProperty<IForm>> propertyTemplate = new ArrayList<IDocProperty<IForm>>();
    propertyTemplate.add(new TitleFormProperty());
    propertyTemplate.add(new DocProperty<IForm>());
    return propertyTemplate;
  }

  @Override
  public IDocProperty<IForm> getFormTitleProperty() {
    return new DocProperty<IForm>();
  }

  @Override
  public IDocProperty<IForm> getFormIdProperty() {
    return new SimpleTypeProperty<IForm>();
  }

  @Override
  public List<IDocProperty<IColumn>> getColumnProperties() {
    List<IDocProperty<IColumn>> propertyTemplate = new ArrayList<IDocProperty<IColumn>>();
    propertyTemplate.add(new DocProperty<IColumn>());
    propertyTemplate.add(new SimpleTypeProperty<IColumn>());
    return propertyTemplate;
  }

  @Override
  public IDocProperty<ITableField<?>> getTableTitleProperty() {
    return new TableFieldTypeAndLabelProperty();
  }
}
