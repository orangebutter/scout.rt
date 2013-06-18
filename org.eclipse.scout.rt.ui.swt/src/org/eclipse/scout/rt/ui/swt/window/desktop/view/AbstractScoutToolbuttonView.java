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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.OptimisticLock;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.client.ui.action.tool.IToolButton;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.IFormToolButton;
import org.eclipse.scout.rt.ui.swt.ISwtEnvironment;
import org.eclipse.scout.rt.ui.swt.form.ISwtScoutForm;
import org.eclipse.scout.rt.ui.swt.window.desktop.view.internal.SwtScoutFormToolButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.part.ViewPart;

/**
 *
 */
@SuppressWarnings("restriction")
public abstract class AbstractScoutToolbuttonView extends ViewPart {
  private static final IScoutLogger LOG = ScoutLogManager.getLogger(AbstractScoutToolbuttonView.class);
  public static String SIZE_PROP = AbstractScoutToolbuttonView.class.getName() + "_VIEW_WIDTH";

  private Composite m_viewControl;
  private Composite m_buttonContainer;

  private ISwtScoutForm m_uiForm;

  private Button m_minimizeButton;
  private IToolButton m_selectedButton;

  private boolean m_handleActionPending;
  private OptimisticLock m_cacheWidthLock = new OptimisticLock();

  public AbstractScoutToolbuttonView() {
  }

  @Override
  public void createPartControl(Composite parent) {
    m_buttonContainer = getSwtEnvironment().getFormToolkit().createComposite(parent);
    m_viewControl = parent;

    //layout
    GridLayout gridlayout = new GridLayout(1, false);
    gridlayout.horizontalSpacing = 0;
    gridlayout.marginHeight = 0;
    gridlayout.marginWidth = 0;
    gridlayout.verticalSpacing = 0;
    parent.setLayout(gridlayout);
    GridDataFactory.fillDefaults().grab(false, true).applyTo(m_buttonContainer);
  }

  public void setDesktop(IDesktop desktop) {
    // Close button
    m_minimizeButton = getSwtEnvironment().getFormToolkit().createButton(m_buttonContainer, "", SWT.PUSH);
    m_minimizeButton.setImage(getSwtEnvironment().getIcon("toolbar_view_minimize"));
    m_minimizeButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        handleSwtMinimizeAction();
      }
    });
    GridDataFactory.fillDefaults().applyTo(m_minimizeButton);
    IToolButton[] toolButtons = getSwtEnvironment().getClientSession().getDesktop().getToolButtons();
    for (final IToolButton b : toolButtons) {
      if (b instanceof IFormToolButton) {
        SwtScoutFormToolButton swtButton = new SwtScoutFormToolButton();
        swtButton.createField(m_buttonContainer, b, getSwtEnvironment());
        final P_ToolButtonPropertyChangeListener buttonPropertyChangedListener = new P_ToolButtonPropertyChangeListener(b);
        b.addPropertyChangeListener(IToolButton.PROP_SELECTED, buttonPropertyChangedListener);
        swtButton.getSwtField().addDisposeListener(new DisposeListener() {

          @Override
          public void widgetDisposed(DisposeEvent e) {
            b.removePropertyChangeListener(IToolButton.PROP_SELECTED, buttonPropertyChangedListener);
          }
        });
        GridDataFactory.fillDefaults().applyTo(swtButton.getSwtContainer());
      }
    }
    m_buttonContainer.setLayout(new GridLayout(1, false));
    m_viewControl.layout(true);
    m_viewControl.getDisplay().asyncExec(new Runnable() {

      @Override
      public void run() {
        ((WorkbenchPage) getViewSite().getPart().getSite().getPage()).resizeView(AbstractScoutToolbuttonView.this, m_buttonContainer.getBounds().width - 3, m_viewControl.getBounds().height);
      }
    });
  }

  @Override
  public void setFocus() {
    m_buttonContainer.setFocus();
  }

  protected void handleSwtMinimizeAction() {
    if (!m_handleActionPending) {
      //notify Scout
      Runnable t = new Runnable() {
        @Override
        public void run() {
          if (m_selectedButton != null) {
            try {
              m_handleActionPending = true;
              m_selectedButton.getUIFacade().setSelectedFromUI(false);
            }
            finally {
              m_handleActionPending = false;
            }
          }
        }
      };
      getSwtEnvironment().invokeScoutLater(t, 0);
      //end notify
    }
  }

  protected void updateMinimizeButton() {
    m_minimizeButton.setEnabled(m_selectedButton != null);
  }

  /**
   * must be implemented by the concrete view to provide an environment
   * 
   * @return
   */
  protected abstract ISwtEnvironment getSwtEnvironment();

  private class P_ToolButtonPropertyChangeListener implements PropertyChangeListener {
    private final IToolButton m_scoutObject;

    private P_ToolButtonPropertyChangeListener(IToolButton scoutObject) {
      m_scoutObject = scoutObject;

    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
      getSwtEnvironment().invokeSwtLater(new Runnable() {

        @Override
        public void run() {

          if ((Boolean) evt.getNewValue()) {
            m_selectedButton = getScoutObject();
            updateMinimizeButton();
          }
          else {
            if (CompareUtility.equals(m_selectedButton, getScoutObject())) {
              m_selectedButton = null;
              updateMinimizeButton();
            }
          }
        }
      });
    }

    public IToolButton getScoutObject() {
      return m_scoutObject;
    }
  }
}
