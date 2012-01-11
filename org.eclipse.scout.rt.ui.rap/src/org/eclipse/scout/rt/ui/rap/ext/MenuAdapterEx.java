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
package org.eclipse.scout.rt.ui.rap.ext;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.ui.rap.IRwtEnvironment;
import org.eclipse.scout.rt.ui.rap.keystroke.IRwtKeyStroke;
import org.eclipse.scout.rt.ui.rap.util.RwtUtility;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * <h3>MenuAdapterEx</h3> ...
 * 
 * @author sle
 * @since 3.7.0 June 2011
 */
public abstract class MenuAdapterEx extends MenuAdapter {
  private static final long serialVersionUID = 1L;

  private final Control m_menuControl;
  private final Control m_keyStrokeWidget;

  public MenuAdapterEx(Control menuControl, Control keyStrokeWidget) {
    m_menuControl = menuControl;
    m_keyStrokeWidget = keyStrokeWidget;
  }

  public IRwtEnvironment getUiEnvironment(Display display) {
    return (IRwtEnvironment) display.getData(IRwtEnvironment.class.getName());
  }

  public Control getMenuControl() {
    return m_menuControl;
  }

  public Control getKeyStrokeWidget() {
    return m_keyStrokeWidget;
  }

  protected abstract Menu getContextMenu();

  protected abstract void setContextMenu(Menu contextMenu);

  @Override
  public void menuShown(MenuEvent e) {
    final IRwtEnvironment uiEnvironment = getUiEnvironment(e.display);
    //add escape-keystroke to close the contextmenu with esc
    IKeyStroke scoutKeyStroke = new AbstractKeyStroke() {
      @Override
      protected String getConfiguredKeyStroke() {
        return "escape";
      }

      @Override
      protected void execAction() throws ProcessingException {
        final IKeyStroke keyStroke = this;
        uiEnvironment.invokeUiLater(new Runnable() {

          @Override
          public void run() {
            if (getContextMenu() != null && !getContextMenu().isDisposed()) {
              for (MenuItem item : getContextMenu().getItems()) {
                disposeMenuItem(item);
              }
              getContextMenu().dispose();
            }

            if ((getContextMenu() == null || getContextMenu().isDisposed())
                && !getMenuControl().isDisposed()
                && !getMenuControl().getShell().isDisposed()) {
              setContextMenu(new Menu(getMenuControl().getShell(), SWT.POP_UP));
              getContextMenu().addMenuListener(MenuAdapterEx.this);
              getMenuControl().setMenu(getContextMenu());
            }

            IRwtKeyStroke[] uiStrokes = RwtUtility.getKeyStrokes(keyStroke, uiEnvironment);
            for (IRwtKeyStroke uiStroke : uiStrokes) {
              uiEnvironment.removeKeyStroke(getKeyStrokeWidget(), uiStroke);
            }
          }
        });
      }
    };
    IRwtKeyStroke[] uiStrokes = RwtUtility.getKeyStrokes(scoutKeyStroke, uiEnvironment);
    for (IRwtKeyStroke uiStroke : uiStrokes) {
      uiEnvironment.addKeyStroke(getKeyStrokeWidget(), uiStroke);
    }

    // clear all previous
    // Windows BUG: fires menu hide before the selection on the menu item is
    // propagated.
    if (getContextMenu() != null) {
      for (MenuItem item : getContextMenu().getItems()) {
        disposeMenuItem(item);
      }
    }
  }

  protected void disposeMenuItem(MenuItem item) {
    Menu menu = item.getMenu();
    if (menu != null) {
      for (MenuItem childItem : menu.getItems()) {
        disposeMenuItem(childItem);
      }
      menu.dispose();
    }
    item.dispose();
  }
}