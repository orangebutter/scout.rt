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
package org.eclipse.scout.rt.ui.swt.basic.table.configuration;

import org.eclipse.nebula.widgets.nattable.layer.config.DefaultColumnHeaderLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultColumnHeaderStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.ESBeveledBorderDecorator;

/**
 *
 */
public class EsColumnHeaderLayerConfiguration extends DefaultColumnHeaderLayerConfiguration {

  @Override
  protected void addColumnHeaderStyleConfig() {
    DefaultColumnHeaderStyleConfiguration configuration = new DefaultColumnHeaderStyleConfiguration();
    configuration.cellPainter = new ESBeveledBorderDecorator(new TextPainter(true, true, true));
    addConfiguration(configuration);
  }
}
