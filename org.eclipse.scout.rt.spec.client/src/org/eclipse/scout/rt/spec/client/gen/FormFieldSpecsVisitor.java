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
package org.eclipse.scout.rt.spec.client.gen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.rt.client.ui.form.IFormFieldVisitor;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.spec.client.property.DocPropertyUtility;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

/**
 * Collects form field properties
 */
public class FormFieldSpecsVisitor implements IFormFieldVisitor {
  private final List<IDocProperty<IFormField>> m_properties;
  private final List<String[]> m_rows = new ArrayList<String[]>();

  public FormFieldSpecsVisitor(List<IDocProperty<IFormField>> properties) {
    m_properties = properties;
  }

  @Override
  public boolean visitField(IFormField field, int level, int fieldIndex) {
    String[] row = DocPropertyUtility.getPropertyRow(m_properties, field);
    m_rows.add(row);
    return true;
  }

  public String[][] getRows() {
    return CollectionUtility.toArray(m_rows, String[].class);
  }

}
