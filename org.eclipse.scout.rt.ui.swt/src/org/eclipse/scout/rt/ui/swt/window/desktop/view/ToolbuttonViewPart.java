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
package org.eclipse.scout.rt.ui.swt.window.desktop.view;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.scout.rt.ui.swt.ISwtEnvironment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 *
 */
public class ToolbuttonViewPart extends ViewPart {
  public static final String VIEW_ID = "org.eclipse.scout.rt.ui.swt.window.desktop.view.ToolbuttonViewPart";

  private Composite m_formArea;
  private Composite m_toolButtonBar;

  public void initialize(ISwtEnvironment environment) {
    System.out.println("init toolbuttons");
  }

  @Override
  public void createPartControl(Composite parent) {
    m_formArea = new Composite(parent, SWT.NONE);
    m_toolButtonBar = new Composite(parent, SWT.NONE);
    // layout
    parent.setLayout(new GridLayout(2, false));
    GridDataFactory.fillDefaults().grab(true, true).applyTo(m_formArea);
    GridDataFactory.fillDefaults().grab(false, true).applyTo(m_toolButtonBar);
  }

  @Override
  public void setFocus() {
  }

}
