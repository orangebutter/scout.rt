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
package org.eclipse.scout.rt.ui.swt.basic.table.converter;

import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;

/**
 *
 */
public class StringColumnDisplayConverter extends DisplayConverter {

  @Override
  public Object canonicalToDisplayValue(Object canonicalValue) {
    return ((ICell) canonicalValue).getText();
  }

  @Override
  public Object displayToCanonicalValue(Object displayValue) {
    throw new IllegalArgumentException("no display text to ICell transformation! ");
  }

}
