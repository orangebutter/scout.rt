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
package org.eclipse.scout.rt.spec.client.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.form.fields.AbstractFormField;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.spec.client.out.TableDescriptor;
import org.eclipse.scout.rt.spec.client.property.form.field.IdFormFieldProperty;
import org.junit.Test;

/**
 * Tests for {@link DocPropertyUtility}
 */
public class DocPropertyUtilityTest {

  private static final String TEST_DOC = "testDoc";

  /**
   * Test for {@link DocPropertyUtility#getHeaders(List)}
   */
  @Test
  public void testGetHeaders() {
    List<IDocProperty<IFormField>> testProperties = getTestProperties();
    String[] headers = DocPropertyUtility.getHeaders(testProperties);
    assertEquals(2, headers.length);
    assertEquals(testProperties.get(0).getHeader(), headers[0]);
    assertEquals(testProperties.get(1).getHeader(), headers[1]);
  }

  /**
   * Test for {@link DocPropertyUtility#getHeaders(List)}
   */
  @Test
  public void testGetHeadersNull() {
    String[] headers = DocPropertyUtility.getHeaders(null);
    assertEquals(0, headers.length);
  }

  /**
   * Test for {@link DocPropertyUtility#getHeaders(List)}
   */
  @Test
  public void testGetHeadersEmpty() {
    String[] headers = DocPropertyUtility.getHeaders(new ArrayList<IDocProperty<IFormField>>());
    assertEquals(0, headers.length);
  }

  /**
   * Test for {@link DocPropertyUtility#getTexts(Object, List)}
   */
  @Test
  public void testGetPropertyRow() {
    IFormField testField = new TestFormField();
    List<IDocProperty<IFormField>> testProperties = getTestProperties();
    String[] texts = DocPropertyUtility.getTexts(testField, testProperties);
    assertEquals(2, texts.length);
    assertEquals(TEST_DOC, texts[0]);
  }

  /**
   * Test for {@link DocPropertyUtility#getTexts(Object, List)}
   */
  @Test
  public void testGetPropertyRowEmpty() {
    IFormField testField = new TestFormField();
    String[] texts = DocPropertyUtility.getTexts(testField, null);
    assertEquals(0, texts.length);
  }

  /**
   * Test for {@link DocPropertyUtility#isAccepted(Object, List)}
   */
  @Test
  public void testNotAccepted() {
    IFormField testField = new TestFormField();
    List<IDocFilter<IFormField>> filters = getTestFilters(false);
    boolean accepted = DocPropertyUtility.isAccepted(testField, filters);
    assertFalse(accepted);
  }

  /**
   * Test for {@link DocPropertyUtility#isAccepted(Object, List)}
   */
  @Test
  public void testAccepted() {
    IFormField testField = new TestFormField();
    List<IDocFilter<IFormField>> filters = getTestFilters(true);
    boolean accepted = DocPropertyUtility.isAccepted(testField, filters);
    assertTrue(accepted);
  }

  /**
   * Test for {@link DocPropertyUtility#isAccepted(Object, List)} without filters
   */
  @Test
  public void testAcceptedNoFilters() {
    IFormField testField = new TestFormField();
    boolean accepted = DocPropertyUtility.isAccepted(testField, null);
    assertTrue(accepted);
  }

  /**
   * {@link DocPropertyUtility#createTableDesc(Object[], List, List)}
   */
  @Test
  public void createTableDesc() {
    IFormField testField = new TestFormField();
    IFormField[] fields = new IFormField[1];
    fields[0] = testField;
    List<IDocProperty<IFormField>> testProperties = getTestProperties();
    TableDescriptor testTable = DocPropertyUtility.createTableDesc(fields, testProperties, null);
    String[][] cellTexts = testTable.getCellTexts();
    String cellText = cellTexts[0][0];
    assertEquals(TEST_DOC, cellText);
  }

  /**
   * {@link DocPropertyUtility#createTableDesc(Object[], List, List)} with a filter accepting no properties.
   */
  @Test
  public void createTableDescWithFilter() {
    IFormField testField = new TestFormField();
    IFormField[] fields = new IFormField[1];
    fields[0] = testField;
    List<IDocProperty<IFormField>> testProperties = getTestProperties();
    List<IDocFilter<IFormField>> testFilters = getTestFilters(false);
    TableDescriptor testTable = DocPropertyUtility.createTableDesc(fields, testProperties, testFilters);
    String[][] cellTexts = testTable.getCellTexts();
    assertEquals(0, cellTexts.length);
  }

  /**
   * A {@link AbstractFormField} for testing
   **/
  class TestFormField extends AbstractFormField {

    /**
     * test documentation
     */
    @Override
    protected String getConfiguredDoc() {
      return TEST_DOC;
    }
  }

  private List<IDocProperty<IFormField>> getTestProperties() {
    List<IDocProperty<IFormField>> testProperties = new ArrayList<IDocProperty<IFormField>>();
    DocProperty<IFormField> docProperty = new DocProperty<IFormField>();
    IdFormFieldProperty idProperty = new IdFormFieldProperty();
    testProperties.add(docProperty);
    testProperties.add(idProperty);
    return testProperties;
  }

  private List<IDocFilter<IFormField>> getTestFilters(final boolean accept) {
    List<IDocFilter<IFormField>> filters = new ArrayList<IDocFilter<IFormField>>();
    IDocFilter<IFormField> testFilter = new IDocFilter<IFormField>() {
      @Override
      public boolean accept(IFormField object) {
        return accept;
      }
    };

    filters.add(testFilter);
    return filters;
  }

}
