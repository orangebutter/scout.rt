package org.eclipse.scout.rt.spec.client.property;

import org.eclipse.scout.rt.client.ui.IDocumentable;
import org.eclipse.scout.rt.shared.TEXTS;

/**
 * A {@link IDocProperty} for the documentation text defined by {@link IDocumentable#getDoc}.
 */
public class DocProperty<T extends IDocumentable> extends AbstractNamedDocProperty<T> implements IDocProperty<T> {

  public DocProperty() {
    super(TEXTS.get("org.eclipse.scout.rt.spec.doc"));
  }

  /**
   * The documentation text of a scout model object without enclosing html tags.
   */
  @Override
  public String getText(T object) {
    String doc = object.getDoc();
    if (doc != null) {
      doc = doc.replaceAll("</html>", "");
      doc = doc.replaceAll("<html>", "");
    }
    return doc;
  }

}
