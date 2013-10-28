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
import java.util.Map;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.spec.client.out.FormDescriptor;
import org.eclipse.scout.rt.spec.client.out.TableFieldDescriptor;

/**
 *
 */
public class WikimediaFormWriter {
  private final Writer m_writer;
  private final FormDescriptor m_formDesc;
  private final Map<String, String> m_images;
  private final int imageScale = 500;

  /**
   * @param images
   */
  public WikimediaFormWriter(Writer writer, FormDescriptor form, Map<String, String> images) {
    m_writer = writer;
    m_formDesc = form;
    m_images = images;
  }

  public void write() throws ProcessingException {
    try {
      WikimediaWriter fieldWriter = new WikimediaWriter(m_writer);
      fieldWriter.appendHeading(m_formDesc.getTitle(), 2);
      fieldWriter.appendTableTransposed(m_formDesc.getFormProperties());
      String image = m_images.get(m_formDesc.getId());
      fieldWriter.appendImageLink(image, imageScale);
      fieldWriter.appendTable(m_formDesc.getFieldProperties());
      appendTableFields(fieldWriter);
      m_writer.flush();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new ProcessingException("", e);
    }
    finally {
      try {
        m_writer.close();
      }
      catch (IOException e) {
        //
      }
    }
  }

  /**
   * @param fieldWriter
   * @throws ProcessingException
   * @throws IOException
   */
  private void appendTableFields(WikimediaWriter fieldWriter) throws ProcessingException, IOException {
    List<TableFieldDescriptor> tableFields = m_formDesc.getTableFields();
    for (TableFieldDescriptor t : tableFields) {
      fieldWriter.appendHeading(t.getTitle(), 3);
      fieldWriter.appendWithHeading(t.getMenuProperties(), TEXTS.get("org.eclipse.scout.rt.spec.menus"), 4);
      fieldWriter.appendWithHeading(t.getColumnProperties(), TEXTS.get("org.eclipse.scout.rt.spec.columns"), 4);
    }
  }

}
