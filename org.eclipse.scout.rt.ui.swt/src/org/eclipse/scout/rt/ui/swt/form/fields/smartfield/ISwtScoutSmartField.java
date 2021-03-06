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
package org.eclipse.scout.rt.ui.swt.form.fields.smartfield;

import org.eclipse.scout.rt.client.ui.form.fields.smartfield.ISmartField;
import org.eclipse.scout.rt.ui.swt.ext.DropDownButton;
import org.eclipse.scout.rt.ui.swt.form.fields.ISwtScoutFormField;

public interface ISwtScoutSmartField extends ISwtScoutFormField<ISmartField<?>> {

  DropDownButton getSwtBrowseButton();

}
