package org.eclipse.scout.rt.spec.client.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder.Stylesheet;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;

/**
 * Converts a wikimedia file to html
 * 
 * @author jgu
 */
public class HtmlConverter {
  private final Stylesheet m_css;

  public HtmlConverter(File css) {
    m_css = new Stylesheet(css);
  }

  public void convertWikiToHtml(File in, File out) {
    try {
//      new File(out.getParent(),in.getn
//      String inputFileName = in.getName();
//      int i = inputFileName.lastIndexOf("\\.");
//      String substring = inputFileName.substring(0, i);

      BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out.getPath()), "UTF-8"));

      HtmlDocumentBuilder builder = new HtmlDocumentBuilder(htmlWriter);
      builder.addCssStylesheet(m_css);
      builder.setUseInlineStyles(false);
      builder.setEncoding("utf-8");

      MarkupParser parser = new MarkupParser(new MediaWikiLanguage());
      parser.setBuilder(builder);
      Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(in.getPath()), "utf-8"));
      parser.parse(reader);

      htmlWriter.flush();
      htmlWriter.close();
    }
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
