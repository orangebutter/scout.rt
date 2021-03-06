/*******************************************************************************
 * Copyright (c) 2011 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.scout.rt.ui.rap.extension;

import org.eclipse.scout.rt.shared.data.basic.FontSpec;

public interface IUiDecoration {

  int getDialogMinWidth();

  int getDialogMinHeight();

  int getProcessButtonHeight();

  int getProcessButtonMinWidth();

  int getProcessButtonMaxWidth();

  boolean isFormMainBoxBorderVisible();

  int getFormFieldLabelWidth();

  int getFormFieldLabelAlignment();

  boolean isFormFieldSelectAllOnFocusEnabled();

  String getMandatoryFieldBackgroundColor();

  int getMandatoryStarMarkerPosition();

  String getMandatoryLabelTextColor();

  FontSpec getMandatoryLabelFont();

  int getLogicalGridLayoutDefaultColumnWidth();

  int getLogicalGridLayoutDefaultPopupWidth();

  int getLogicalGridLayoutHorizontalGap();

  int getLogicalGridLayoutVerticalGap();

  int getLogicalGridLayoutRowHeight();

  String getColorForegroundDisabled();

  int getTableRowHeight();

  int getTreeNodeHeight();

  int getMessageBoxMinHeight();

  int getMessageBoxMinWidth();

  /**
   * @return the minimum width of the right area in the desktop when using standalone rwt environment
   *         default is 330px
   */
  int getToolFormMinWidth();

  boolean isDndSupportEnabled();

  boolean isBrowserHistoryEnabled();
}
