package org.eclipse.scout.rt.spec.example.ui.swing.form;

import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class LanguageCodeType extends AbstractCodeType<Long> {
  private static final long serialVersionUID = 1L;
  public static final long ID = 1;

  @Override
  public Long getId() {
    return ID;
  }

  @Override
  protected String getConfiguredText() {
    return "Language";
  }

}
