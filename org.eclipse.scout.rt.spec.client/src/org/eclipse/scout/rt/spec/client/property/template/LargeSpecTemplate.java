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

import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.IStringField;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.spec.client.property.DocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;
import org.eclipse.scout.rt.spec.client.property.SimpleTypeProperty;
import org.eclipse.scout.rt.spec.client.property.form.field.BooleanFormFieldProperty;
import org.eclipse.scout.rt.spec.client.property.form.field.IdFormFieldProperty;
import org.eclipse.scout.rt.spec.client.property.form.field.TextFormFieldProperty;

/**
   *
   */
public class LargeSpecTemplate extends DefaultSpecTemplate {

  @Override
  public List<IDocProperty<IFormField>> getFieldProperties() {
    List<IDocProperty<IFormField>> propertyTemplate = new ArrayList<IDocProperty<IFormField>>();
    propertyTemplate.add(new IdFormFieldProperty());
    propertyTemplate.add(new TextFormFieldProperty(IFormField.PROP_LABEL, TEXTS.get("org.eclipse.scout.rt.spec.label")));
    propertyTemplate.add(new SimpleTypeProperty<IFormField>());
    propertyTemplate.add(new TextFormFieldProperty(IStringField.PROP_MAX_LENGTH, TEXTS.get("org.eclipse.scout.rt.spec.length")));
    propertyTemplate.add(new BooleanFormFieldProperty(IFormField.PROP_MANDATORY, TEXTS.get("org.eclipse.scout.rt.spec.mandatory")));
    propertyTemplate.add(new BooleanFormFieldProperty(IFormField.PROP_ENABLED, TEXTS.get("org.eclipse.scout.rt.spec.enabled")));
    propertyTemplate.add(new TextFormFieldProperty(IFormField.PROP_TOOLTIP_TEXT, TEXTS.get("org.eclipse.scout.rt.spec.tooltip")));
    propertyTemplate.add(new DocProperty<IFormField>());
    return propertyTemplate;
  }

}
