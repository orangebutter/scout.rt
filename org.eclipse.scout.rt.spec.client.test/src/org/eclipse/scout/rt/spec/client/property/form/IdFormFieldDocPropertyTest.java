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

import static org.junit.Assert.assertEquals;

import org.eclipse.scout.rt.client.ui.form.fields.AbstractFormField;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.spec.client.property.form.IdFormFieldDocProperty;
import org.junit.Test;

/**
 * Test for {@link IdFormFieldDocProperty}
 */
public class IdFormFieldDocPropertyTest {

  /**
   * The id of a simple Field should be its class name.
   */
  @Test
  public void testIDText() {
    IFormField testField = new TestFormField();
    IdFormFieldDocProperty p = new IdFormFieldDocProperty("Id");

    String actualText = p.getText(testField);
    assertEquals("Doc Text Invalid", TestFormField.class.getName(), actualText);
  }

  class TestFormField extends AbstractFormField {
  }

}
