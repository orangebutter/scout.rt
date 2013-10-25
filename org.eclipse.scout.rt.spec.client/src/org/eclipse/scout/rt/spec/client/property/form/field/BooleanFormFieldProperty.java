package org.eclipse.scout.rt.spec.client.property.form.field;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.spec.client.property.AbstractNamedDocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

public class BooleanFormFieldProperty extends AbstractNamedDocProperty<IFormField> implements IDocProperty<IFormField> {
  public static final String DOC_ID_TRUE = "org.eclipse.scout.rt.spec.true";
  public static final String DOC_ID_FALSE = "org.eclipse.scout.rt.spec.false";

  private final String m_propertyName;

  public BooleanFormFieldProperty(String propertyName, String header) {
    super(header);
    m_propertyName = propertyName;

  }

  /**
   * Reads the property of the form field and returns the translated doc text for {@value #DOC_ID_TRUE}, if the property
   * is <code>true</code>. {@value #DOC_ID_FALSE} otherwise.
   */
  @Override
  public String getText(IFormField field) {
    boolean b = Boolean.parseBoolean(StringUtility.nvl(field.getProperty(m_propertyName), "false"));
    return b ? TEXTS.get(DOC_ID_TRUE) : TEXTS.get(DOC_ID_FALSE);
  }

}
