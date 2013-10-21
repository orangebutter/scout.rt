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

import org.eclipse.scout.rt.client.ui.form.fields.IFormField;

/**
 * Provides header description and text for a given form field.
 */
public interface IFormFieldDocProperty {

	/**
	 * @return header description for a this property
	 */
	public String getHeader();

	/**
	 * @param field
	 * @return value of the property
	 */
	public String getText(IFormField field);

}
