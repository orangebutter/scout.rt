package org.eclipse.scout.rt.spec.client.property.form.field;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.spec.client.property.AbstractBooleanDocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

public class BooleanFormFieldProperty extends AbstractBooleanDocProperty<IFormField> implements IDocProperty<IFormField> {
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
    Object property = field.getProperty(m_propertyName);
    boolean b = Boolean.parseBoolean(StringUtility.nvl(property, "false"));
    return getBooleanText(b);
  }

}
