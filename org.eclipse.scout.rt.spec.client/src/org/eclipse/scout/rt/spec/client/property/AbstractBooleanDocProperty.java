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
 *
 */
public abstract class AbstractBooleanDocProperty<T> extends AbstractNamedDocProperty<T> {

  public static final String DOC_ID_TRUE = "org.eclipse.scout.rt.spec.true";
  public static final String DOC_ID_FALSE = "org.eclipse.scout.rt.spec.false";

  /**
   * @param name
   */
  public AbstractBooleanDocProperty(String name) {
    super(name);
  }

  protected String getBooleanText(boolean b) {
    return b ? TEXTS.get(DOC_ID_TRUE) : TEXTS.get(DOC_ID_FALSE);
  }

}
