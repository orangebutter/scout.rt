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

/**
 * An {@link IDocProperty} with the simple class name as text.
 */
public class SimpleTypeProperty<T> extends TypeProperty<T> {

  /**
   * @param object
   *          object to compare against the filter
   * @return <code>true</code> if the object is accepted by the filter.
   */
  @Override
  public String getText(T object) {
    return object.getClass().getSimpleName();
  }

}
