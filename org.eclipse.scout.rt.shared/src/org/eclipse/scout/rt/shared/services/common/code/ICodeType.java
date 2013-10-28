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
package org.eclipse.scout.rt.shared.services.common.code;

/**
 * Code types are dynamic enumerations used in front- and back-end. <br>
 * A code type may (must not) contain codes divided into different partitions
 * (german: Mandanten) using the partitionId. <br>
 * If partitions are used, a context contains to a certain partition and
 * receives only his codes.
 */
public interface ICodeType<CODE_ID_TYPE, CODE extends ICode<CODE_ID_TYPE>> {

  /**
   * property into ISharedContextService's Map to get default partitionId of
   * current Subject / User
   */
  String PROP_PARTITION_ID = "partitionId";

  Long getId();

  boolean isHierarchy();

  int getMaxLevel();

  String getText();

  String getIconId();

  /**
   * all active top-level (root) codes
   */
  CODE[] getCodes();

  /**
   * all top-level (root) codes
   */
  CODE[] getCodes(boolean activeOnly);

  /**
   * find the code with this id
   */
  CODE getCode(CODE_ID_TYPE id);

  /**
   * find the code with this external reference
   */
  CODE getCodeByExtKey(Object extKey);

  /**
   * @return the index (starting at 0) of this code, -1 when not found <br>
   *         When the code type is a tree, the top-down-left-right traversal
   *         index is used
   */
  int getCodeIndex(final CODE_ID_TYPE id);

  /**
   * @return the index (starting at 0) of this code, -1 when not found <br>
   *         When the code type is a tree, the top-down-left-right traversal
   *         index is used
   */
  int getCodeIndex(final CODE c);
//
//  CODE[] getChildCodes(final CODE c);
//
//  CODE[] getChildCodes(final CODE c, boolean activeOnly);
//
//  CODE getParentCode(final CODE c);
//
//  CODE getParentCode(final CODE c, boolean activeOnly);

  /**
   * visits per default only the active codes
   * 
   * @param visitor
   * @return
   */
  boolean visit(ICodeVisitor<CODE_ID_TYPE, CODE> visitor);

  boolean visit(ICodeVisitor<CODE_ID_TYPE, CODE> visitor, boolean activeOnly);

}
