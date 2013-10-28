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
package org.eclipse.scout.rt.spec.client.property.column;

import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.spec.client.property.IDocFilter;

/**
 * A filter accepting only displayable columns.
 */
public class DisplayableColumnFilter implements IDocFilter<IColumn> {

  /**
   * @param column
   *          (may not be <code>null</code>)
   * @return true for displayable column
   */
  @Override
  public boolean accept(IColumn c) {
    return c.isDisplayable();
  }

}
