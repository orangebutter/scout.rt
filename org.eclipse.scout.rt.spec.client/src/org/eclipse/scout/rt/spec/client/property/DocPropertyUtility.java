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
import org.eclipse.scout.rt.spec.client.out.TableDescriptor;

/**
 * Some utility methods for {@link IDocProperty}.
 */
public final class DocPropertyUtility {

  private DocPropertyUtility() {
  }

  /**
   * The headers of a list of {@link IDocProperty}
   * 
   * @param properties
   *          {@link IDocProperty} (may be <code>null</code>)
   * @return The headers of a list of {@link IDocProperty}.
   */
  public static String[] getHeaders(List<? extends IDocProperty> properties) {
    List<String> headers = new ArrayList<String>();
    if (properties != null) {
      for (IDocProperty p : properties) {
        headers.add(p.getHeader());
      }
    }
    return CollectionUtility.toArray(headers, String.class);
  }

  /**
   * Returns the texts for a scout model object for a list of {@link IDocProperty} properties.
   * 
   * @param object
   *          the scout model object
   * @param properties
   *          the list of {@link IDocProperty}
   * @return the texts for a scout model object for a list of {@link IDocProperty} properties.
   */
  public static <T> String[] getTexts(T object, List<IDocProperty<T>> properties) {
    if (properties != null) {
      String[] row = new String[properties.size()];
      for (int i = 0; i < properties.size(); i++) {
        IDocProperty<T> p = properties.get(i);
        row[i] = p.getText(object);
      }
      return row;
    }
    return new String[0];
  }

  /**
   * Creates a {@link TableDescriptor} for scout objects containing the properties described in {@link IDocProperty}.
   * Only scout objects that are accepted by all filters are included.
   * 
   * @param scoutObjects
   * @param properties
   *          {@link IDocProperty} list
   * @param filters
   *          {@link IDocFilter} list
   * @return {@link TableDescriptor}
   */
  public static <T> TableDescriptor createTableDesc(T[] scoutObjects, List<IDocProperty<T>> properties, List<IDocFilter<T>> filters) {
    final List<String[]> rows = new ArrayList<String[]>();
    for (T e : scoutObjects) {
      if (isAccepted(e, filters)) {
        String[] row = getTexts(e, properties);
        rows.add(row);
      }
    }
    String[][] rowArray = CollectionUtility.toArray(rows, String[].class);
    String[] headers = getHeaders(properties);
    return new TableDescriptor(rowArray, headers);
  }

  /**
   * Returns <code>true</code>, if the scoutObject is accepted by all filters.
   * 
   * @param scoutObject
   * @param filters
   *          {@link IDocFilter}s (may be <code>null</code>)
   * @return <code>true</code>, if the scoutObject is accepted by all filters.
   */
  public static <T> boolean isAccepted(T scoutObject, List<IDocFilter<T>> filters) {
    if (filters == null) {
      return true;
    }
    for (IDocFilter<T> filter : filters) {
      if (!filter.accept(scoutObject)) {
        return false;
      }
    }
    return true;
  }
}
