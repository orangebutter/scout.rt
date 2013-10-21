package org.eclipse.scout.rt.spec.client.property.form;

import org.eclipse.scout.rt.client.ui.form.fields.IFormField;

/**
 * Provides some basic type information without "Abstract" prefix.
 */
public class TypeFormFieldDocProperty extends AbstractDocProperty implements IFormFieldDocProperty {

  public TypeFormFieldDocProperty(String header) {
    super(header);
  }

  @Override
  public String getText(IFormField field) {
    String superTypeName = field.getClass().getSuperclass().getSimpleName();
    String basicName = superTypeName.replaceFirst("Abstract", "");
    return basicName;
  }
}
