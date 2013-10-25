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

import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.spec.client.property.IDocProperty;

/**
 *
 */
public class TitleFormProperty implements IDocProperty<IForm> {

  @Override
  public String getHeader() {
    return TEXTS.get("org.eclipse.scout.rt.spec.label");
  }

  @Override
  public String getText(IForm field) {
    return field.getTitle();
  }

}
