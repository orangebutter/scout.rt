package org.eclipse.scout.rt.spec.client.property.action;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.action.IAction;
import org.eclipse.scout.rt.spec.client.property.AbstractNamedDocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

/**
 *
 */
public class TextActionProperty<T extends IAction> extends AbstractNamedDocProperty<T> implements IDocProperty<T> {
  private final String m_propertyName;

  public TextActionProperty(String propertyName, String header) {
    super(header);
    m_propertyName = propertyName;
  }

  /**
   * Reads the property of the form field and returns its value
   */
  @Override
  public String getText(T field) {
    return StringUtility.nvl(field.getProperty(m_propertyName), "");
  }

}
