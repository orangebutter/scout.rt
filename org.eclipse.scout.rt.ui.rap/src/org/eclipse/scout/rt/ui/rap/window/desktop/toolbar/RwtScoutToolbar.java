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
package org.eclipse.scout.rt.ui.rap.window.desktop.toolbar;

import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.ui.rap.basic.RwtScoutComposite;
import org.eclipse.scout.rt.ui.rap.busy.RwtBusyHandler;
import org.eclipse.scout.rt.ui.rap.util.RwtUtility;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * <h3>RwtScoutToolbar</h3> ...
 * 
 * @author Andreas Hoegger
 * @since 3.7.0 June 2011
 */
public class RwtScoutToolbar extends RwtScoutComposite<IDesktop> {

  private static final String VARIANT_TOOL_BUTTON_BAR = "toolButtonBar";
  private static final String VARIANT_TOOL_BUTTON_BAR_ACTIVE = "toolButtonBar-active";
  private static final String VARIANT_TOOLBAR_CONTAINER = "toolbarContainer";
  private static final String VARIANT_TOOL_BUTTON_BUTTON_ACTIVE = "toolButton-active";
  private static final String VARIANT_TOOL_BUTTON = "toolButton";
  private RwtScoutToolButtonBar m_toolButtonBar;
  private Composite m_busyIndicator;
  private RwtScoutViewButtonBar m_viewButtonBar;

  @Override
  protected void initializeUi(Composite parent) {
    Composite container = getUiEnvironment().getFormToolkit().createComposite(parent);
    container.setData(WidgetUtil.CUSTOM_VARIANT, VARIANT_TOOLBAR_CONTAINER);

    Control viewButtonbar = createViewButtonBar(container);
    Control busyIndicator = createBusyIndicator(container);
    Control toolButtonBar = createToolButtonBar(container);

    //layout
    container.setLayout(RwtUtility.createGridLayoutNoSpacing(3, false));
    viewButtonbar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_END));
    busyIndicator.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
    toolButtonBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_END));
    setUiContainer(container);
  }

  public void handleRightViewPositionChanged(int rightViewX) {
    GridData gridData = (GridData) getToolButtonBar().getUiContainer().getLayoutData();
    if (rightViewX > 0) {
      gridData.widthHint = getUiContainer().getSize().x - rightViewX;
    }
    else {
      gridData.widthHint = -1;
    }
    getUiContainer().layout();

  }

  protected Control createViewButtonBar(Composite parent) {
    m_viewButtonBar = new RwtScoutViewButtonBar();
    m_viewButtonBar.createUiField(parent, getScoutObject(), getUiEnvironment());
    return m_viewButtonBar.getUiContainer();
  }

  protected Control createBusyIndicator(Composite parent) {
    Composite busyComposite = getUiEnvironment().getFormToolkit().createComposite(parent);
    busyComposite.setData(WidgetUtil.CUSTOM_VARIANT, RwtBusyHandler.CUSTOM_VARIANT_CSS_NAME);
    busyComposite.setLayout(new Layout() {
      private static final long serialVersionUID = 1L;

      @Override
      protected void layout(Composite composite, boolean flushCache) {
      }

      @Override
      protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
        return new Point(1, 1);
      }
    });
    busyComposite.setVisible(false);
    //register
    getUiEnvironment().getClientSession().setData(RwtBusyHandler.BUSY_CONTROL_CLIENT_SESSION_KEY, busyComposite);
    return busyComposite;
  }

  protected Control createToolButtonBar(Composite parent) {
    m_toolButtonBar = new RwtScoutToolButtonBar();
    m_toolButtonBar.createUiField(parent, getScoutObject(), getUiEnvironment());
    return m_toolButtonBar.getUiContainer();
  }

  /**
   * @return the viewButtonBar
   */
  public RwtScoutViewButtonBar getViewButtonBar() {
    return m_viewButtonBar;
  }

  /**
   * @return the toolButtonBar
   */
  public RwtScoutToolButtonBar getToolButtonBar() {
    return m_toolButtonBar;
  }
}