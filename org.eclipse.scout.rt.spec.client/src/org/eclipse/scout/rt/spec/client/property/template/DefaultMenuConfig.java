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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.annotations.Doc;
import org.eclipse.scout.rt.client.ui.action.IAction;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.spec.client.property.DocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocFilter;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;
import org.eclipse.scout.rt.spec.client.property.IgnoreDocFilter;
import org.eclipse.scout.rt.spec.client.property.SimpleTypeProperty;
import org.eclipse.scout.rt.spec.client.property.action.EmptySpaceActionProperty;
import org.eclipse.scout.rt.spec.client.property.action.MultiSelectionActionProperty;
import org.eclipse.scout.rt.spec.client.property.action.SingleSelectionActionProperty;
import org.eclipse.scout.rt.spec.client.property.action.TextActionProperty;

/**
 * The default configuration for {@link IMenu}
 */
public class DefaultMenuConfig implements IDocConfig<IMenu> {

  /**
   * Default properties for {@link IMenu} with
   * <p>
   * Label,Type,Description,EmptySpaceAction, SingleSelectionAction,MultiselectionAction
   * </p>
   */
  @Override
  public List<IDocProperty<IMenu>> getProperties() {
    List<IDocProperty<IMenu>> propertyTemplate = new ArrayList<IDocProperty<IMenu>>();
    propertyTemplate.add(new TextActionProperty<IMenu>(IAction.PROP_TEXT, TEXTS.get("org.eclipse.scout.rt.spec.label")));
    propertyTemplate.add(new SimpleTypeProperty<IMenu>());
    propertyTemplate.add(new DocProperty<IMenu>());
    propertyTemplate.add(new EmptySpaceActionProperty<IMenu>());
    propertyTemplate.add(new SingleSelectionActionProperty<IMenu>());
    propertyTemplate.add(new MultiSelectionActionProperty<IMenu>());
    return propertyTemplate;
  }

  /**
   * Default filters for {@link IMenu}: Ignores Types annotated with {@link Doc#ignore()}==false
   */
  @Override
  public List<IDocFilter<IMenu>> getFilters() {
    List<IDocFilter<IMenu>> filters = new ArrayList<IDocFilter<IMenu>>();
    filters.add(new IgnoreDocFilter<IMenu>());
    return filters;
  }

}
