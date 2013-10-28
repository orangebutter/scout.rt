/*******************************************************************************
 * Copyright (c) 2013 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.rt.server.services.common.code.fixture;

import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;
import org.eclipse.scout.rt.shared.services.common.code.CodeRow;

public class TestCodeType2 extends AbstractCodeType<String, AbstractCode<String>, CodeRow<String>> {
  private static final long serialVersionUID = 1L;

  public static final Long ID = Long.valueOf(29);

  @Override
  public Long getId() {
    return ID;
  }
}
