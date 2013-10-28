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

import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.ITableField;
import org.eclipse.scout.rt.spec.client.property.IDocFilter;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

/**
 * A template for describing the configuration of the generated documentation.
 * <p>
 * Describes for each scout model object (e.g. for {@link IForm}, {@link IFormField}, {@link IColumn}) which kind of
 * text is as documenation.
 * </p>
 * <p>
 * Describes what should be used as title for each generated scout model object.
 * </p>
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
   * Configuration for filtering {@link IColumn}. Only the columns accepted by all filters are generated.
   * 
   * @return a list of filters.
   */
  public List<IDocFilter<IColumn>> getColumnFilters();

  /**
   * Configuration for documenting {@link IMenu}.
   * 
   * @return a list of properties that should be generated.
   */
  public List<IDocProperty<IMenu>> getMenuProperties();

  /**
   * Configuration for the title of the {@link IForm} description.
   */
  public IDocProperty<IForm> getFormTitleProperty();

  /**
   * Configuration for the title of the {@link ITableField} description.
   */
  public IDocProperty<ITableField<?>> getTableTitleProperty();

  /**
   * The unique id that is used for {@link IForm}.
   */
  public IDocProperty<IForm> getFormIdProperty();

}
