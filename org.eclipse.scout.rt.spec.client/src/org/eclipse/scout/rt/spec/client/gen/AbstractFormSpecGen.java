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
package org.eclipse.scout.rt.spec.client.gen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.spec.client.converter.DocBookConverter;
import org.eclipse.scout.rt.spec.client.converter.HtmlConverter;
import org.eclipse.scout.rt.spec.client.converter.TemplateUtility;
import org.eclipse.scout.rt.spec.client.out.TableDescriptor;
import org.eclipse.scout.rt.spec.client.property.template.SimpleSpecTemplate;
import org.eclipse.scout.rt.spec.client.screenshot.PrintFormListener;
import org.eclipse.scout.rt.spec.client.writer.FormFieldSpecGenerator;
import org.eclipse.scout.rt.spec.client.writer.FormFieldWikimediaWriter;
import org.osgi.framework.Bundle;

/**
 *
 */
public abstract class AbstractFormSpecGen {
  private final String ENCODING = "utf-8";

  public void printForm() throws ProcessingException {
    IForm form = createAndStartForm();
    File screensDir = getSpecDir();
    form.addFormListener(new PrintFormListener(screensDir));
    form.waitFor();
  }

  public void printAllFields() throws ProcessingException {
    try {
      // prepare form
      IForm form = createAndStartForm();

      //template
      SimpleSpecTemplate template = new SimpleSpecTemplate();

      // get the data
      FormFieldSpecGenerator g = new FormFieldSpecGenerator(template.getFieldProperties());
      TableDescriptor specData = g.getSpecData(form);

      // write
      File out = getSpecDir();
      out.mkdirs();

      File tableFile = new File(out, form.getClass().getName() + ".mediawiki");
      tableFile.createNewFile();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tableFile.getPath()), ENCODING));

      FormFieldWikimediaWriter fieldWriter = new FormFieldWikimediaWriter(writer, specData);
      fieldWriter.writeTable(form);

      // copy css
      File css = new File(getSpecDir(), "default.css");
      TemplateUtility.copyDefaultCss(css);

      // html
      File htmlFile = new File(out, form.getClass().getName() + ".html");
      HtmlConverter htmlConverter = new HtmlConverter(css);
      htmlConverter.convertWikiToHtml(tableFile, htmlFile);

      // docbook
      File docBook = new File(out, form.getClass().getName() + ".xml");
      DocBookConverter c = new DocBookConverter();
      c.convertWikiToDocBook(tableFile, docBook);
    }
    catch (IOException e) {
      throw new ProcessingException("Error printing form.", e);
    }
  }

  protected File getSpecDir() throws ProcessingException {
    try {
      Bundle bundle = Platform.getBundle(getPluginName());
      URL bundleRoot = bundle.getEntry("/");
      URI uri = FileLocator.resolve(bundleRoot).toURI();
      File targetFile = new File(uri);
      return new File(targetFile + File.separator + "target" + File.separator + "spec");
    }
    catch (IOException e) {
      throw new ProcessingException("Folder not found", e);
    }
    catch (URISyntaxException e) {
      throw new ProcessingException("Folder not found", e);
    }
  }

  protected abstract String getPluginName() throws ProcessingException;

  protected abstract IForm createAndStartForm() throws ProcessingException;
}
