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

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;

/**
 *
 */
public class TextFormFieldDocProperty extends AbstractDocProperty implements IFormFieldDocProperty {
  private final String m_propertyName;

  public TextFormFieldDocProperty(String propertyName, String header) {
    super(header);
    m_propertyName = propertyName;
  }

  /**
   * Reads the property of the form field and returns its value
   */
  @Override
  public String getText(IFormField field) {
    return StringUtility.nvl(field.getProperty(m_propertyName), "");
  }

}
