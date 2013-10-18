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
package org.eclipse.scout.rt.ui.swt.basic.table.command;

import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.swt.graphics.GC;

/**
 *
 */
public class ComputeRowHeightCommand implements ILayerCommand {

  private GC m_gc;
  private IConfigRegistry m_configRegistry;
  private int m_viewPortMinIndex;
  private int m_viewPortMaxIndex;

  public ComputeRowHeightCommand(GC gc, IConfigRegistry configRegistry) {
    m_gc = gc;
    m_configRegistry = configRegistry;
  }

  protected ComputeRowHeightCommand(ComputeRowHeightCommand command) {
    m_gc = command.getGc();
    m_configRegistry = command.getConfigRegistry();
    m_viewPortMinIndex = command.getViewPortMinIndex();
    m_viewPortMaxIndex = command.getViewPortMaxIndex();
  }

  @Override
  public boolean convertToTargetLayer(ILayer targetLayer) {
    return true;
  }

  @Override
  public ILayerCommand cloneCommand() {
    return new ComputeRowHeightCommand(this);
  }

  public GC getGc() {
    return m_gc;
  }

  public IConfigRegistry getConfigRegistry() {
    return m_configRegistry;
  }

  public void setViewPortMinIndex(int viewPortMinIndex) {
    m_viewPortMinIndex = viewPortMinIndex;
  }

  public int getViewPortMinIndex() {
    return m_viewPortMinIndex;
  }

  public void setViewPortMaxIndex(int viewPortMaxIndex) {
    m_viewPortMaxIndex = viewPortMaxIndex;
  }

  public int getViewPortMaxIndex() {
    return m_viewPortMaxIndex;
  }

}
