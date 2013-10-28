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

import org.eclipse.scout.rt.spec.client.property.IDocFilter;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

/**
 * A template for describing the configuration of the generated documentation for a specific type.
 */
public interface IDocConfig<T> {

  /**
   * Configuration for documenting type <code>T</code>.
   * 
   * @return a list of properties that should be generated.
   */
  public List<IDocProperty<T>> getProperties();

  /**
   * Configuration for filtering <code>T</code>. Only the objects accepted by all filters are generated.
   * 
   * @return a list of filters.
   */
  public List<IDocFilter<T>> getFilters();

}
