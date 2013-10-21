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
package org.eclipse.scout.rt.spec.client.property.form;

import org.eclipse.scout.rt.client.ui.form.fields.ICompositeField;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;

/**
 *
 */
public class IdFormFieldDocProperty extends AbstractDocProperty implements IFormFieldDocProperty {

  /**
   * @param name
   */
  public IdFormFieldDocProperty(String name) {
    super(name);
  }

  /**
   * A unique id text for the field.
   */
  @Override
  public String getText(IFormField field) {
    return getUniqueFieldId(field);
  }

  public String getUniqueFieldId(IFormField formField) {
    StringBuilder uniqueId = new StringBuilder();
    uniqueId.append(formField.getClass().getName());
    for (ICompositeField enclosingField : formField.getEnclosingFieldList()) {
      uniqueId.append("|").append(enclosingField.getClass().getName());
    }
    return uniqueId.toString();
  }

}
