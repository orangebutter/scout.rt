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

import org.eclipse.scout.commons.annotations.Doc;

/**
 *
 */
public class IgnoreDocFilter<T> implements IDocFilter<T> {

  /**
   * @param column
   *          (may not be <code>null</code>)
   * @return true for displayable column
   */
  @Override
  public boolean accept(T c) {
    Doc docAnnotation = c.getClass().getAnnotation(Doc.class);
    if (docAnnotation != null) {
      return !docAnnotation.ignore();
    }
    return true;
  }

}
