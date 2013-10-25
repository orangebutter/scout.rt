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
package org.eclipse.scout.rt.spec.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.spec.client.converter.DocBookConverter;
import org.eclipse.scout.rt.spec.client.converter.HtmlConverter;
import org.eclipse.scout.rt.spec.client.converter.TemplateUtility;
import org.eclipse.scout.rt.spec.client.gen.FormSpecGenerator;
import org.eclipse.scout.rt.spec.client.gen.SpecImageFilter;
import org.eclipse.scout.rt.spec.client.out.FormDescriptor;
import org.eclipse.scout.rt.spec.client.property.template.DefaultSpecTemplate;
import org.eclipse.scout.rt.spec.client.screenshot.PrintFormListener;
import org.eclipse.scout.rt.spec.client.writer.WikimediaFormWriter;
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
    // prepare form
    IForm form = createAndStartForm();

    //template
    DefaultSpecTemplate template = new DefaultSpecTemplate();

    // get the data
    String formId = form.getClass().getName();
    FormSpecGenerator g = new FormSpecGenerator(template);
    FormDescriptor specData = g.getSpecData(form);

    // write
    File out = getSpecDir();
    out.mkdirs();
    File wiki = createMediaWikiFile(out, formId);
    Writer mediaWikiWriter = createWriter(wiki);
    Map<String, String> images = getImages(out);
    WikimediaFormWriter wikimediaFormWriter = new WikimediaFormWriter(mediaWikiWriter, specData, images);
    wikimediaFormWriter.write();

    convertToHTML(out, formId, wiki);
  }

  private Map<String, String> getImages(File dir) {
    String[] files = dir.list(new SpecImageFilter());
    HashMap<String, String> map = new HashMap<String, String>();
    for (String file : files) {
      String id = file.replace(".jpg", "");
      map.put(id, file);
    }
    return map;
  }

  protected File convertToDocBook(File out, String id, File mediaWiki) {
    File docBook = new File(out, id + ".xml");
    DocBookConverter c = new DocBookConverter();
    c.convertWikiToDocBook(mediaWiki, docBook);
    return docBook;
  }

  private File convertToHTML(File out, String id, File mediaWiki) throws ProcessingException {
    try {
      // copy css
      File css = new File(getSpecDir(), "default.css");
      TemplateUtility.copyDefaultCss(css);

      File htmlFile = new File(out, id + ".html");
      HtmlConverter htmlConverter = new HtmlConverter(css);
      htmlConverter.convertWikiToHtml(mediaWiki, htmlFile);
      return htmlFile;
    }
    catch (IOException e) {
      throw new ProcessingException("Error writing mediawiki file.", e);
    }
  }

  private File createMediaWikiFile(File out, String id) throws ProcessingException {
    File tableFile = new File(out, id + ".mediawiki");
    try {
      if (tableFile.exists()) {
        tableFile.delete();
      }
      tableFile.createNewFile();
      return tableFile;
    }
    catch (IOException e) {
      throw new ProcessingException("Error writing mediawiki file.", e);
    }
  }

  private BufferedWriter createWriter(File mediaWiki) throws ProcessingException {
    try {
      FileOutputStream outputStream = new FileOutputStream(mediaWiki.getPath(), true);
      return new BufferedWriter(new OutputStreamWriter(outputStream, ENCODING));
    }
    catch (IOException e) {
      throw new ProcessingException("Error writing mediawiki file.", e);
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
