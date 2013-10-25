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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.CollectionUtility;

/**
 *
 */
public final class DocPropertyUtility {

  private DocPropertyUtility() {
  }

  public static String[] getHeaders(List<? extends IDocProperty> properties) {
    List<String> headers = new ArrayList<String>();
    for (IDocProperty p : properties) {
      headers.add(p.getHeader());
    }
    return CollectionUtility.toArray(headers, String.class);
  }

  public static <T> String[] getPropertyRow(List<IDocProperty<T>> properties, T form) {
    String[] row = new String[properties.size()];
    for (int i = 0; i < properties.size(); i++) {
      IDocProperty<T> p = properties.get(i);
      row[i] = p.getText(form);
    }
    return row;
  }
}
