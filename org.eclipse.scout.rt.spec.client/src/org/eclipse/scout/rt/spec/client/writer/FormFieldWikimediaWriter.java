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
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.spec.client.out.TableDescriptor;

/**
 *
 */
public class FormFieldWikimediaWriter {
  private final Writer m_writer;

  private static final String NEWLINE = System.getProperty("line.separator");
  private static final String TABLE_START = "{|border=\"1\" " + NEWLINE;
  private static final String TABLE_END = "|}" + NEWLINE;
  private static final String TABLE_NEW_ROW = "|-" + System.getProperty("line.separator");
  private static final String TABLE_COLUMN_SEPARATOR = "| ";
  private static final String EMPTY_CELL_TEXT = "&nbsp;";

  private final TableDescriptor m_table;

  /**
   *
   */
  public FormFieldWikimediaWriter(Writer writer, TableDescriptor table) {
    m_writer = writer;
    m_table = table;
  }

  public void writeTable(IForm form) throws ProcessingException {
    try {
      m_writer.append(TABLE_START);
      writeHeaders();
      writeDataRows();
      m_writer.append(TABLE_END);
      m_writer.flush();
    }
    catch (IOException e) {
      throw new ProcessingException("Error writing form field table" + e);
    }
    finally {
      try {
        m_writer.close();
      }
      catch (IOException e) {
        // nop
      }
    }
  }

  private void writeDataRows() throws IOException {
    List<List<String>> cellTexts = m_table.getCellTexts();
    for (List<String> row : cellTexts) {
      m_writer.append(TABLE_NEW_ROW);
      for (String cell : row) {
        m_writer.append(TABLE_COLUMN_SEPARATOR);
        String cellText = StringUtility.isNullOrEmpty(cell) ? EMPTY_CELL_TEXT : cell;
        m_writer.append(cellText);
        m_writer.append(System.getProperty("line.separator"));
      }
    }
  }

  private void writeHeaders() throws IOException {
    for (String header : m_table.getHeaderTexts()) {
      m_writer.append("!");
      m_writer.append(header);
      m_writer.append(NEWLINE);
    }
  }

}
