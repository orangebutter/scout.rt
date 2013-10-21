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
package org.eclipse.scout.rt.spec.client.writer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.form.IFormFieldVisitor;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.spec.client.property.form.IFormFieldDocProperty;

/**
 * Collects form field properties
 */
public class FormFieldSpecsVisitor implements IFormFieldVisitor {
	private final List<IFormFieldDocProperty> m_properties;
	private final List<List<String>> m_rows = new ArrayList<List<String>>();

	public FormFieldSpecsVisitor(List<IFormFieldDocProperty> properties) {
		m_properties = properties;
	}

	@Override
	public boolean visitField(IFormField field, int level, int fieldIndex) {
		List<String> row = new ArrayList<String>();
		for (IFormFieldDocProperty p : m_properties) {
			row.add(p.getText(field));
		}
		m_rows.add(row);
		return true;
	}

	public List<List<String>> getRows() {
		return m_rows;
	}

}
