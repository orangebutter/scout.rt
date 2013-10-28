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
package org.eclipse.scout.rt.spec.client.property.action;

import org.eclipse.scout.rt.client.ui.action.IAction;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.spec.client.property.AbstractBooleanDocProperty;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

/**
 *
 */
public class EmptySpaceActionProperty<T extends IAction> extends AbstractBooleanDocProperty<T> implements IDocProperty<T> {

  /**
   * @param name
   */
  public EmptySpaceActionProperty() {
    super(TEXTS.get("org.eclipse.scout.rt.spec.action.emptySpace"));
  }

  @Override
  public String getText(T object) {
    return getBooleanText(object.isEmptySpaceAction());
  }

}
