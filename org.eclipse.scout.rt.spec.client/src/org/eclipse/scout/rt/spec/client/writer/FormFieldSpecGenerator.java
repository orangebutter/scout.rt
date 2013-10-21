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

import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.spec.client.out.TableDescriptor;
import org.eclipse.scout.rt.spec.client.property.form.IFormFieldDocProperty;

/**
 * Creates Specification data from Form fields
 */
public class FormFieldSpecGenerator {
	private final List<IFormFieldDocProperty> m_properties;
	private final FormFieldSpecsVisitor m_visitor;

	public FormFieldSpecGenerator(List<IFormFieldDocProperty> properties) {
		m_properties = properties;
		m_visitor = new FormFieldSpecsVisitor(m_properties);
	}

	public TableDescriptor getSpecData(IForm form) {
		form.visitFields(m_visitor);
		List<List<String>> rows = m_visitor.getRows();
		List<String> headers = getHeaders();
		return new TableDescriptor(rows, headers);
	}

	private List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		for (IFormFieldDocProperty p : m_properties) {
			headers.add(p.getHeader());
		}
		return headers;
	}

}
