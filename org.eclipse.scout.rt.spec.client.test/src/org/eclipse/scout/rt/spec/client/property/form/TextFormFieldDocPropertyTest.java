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

import org.eclipse.scout.rt.client.ui.form.fields.AbstractFormField;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.spec.client.property.form.field.TextFormFieldProperty;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link TextFormFieldProperty}
 */
public class TextFormFieldDocPropertyTest {

  /**
   * Test {@link TextFormFieldProperty#getText(IFormField)} for a label.
   */
  @Test
  public void testSpecsPropertyLabel() {
    IFormField fieldWithLabel = new AbstractFormField() {
    };
    String testLabel = "testLabel";
    fieldWithLabel.setLabel(testLabel);
    TextFormFieldProperty p = new TextFormFieldProperty(IFormField.PROP_LABEL, "Label");

    String actualText = p.getText(fieldWithLabel);
    Assert.assertEquals("Boolean Doc Text Invalid", testLabel, actualText);
  }

  /**
   * Test {@link TextFormFieldProperty#getText(IFormField)} for a label.
   */
  @Test
  public void testSpecsPropertyNullLabel() {
    IFormField fieldWithLabel = new AbstractFormField() {
    };
    fieldWithLabel.setLabel(null);
    TextFormFieldProperty p = new TextFormFieldProperty(IFormField.PROP_LABEL, "Label");

    String actualText = p.getText(fieldWithLabel);
    Assert.assertEquals("Boolean Doc Text Invalid", "", actualText);
  }

}
