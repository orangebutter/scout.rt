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
package org.eclipse.scout.rt.spec.client.property;

import org.eclipse.scout.rt.shared.TEXTS;

/**
 * An {@link IDocProperty} with the fully qualified class name as text.
 */
public class TypeProperty<T> extends AbstractNamedDocProperty<T> implements IDocProperty<T> {

  public TypeProperty() {
    super(TEXTS.get("org.eclipse.scout.rt.spec.type"));
  }

  /**
   * @return fully qualified class name.
   */
  @Override
  public String getText(T object) {
    return object.getClass().getName();
  }

}