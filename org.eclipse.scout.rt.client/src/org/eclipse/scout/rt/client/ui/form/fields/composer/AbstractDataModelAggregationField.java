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
package org.eclipse.scout.rt.client.ui.form.fields.composer;

import java.util.HashSet;

import org.eclipse.scout.commons.annotations.ConfigOperation;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.data.model.DataModelConstants;
import org.eclipse.scout.rt.shared.data.model.IDataModelAttribute;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

/**
 * Convenience field template to present {@link IDataModelAttribute#getAggregationTypes()}
 * <p>
 * Uses the lookup call {@link DataModelAggregationLookupCall}
 * <p>
 * Expects the property {@link #setAttribute(IDataModelAttribute)} to be set.
 */
public abstract class AbstractDataModelAggregationField extends AbstractSmartField<Integer> {

  public AbstractDataModelAggregationField() {
    this(true);
  }

  public AbstractDataModelAggregationField(boolean callInitializer) {
    super(callInitializer);
  }

  @Override
  protected String getConfiguredLabel() {
    return ScoutTexts.get("ComposerFieldAggregationLabel");
  }

  @Override
  protected Class<? extends LookupCall> getConfiguredLookupCall() {
    return DataModelAggregationLookupCall.class;
  }

  @Override
  protected boolean getConfiguredTreat0AsNull() {
    return false;
  }

  @Override
  protected void execAddSearchTerms(SearchFilter search) {
    //nop
  }

  public IDataModelAttribute getAttribute() {
    return ((DataModelAggregationLookupCall) getLookupCall()).getAttribute();
  }

  public void setAttribute(IDataModelAttribute attribute) throws ProcessingException {
    ((DataModelAggregationLookupCall) getLookupCall()).setAttribute(attribute);
    if (attribute != null) {
      setEnabled(true);
    }
    else {
      setEnabled(false);
    }
    execAttributeChanged(attribute);
    refreshDisplayText();
  }

  /**
   * Whenever the reference attribute changes, this method is called to customize the texts, values and new value of
   * this smart field.
   * <p>
   * This method is called after the lookap call was updated with the new attribute but before the value of this
   * smartfield is adapted.
   * <p>
   * The default sets the value to {@link DataModelConstants#AGGREGATION_NONE} if valid, else to
   * {@link DataModelConstants#AGGREGATION_COUNT} if valid and else to the first valid aggregation available or null.
   * 
   * @param attribute
   *          the new attribute
   */
  @ConfigOperation
  @Order(100)
  protected void execAttributeChanged(IDataModelAttribute attribute) throws ProcessingException {
    Integer newAg = null;
    if (attribute != null) {
      HashSet<Integer> agSet = new HashSet<Integer>();
      for (LookupRow row : ((DataModelAggregationLookupCall) getLookupCall()).getLookupRows()) {
        agSet.add((Integer) row.getKey());
      }
      if (agSet.contains(DataModelConstants.AGGREGATION_NONE)) {
        newAg = DataModelConstants.AGGREGATION_NONE;
      }
      else if (agSet.contains(DataModelConstants.AGGREGATION_COUNT)) {
        newAg = DataModelConstants.AGGREGATION_COUNT;
      }
      else if (agSet.size() > 0) {
        newAg = agSet.iterator().next();
      }
    }
    setValue(newAg);
  }

}
