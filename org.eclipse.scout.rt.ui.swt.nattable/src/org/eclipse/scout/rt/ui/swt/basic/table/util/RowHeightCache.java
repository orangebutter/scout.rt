package org.eclipse.scout.rt.ui.swt.basic.table.util;

import java.util.HashMap;
import java.util.Map;

public class RowHeightCache {

  private Map<Integer/* row index */, Integer/* cachedHeight */> m_rowHeights;

  public RowHeightCache() {
    m_rowHeights = new HashMap<Integer, Integer>();
  }

  public boolean setRowHeight(int rowIndex, int contentHeight) {
    Integer rowHeight = m_rowHeights.get(rowIndex);
    System.out.println("setRowHeight to '" + contentHeight + "' with rowIndex '" + rowIndex + "' cache height '" + rowHeight + "'");
    if (rowHeight == null) {
      rowHeight = new Integer(contentHeight);
      m_rowHeights.put(rowIndex, rowHeight);
      return true;
    }
    if ((contentHeight > rowHeight)) {
      rowHeight = contentHeight;
      m_rowHeights.put(rowIndex, rowHeight);
      return true;
    }
    return false;
  }

  public void clearCache() {
    System.out.println("clear cache...");
    m_rowHeights.clear();
  }

}
