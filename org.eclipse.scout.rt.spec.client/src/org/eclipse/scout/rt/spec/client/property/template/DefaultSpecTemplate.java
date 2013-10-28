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

import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.ITableField;
import org.eclipse.scout.rt.spec.client.property.DocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;
import org.eclipse.scout.rt.spec.client.property.TypeProperty;
import org.eclipse.scout.rt.spec.client.property.form.TitleFormProperty;
import org.eclipse.scout.rt.spec.client.property.form.field.TableFieldTypeAndLabelProperty;

/**
 * A default template that should be possible to use for most projects.
 */
public class DefaultSpecTemplate implements ISpecTemplate {

  @Override
  public List<IDocProperty<IForm>> getFormProperties() {
    List<IDocProperty<IForm>> propertyTemplate = new ArrayList<IDocProperty<IForm>>();
    propertyTemplate.add(new TitleFormProperty());
    propertyTemplate.add(new DocProperty<IForm>());
    propertyTemplate.add(new TypeProperty<IForm>());
    return propertyTemplate;
  }

  @Override
  public IDocConfig<IFormField> getFieldConfig() {
    return new DefaultFormFieldConfig();
  }

  @Override
  public IDocConfig<IColumn> getColumnConfig() {
    return new DefaultColumnConfig();
  }

  @Override
  public IDocConfig<IMenu> getMenuConfig() {
    return new DefaultMenuConfig();
  }

  @Override
  public IDocProperty<IForm> getFormTitleProperty() {
    return new TitleFormProperty();
  }

  @Override
  public IDocProperty<ITableField<?>> getTableTitleProperty() {
    return new TableFieldTypeAndLabelProperty();
  }

  @Override
  public IDocProperty<IForm> getFormIdProperty() {
    return new TypeProperty<IForm>();
  }

}
