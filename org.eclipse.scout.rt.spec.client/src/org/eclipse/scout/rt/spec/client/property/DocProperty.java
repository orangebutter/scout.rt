package org.eclipse.scout.rt.spec.client.property;

import org.eclipse.scout.rt.client.ui.IDocumentable;
import org.eclipse.scout.rt.shared.TEXTS;

/**
 * TODO replace doc properties
 */
public class DocProperty<T extends IDocumentable> extends AbstractNamedDocProperty<T> implements IDocProperty<T> {

  public DocProperty() {
    super(TEXTS.get("org.eclipse.scout.rt.spec.doc"));
  }

  /**
   * @param name
   */
  public DocProperty(String name) {
    super(name);
  }

  @Override
  public String getText(T d) {
    String doc = d.getDoc();
    if (doc != null) {
      doc = doc.replaceAll("</html>", "");
      doc = doc.replaceAll("<html>", "");
    }
    return doc;
  }

}
