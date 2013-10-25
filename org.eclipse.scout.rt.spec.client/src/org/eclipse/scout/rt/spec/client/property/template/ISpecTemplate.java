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
package org.eclipse.scout.rt.spec.client.property.template;

import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.ITableField;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

/**
 *
 */
public interface ISpecTemplate {

  /**
   * Configuration for documenting {@link IForm}.
   * 
   * @return a list of properties that should be generated.
   */
  public List<IDocProperty<IForm>> getFormProperties();

  /**
   * Configuration for documenting {@link IFormField}.
   * 
   * @return a list of properties that should be generated.
   */
  public List<IDocProperty<IFormField>> getFieldProperties();

  /**
   * Configuration for documenting {@link IColumn}.
   * 
   * @return a list of properties that should be generated.
   */
  public List<IDocProperty<IColumn>> getColumnProperties();

  /**
   * @return
   */
  public IDocProperty<IForm> getFormTitleProperty();

  public IDocProperty<ITableField<?>> getTableTitleProperty();

  public IDocProperty<IForm> getFormIdProperty();

}
