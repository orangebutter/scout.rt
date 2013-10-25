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
package org.eclipse.scout.rt.spec.client.property.form.field;

import org.eclipse.scout.rt.client.ui.form.fields.tablefield.ITableField;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.spec.client.property.AbstractNamedDocProperty;

/**
 *
 */
public class TableFieldTypeAndLabelProperty extends AbstractNamedDocProperty<ITableField<?>> {

  public TableFieldTypeAndLabelProperty() {
    super(TEXTS.get("org.eclipse.scout.rt.spec.label"));
  }

  @Override
  public String getText(ITableField<?> field) {
    String label = field.getLabel() == null ? "" : " (" + field.getLabel() + ")";
    return field.getClass().getSimpleName() + label;
  }
}
