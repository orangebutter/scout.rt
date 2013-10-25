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
import java.io.Writer;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.spec.client.out.TableDescriptor;

/**
 *
 */
public class WikimediaWriter {
  private final Writer m_writer;

  private static final String NEWLINE = System.getProperty("line.separator");
  private static final String TABLE_START = "{|border=\"1\" " + NEWLINE;
  private static final String TABLE_END = "|}" + NEWLINE;
  private static final String TABLE_NEW_ROW = "|-" + System.getProperty("line.separator");
  private static final String TABLE_COLUMN_SEPARATOR = "| ";
  private static final String EMPTY_CELL_TEXT = "&nbsp;";
  private final char HEADING_CHAR = '=';

  /**
   *
   */
  public WikimediaWriter(Writer writer) {
    m_writer = writer;
  }

  public void appendTable(TableDescriptor table) throws IOException {
    m_writer.append(TABLE_START);
    appendTableHeaders(table.getHeaderTexts());
    appendDataRows(table.getCellTexts());
    m_writer.append(TABLE_END);
    m_writer.flush();
  }

  private void appendDataRows(List<List<String>> cellTexts) throws IOException {
    for (List<String> row : cellTexts) {
      m_writer.append(TABLE_NEW_ROW);
      for (String cell : row) {
        m_writer.append(TABLE_COLUMN_SEPARATOR);
        String cellText = getEscapedText(cell);
        m_writer.append(cellText);
        m_writer.append(System.getProperty("line.separator"));
      }
    }
  }

  private String getEscapedText(String text) {
    String nonEmptyText = StringUtility.isNullOrEmpty(text) ? EMPTY_CELL_TEXT : text;
    String noLinks = nonEmptyText.replace("[", "<nowiki>[</nowiki>").replace("]", "<nowiki>]</nowiki>");
    return noLinks;
  }

  private void appendTableHeaders(List<String> headerTexts) throws IOException {
    for (String header : headerTexts) {
      m_writer.append("!");
      m_writer.append(header);
      m_writer.append(NEWLINE);
    }
  }

  public void appendHeading(String name, int level) throws ProcessingException, IOException {
    String prefix = multiply(level, HEADING_CHAR);
    m_writer.append(prefix);
    m_writer.append(" ");
    m_writer.append(name);
    m_writer.append(" ");
    m_writer.append(prefix);
    m_writer.append(NEWLINE);
  }

  private String multiply(int level, char c) {
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < level; i++) {
      b.append(HEADING_CHAR);
    }
    return b.toString();
  }

  /**
   * @param imageName
   * @param scale
   *          in pixel
   * @throws IOException
   */
  public void appendImageLink(String imageName, int scale) throws IOException {
    m_writer.append("[[Image:" + imageName + "|" + scale + "px]]");
    m_writer.append(NEWLINE);
  }
}
