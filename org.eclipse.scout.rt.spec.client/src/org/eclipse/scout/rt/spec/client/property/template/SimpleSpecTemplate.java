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
import org.eclipse.scout.rt.spec.client.property.form.BooleanFormFieldDocProperty;
import org.eclipse.scout.rt.spec.client.property.form.IFormFieldDocProperty;
import org.eclipse.scout.rt.spec.client.property.form.IdFormFieldDocProperty;
import org.eclipse.scout.rt.spec.client.property.form.TextFormFieldDocProperty;
import org.eclipse.scout.rt.spec.client.property.form.TypeFormFieldDocProperty;

/**
 *
 */
public class SimpleSpecTemplate implements ISpecTemplate {

  @Override
  public List<IFormFieldDocProperty> getFieldProperties() {
    List<IFormFieldDocProperty> propertyTemplate = new ArrayList<IFormFieldDocProperty>();
    propertyTemplate.add(new IdFormFieldDocProperty(TEXTS.get("org.eclipse.scout.rt.spec.id")));
    propertyTemplate.add(new TextFormFieldDocProperty(IFormField.PROP_LABEL, TEXTS.get("org.eclipse.scout.rt.spec.label")));
    propertyTemplate.add(new TypeFormFieldDocProperty(TEXTS.get("org.eclipse.scout.rt.spec.type")));
    propertyTemplate.add(new TextFormFieldDocProperty(IStringField.PROP_MAX_LENGTH, TEXTS.get("org.eclipse.scout.rt.spec.length")));
    propertyTemplate.add(new BooleanFormFieldDocProperty(IFormField.PROP_MANDATORY, TEXTS.get("org.eclipse.scout.rt.spec.mandatory")));
    propertyTemplate.add(new BooleanFormFieldDocProperty(IFormField.PROP_ENABLED, TEXTS.get("org.eclipse.scout.rt.spec.enabled")));
    propertyTemplate.add(new TextFormFieldDocProperty(IFormField.PROP_TOOLTIP_TEXT, TEXTS.get("org.eclipse.scout.rt.spec.tooltip")));
    return propertyTemplate;
  }

}
