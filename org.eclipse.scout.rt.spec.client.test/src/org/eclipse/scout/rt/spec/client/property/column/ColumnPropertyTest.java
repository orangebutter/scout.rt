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

import static org.junit.Assert.assertEquals;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;
import org.junit.Test;

/**
 * Tests for default column properties: {@link IDocProperty}<{@link IColumn}>.
 */
public class ColumnPropertyTest {

  /**
   * Test for {@link SortColumnProperty#getText(org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn)}
   * 
   * @throws ProcessingException
   */
  @Test
  public void testSortColumnProperty() {
    SortColumnProperty sortColumnProperty = new SortColumnProperty();
    final int TEST_SORT_INDEX = 333;
    AbstractColumn c = new AbstractColumn<Long>() {
      @Override
      public int getSortIndex() {
        return TEST_SORT_INDEX;
      }
    };
    String text = sortColumnProperty.getText(c);
    assertEquals(String.valueOf(TEST_SORT_INDEX), text);
  }

  /**
   * Test for {@link SortColumnProperty#getText(org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn)} for the
   * default sort index
   * 
   * @throws ProcessingException
   */
  @Test
  public void testSortColumnPropertyDefault() {
    SortColumnProperty sortColumnProperty = new SortColumnProperty();
    AbstractColumn c = new AbstractColumn<Long>() {
    };
    String text = sortColumnProperty.getText(c);
    assertEquals("", text);
  }
}
