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
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link TextFormFieldDocProperty}
 */
public class TextFormFieldDocPropertyTest {

  /**
   * Test {@link TextFormFieldDocProperty#getText(IFormField)} for a label.
   */
  @Test
  public void testSpecsPropertyLabel() {
    IFormField fieldWithLabel = new AbstractFormField() {
    };
    String testLabel = "testLabel";
    fieldWithLabel.setLabel(testLabel);
    TextFormFieldDocProperty p = new TextFormFieldDocProperty(IFormField.PROP_LABEL, "Label");

    String actualText = p.getText(fieldWithLabel);
    Assert.assertEquals("Boolean Doc Text Invalid", testLabel, actualText);
  }

  /**
   * Test {@link TextFormFieldDocProperty#getText(IFormField)} for a label.
   */
  @Test
  public void testSpecsPropertyNullLabel() {
    IFormField fieldWithLabel = new AbstractFormField() {
    };
    fieldWithLabel.setLabel(null);
    TextFormFieldDocProperty p = new TextFormFieldDocProperty(IFormField.PROP_LABEL, "Label");

    String actualText = p.getText(fieldWithLabel);
    Assert.assertEquals("Boolean Doc Text Invalid", "", actualText);
  }

}
