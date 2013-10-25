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
package org.eclipse.scout.rt.spec.client.writer;

import java.io.IOException;
import java.io.StringWriter;

import org.eclipse.scout.rt.spec.client.out.TableDescriptor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link WikimediaFormWriter}
 */
public class WikimediaWriterTest {

  /**
   * Test for {@link WikimediaWriter#appendTable(TableDescriptor)}
   * 
   * @throws IOException
   */
  @Test
  public void appendTableTest() throws IOException {
    String expectedTable = getTestTableWiki();
    TableDescriptor testTable = getTestTable(true);
    verifyAppendTable(testTable, expectedTable);
  }

  /**
   * Verifies that a test table is written as expected with {@link WikimediaWriter#appendTable(TableDescriptor)}
   * 
   * @param testTable
   *          test {@link TableDescriptor}
   * @param expectedTable
   *          Expected result in wikimedia format as {@link String}
   * @throws IOException
   */
  private void verifyAppendTable(TableDescriptor testTable, String expectedTable) throws IOException {
    StringWriter testWriter = new StringWriter();
    new WikimediaWriter(testWriter, null).appendTable(testTable);
    String actualTable = testWriter.toString();
    Assert.assertEquals(expectedTable, actualTable);
  }

  /**
   * Test for {@link WikimediaWriter#appendTableTransposed(TableDescriptor)}
   * 
   * @throws IOException
   */
  @Test
  public void appendTableTransposedTest() throws IOException {
    TableDescriptor testTable = getTestTable(true);
    String expectedTable = getTestTableWikiTransposed(true);

    verifyAppendTableTransposed(testTable, expectedTable);
  }

//  /**
//   * Test for {@link WikimediaWriter#appendTableTransposed(TableDescriptor)} without headers
//   *
//   * @throws IOException
//   */
//  @Test
//  public void appendTableTransposedTestNoHeaders() throws IOException {
//    TableDescriptor testTable = getTestTableNoHeaders();
//    String expectedTable = getTestTableWikiTransposed(false);
//    verifyAppendTableTransposed(testTable, expectedTable);
//  }

  /**
   * Verifies that a test table is written as expected with
   * {@link WikimediaWriter#appendTableTransposed(TableDescriptor)}
   * 
   * @param testTable
   *          test {@link TableDescriptor}
   * @param expectedTable
   *          Expected result in wikimedia format as {@link String}
   * @throws IOException
   */
  private void verifyAppendTableTransposed(TableDescriptor testTable, String expectedTable) throws IOException {
    StringWriter testWriter = new StringWriter();
    new WikimediaWriter(testWriter, null).appendTableTransposed(testTable);
    String actualTable = testWriter.toString();
    Assert.assertEquals(expectedTable, actualTable);
  }

  private TableDescriptor getTestTable(boolean headers) {
    String[] header = new String[]{"h1", "h2"};
    String[][] cells = new String[][]{{"a1", "a2"}, {"b1", "b2"}, {"c1", "c2"}};
    if (headers) {
      return new TableDescriptor(cells, header);
    }
    return new TableDescriptor(cells, null);
  }

  private TableDescriptor getTestTableNoHeaders() {
    String[][] cells = new String[][]{{"a1", "a2"}, {"b1", "b2"}, {"c1", "c2"}};
    return new TableDescriptor(cells, null);
  }

  private String getTestTableWiki() {
    return ""
        + "{|" + WikimediaWriter.NEWLINE
        + "!h1" + WikimediaWriter.NEWLINE
        + "!h2" + WikimediaWriter.NEWLINE
        + "|-" + WikimediaWriter.NEWLINE
        + "| a1" + WikimediaWriter.NEWLINE
        + "| a2" + WikimediaWriter.NEWLINE
        + "|-" + WikimediaWriter.NEWLINE
        + "| b1" + WikimediaWriter.NEWLINE
        + "| b2" + WikimediaWriter.NEWLINE
        + "|-" + WikimediaWriter.NEWLINE
        + "| c1" + WikimediaWriter.NEWLINE
        + "| c2" + WikimediaWriter.NEWLINE
        + "|}" + WikimediaWriter.NEWLINE;
  }

  private String getTestTableWikiTransposed(boolean headers) {
    if (headers) {
      return ""
          + "{|" + WikimediaWriter.NEWLINE
          + "|-" + WikimediaWriter.NEWLINE
          + "!h1" + WikimediaWriter.NEWLINE
          + "| a1" + WikimediaWriter.NEWLINE
          + "| b1" + WikimediaWriter.NEWLINE
          + "| c1" + WikimediaWriter.NEWLINE
          + "|-" + WikimediaWriter.NEWLINE
          + "!h2" + WikimediaWriter.NEWLINE
          + "| a2" + WikimediaWriter.NEWLINE
          + "| b2" + WikimediaWriter.NEWLINE
          + "| c2" + WikimediaWriter.NEWLINE
          + "|}" + WikimediaWriter.NEWLINE;
    }
    return ""
        + "{|" + WikimediaWriter.NEWLINE
        + "|-" + WikimediaWriter.NEWLINE
        + "| a1" + WikimediaWriter.NEWLINE
        + "| b1" + WikimediaWriter.NEWLINE
        + "| c1" + WikimediaWriter.NEWLINE
        + "|-" + WikimediaWriter.NEWLINE
        + "| a2" + WikimediaWriter.NEWLINE
        + "| b2" + WikimediaWriter.NEWLINE
        + "| c2" + WikimediaWriter.NEWLINE
        + "|}" + WikimediaWriter.NEWLINE;
  }

}
