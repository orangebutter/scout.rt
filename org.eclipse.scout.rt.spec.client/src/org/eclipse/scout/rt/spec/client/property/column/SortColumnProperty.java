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
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.spec.client.property.AbstractNamedDocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

/**
 * Doc Property for {@link IColumn#getSortIndex()}.
 */
public class SortColumnProperty extends AbstractNamedDocProperty<IColumn> implements IDocProperty<IColumn> {

  public SortColumnProperty() {
    super(TEXTS.get("org.eclipse.scout.rt.spec.sort"));
  }

  /**
   * @param column
   *          {@link IColumn}
   * @return the sort index of a column as {@link String}, if positive. An empty String otherwise.
   */
  @Override
  public String getText(IColumn column) {
    int sortIndex = column.getSortIndex();
    if (sortIndex > 0) {
      return String.valueOf(sortIndex);
    }
    return "";
  }
}
