/*******************************************************************************
 * Copyright (c) 2011 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.rt.ui.swt.basic.calendar;

/**
 * A couple of constants regarding the calendar
 * 
 * @author Michael Rudolf, Andreas Hoegger
 */
public final class CalendarConstants {

  /** how many pixels for the cell headers when drawing the timeline */
  public static final int OFFSET_CELL_HEADER_Y = 13;

  /** starting time for day appointment timeline */
  public static final int DAY_TIMELINE_START_TIME = 6;
  /** ending time for day appointment timeline */
  public static final int DAY_TIMELINE_END_TIME = 20;

  /** timeline width for week and day view */
  public static final int TIMELINE_WIDTH = 50;

  /** nb of ms in an hour */
  public static final long HOUR_MILLIS = 3600 * 1000;

  /** type of calendar item is within a monthly view */
  public static final int ITEM_TYPE_MONTH = 1;
  /** type of calendar item is within a weekly or daily view */
  public static final int ITEM_TYPE_WEEK = 2;

  /** size below which a month cell will show a condensed date label */
  public static final int SWITCH_ITEM_WIDTH = 60;
  public static final int SWITCH_ITEM_HEIGHT = 40;

  private CalendarConstants() {
  }
}
