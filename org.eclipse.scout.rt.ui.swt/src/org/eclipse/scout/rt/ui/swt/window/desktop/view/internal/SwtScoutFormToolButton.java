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
package org.eclipse.scout.rt.ui.swt.window.desktop.view.internal;

import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.KeyStroke;
import org.eclipse.scout.rt.client.ui.action.tool.IToolButton;
import org.eclipse.scout.rt.ui.swt.basic.SwtScoutComposite;
import org.eclipse.scout.rt.ui.swt.keystroke.ISwtKeyStroke;
import org.eclipse.scout.rt.ui.swt.util.SwtLayoutUtility;
import org.eclipse.scout.rt.ui.swt.util.SwtUtility;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 *
 */
public class SwtScoutFormToolButton extends SwtScoutComposite<IToolButton> {

  //ticket 86811: avoid double-action in queue
  private boolean m_handleActionPending;
  // cache
  private ISwtKeyStroke[] m_swtKeyStrokes;

  @Override
  protected void initializeSwt(Composite parent) {
    Composite container = getEnvironment().getFormToolkit().createComposite(parent);
    Button button = getEnvironment().getFormToolkit().createButton(container, "", SWT.TOGGLE);
    button.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleSwtClickAction();
      }
    });
    setSwtContainer(container);
    setSwtField(button);
    // layout
    FillLayout containerLayout = new FillLayout();
    container.setLayout(containerLayout);
  }

  @Override
  protected void attachScout() {
    super.attachScout();
    updateEnabledFromScout();
    updateIconIdFromScout();
    updateSelectionFromScout();
    updateKeyStrokeFromScout();
    updateTooltipFromScout();
    updateVisibleFromScout();
  }

  @Override
  public Button getSwtField() {
    return (Button) super.getSwtField();
  }

  protected void handleSwtClickAction() {
    if (!m_handleActionPending) {
      m_handleActionPending = true;
      //notify Scout
      Runnable t = new Runnable() {
        @Override
        public void run() {
          try {
            getScoutObject().getUIFacade().setSelectedFromUI(!getScoutObject().isSelected());
          }
          finally {
            m_handleActionPending = false;
          }
        }
      };
      getEnvironment().invokeScoutLater(t, 0);
      //end notify
    }
  }

  /**
   *
   */
  protected void updateEnabledFromScout() {
    getSwtField().setEnabled(getScoutObject().isEnabled());
  }

  /**
   *
   */
  protected void updateIconIdFromScout() {
    String iconId = getScoutObject().getIconId();
    if (iconId == null) {
      getSwtField().setImage(null);
    }
    else {
      Image icon = getEnvironment().getIcon(iconId);
      getSwtField().setImage(icon);
    }
  }

  /**
   *
   */
  protected void updateSelectionFromScout() {
    getSwtField().setSelection(getScoutObject().isSelected());
  }

  protected void updateKeyStrokeFromScout() {
    // remove old
    if (m_swtKeyStrokes != null) {
      for (ISwtKeyStroke swtStroke : m_swtKeyStrokes) {
        getEnvironment().removeGlobalKeyStroke(swtStroke);
      }
    }
    m_swtKeyStrokes = null;
    if (getScoutObject().getKeyStroke() != null) {
      IKeyStroke scoutKeyStroke = new KeyStroke(getScoutObject().getKeyStroke());
      m_swtKeyStrokes = SwtUtility.getKeyStrokes(scoutKeyStroke, getEnvironment());
      for (ISwtKeyStroke swtStroke : m_swtKeyStrokes) {
        getEnvironment().addGlobalKeyStroke(swtStroke);
      }
    }
  }

  /**
   *
   */
  private void updateVisibleFromScout() {
    boolean visible = getScoutObject().isVisible();
    if (visible != getSwtContainer().getVisible()) {
      getSwtContainer().setVisible(visible);
      SwtLayoutUtility.invalidateLayout(getSwtContainer());
    }
  }

  /**
   *
   */
  protected void updateTooltipFromScout() {
    getSwtField().setToolTipText(getScoutObject().getTooltipText());
  }

  /**
   * scout property handler override
   */
  @Override
  protected void handleScoutPropertyChange(String name, Object newValue) {

    super.handleScoutPropertyChange(name, newValue);
    if (name.equals(IToolButton.PROP_ENABLED)) {
      updateEnabledFromScout();
    }
    else if (name.equals(IToolButton.PROP_ICON_ID)) {
      updateIconIdFromScout();
    }
    else if (name.equals(IToolButton.PROP_SELECTED)) {
      updateSelectionFromScout();
    }
    else if (name.equals(IToolButton.PROP_KEYSTROKE)) {
      updateKeyStrokeFromScout();
    }
    else if (name.equals(IToolButton.PROP_TOOLTIP_TEXT)) {
      updateTooltipFromScout();
    }
    else if (name.equals(IToolButton.PROP_VISIBLE)) {
      updateVisibleFromScout();
    }
  }

}
