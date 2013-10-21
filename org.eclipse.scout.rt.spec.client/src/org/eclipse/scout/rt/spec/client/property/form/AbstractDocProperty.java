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
package org.eclipse.scout.rt.spec.client.property.form;

/**
 *
 */
public class AbstractDocProperty {
  private final String m_name;

  /**
   * @param name
   *          the (language specific) name of the property
   */
  public AbstractDocProperty(String name) {
    m_name = name;
  }

  public String getHeader() {
    return m_name;
  }

}
