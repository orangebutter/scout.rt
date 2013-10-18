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

import org.eclipse.nebula.widgets.nattable.command.ILayerCommandHandler;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;

/**
 *
 */
public class ViewportComputeRowHeightCommandHandler implements ILayerCommandHandler<ComputeRowHeightCommand> {

  @Override
  public Class<ComputeRowHeightCommand> getCommandClass() {
    return ComputeRowHeightCommand.class;
  }

  @Override
  public boolean doCommand(ILayer targetLayer, ComputeRowHeightCommand command) {
    ViewportLayer viewport = (ViewportLayer) targetLayer;
    command.setViewPortMinIndex(viewport.localToUnderlyingRowPosition(viewport.getMinimumOriginRowPosition()));
    command.setViewPortMaxIndex(viewport.localToUnderlyingRowPosition(viewport.getMinimumOriginRowPosition() + viewport.getRowCount()));

    return false;
  }

}
