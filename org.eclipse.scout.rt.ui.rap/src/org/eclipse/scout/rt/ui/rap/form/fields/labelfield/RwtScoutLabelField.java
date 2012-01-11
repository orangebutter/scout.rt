/*******************************************************************************
 * Copyright (c) 2011 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.scout.rt.ui.rap.form.fields.labelfield;

import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.labelfield.ILabelField;
import org.eclipse.scout.rt.ui.rap.LogicalGridLayout;
import org.eclipse.scout.rt.ui.rap.core.LogicalGridData;
import org.eclipse.scout.rt.ui.rap.core.util.RwtLayoutUtility;
import org.eclipse.scout.rt.ui.rap.ext.StatusLabelEx;
import org.eclipse.scout.rt.ui.rap.extension.UiDecorationExtensionPoint;
import org.eclipse.scout.rt.ui.rap.form.fields.LogicalGridDataBuilder;
import org.eclipse.scout.rt.ui.rap.form.fields.RwtScoutValueFieldComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * <h3>RwtScoutLabelField</h3> ...
 * 
 * @since 3.7.0 June 2011
 */
public class RwtScoutLabelField extends RwtScoutValueFieldComposite<ILabelField> implements IRwtScoutLabelField {

  @Override
  protected void initializeUi(Composite parent) {
    Composite container = getUiEnvironment().getFormToolkit().createComposite(parent);
    // label
    if (getScoutObject().isLabelVisible()) {
      int labelStyle = UiDecorationExtensionPoint.getLookAndFeel().getFormFieldLabelAlignment();
      StatusLabelEx label = new StatusLabelEx(container, labelStyle);
      getUiEnvironment().getFormToolkit().getFormToolkit().adapt(label, false, false);
      setUiLabel(label);
    }
    //
    int style = SWT.NONE;
    if (getScoutObject().isWrapText()) {
      style |= SWT.WRAP;
    }
    Label text = getUiEnvironment().getFormToolkit().createLabel(container, "", style);
    LogicalGridData textData = LogicalGridDataBuilder.createField(((IFormField) getScoutObject()).getGridData());
    textData.topInset = 4;
    text.setLayoutData(textData);
    setUiField(text);
    //
    container.setTabList(new Control[]{});
    setUiContainer(container);
    // layout
    getUiContainer().setLayout(new LogicalGridLayout(1, 0));
  }

  @Override
  public Label getUiField() {
    return (Label) super.getUiField();
  }

  @Override
  protected void setDisplayTextFromScout(String s) {
    String oldText = getUiField().getText();
    if (s == null) {
      s = "";
    }
    if (oldText == null) {
      oldText = "";
    }
    if (oldText.equals(s)) {
      return;
    }
    getUiField().setText(s);
    RwtLayoutUtility.invalidateLayout(getUiEnvironment(), getUiField());
  }

}