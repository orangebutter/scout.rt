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
import org.eclipse.mylyn.wikitext.core.parser.builder.DocBookDocumentBuilder;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;

public class DocBookConverter {
  private final String ENCODING = "utf-8";

  public void convertWikiToDocBook(File in, File out) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out.getPath()), ENCODING));
      DocBookDocumentBuilder builder = new DocBookDocumentBuilder(writer);

      MarkupParser parser = new MarkupParser(new MediaWikiLanguage());
      parser.setBuilder(builder);
      Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(in.getPath()), ENCODING));
      parser.parse(reader);
      writer.flush();
      writer.close();
    }
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally {
      if (writer != null) {
        try {
          writer.close();
        }
        catch (IOException e) {
          // nop
        }
      }
    }
  }

}
