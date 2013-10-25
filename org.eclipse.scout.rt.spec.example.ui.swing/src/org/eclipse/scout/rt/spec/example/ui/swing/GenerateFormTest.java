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
package org.eclipse.scout.rt.spec.example.ui.swing;

import java.util.Locale;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.spec.client.gen.AbstractFormSpecGen;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm;
import org.eclipse.scout.testing.client.runner.ScoutClientGUITestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ScoutClientGUITestRunner.class)
public class GenerateFormTest extends AbstractFormSpecGen {

  public void setUp() {
    Locale.setDefault(new Locale("de", "CH"));
  }

  @Test
  public void createForm() throws ProcessingException {
    printForm();
    printAllFields();
  }

  @Override
  protected IForm createAndStartForm() throws ProcessingException {
    TestPersonForm form = new TestPersonForm();
    form.startNew();
    return form;
  }

  @Override
  protected String getPluginName() throws ProcessingException {
    return Activator.PLUGIN_ID;
  }

}
