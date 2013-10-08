package org.eclipse.scout.rt.ui.swt.basic.table;

import java.util.ArrayList;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.basic.table.TableEvent;
import org.eclipse.scout.rt.client.ui.basic.table.TableListener;
import org.eclipse.scout.rt.ui.swt.basic.SwtScoutComposite;
import org.eclipse.swt.widgets.Composite;

public class SwtScoutNatTable extends SwtScoutComposite<ITable> implements ISwtScoutNatTable {

  private ITable m_scoutObject;
  private P_ScoutTableListener m_scoutTableListener;

  @Override
  protected void initializeSwt(Composite parent) {

    IDataProvider bodyDataProvider = new TableDataProvider(getScoutObject());
    DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
    ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(bodyDataLayer);
    SelectionLayer selectionLayer = new SelectionLayer(columnHideShowLayer);
    ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

    //build the column header layer
    IDataProvider columnHeaderDataProvider = new ColumnHeaderDataProvider(getScoutObject());
//    DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
    DataLayer columnHeaderDataLayer = new ColumnHeaderDataLayer(columnHeaderDataProvider, getScoutObject());
    ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);

    //build the row header layer
    IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider) {
      @Override
      public int getColumnCount() {
        // TODO Auto-generated method stub
        return 0;
      }
    };
    DataLayer rowHeaderDataLayer = new ColumnHeaderDataLayer(rowHeaderDataProvider, getScoutObject());
    ILayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, viewportLayer, selectionLayer);

    //build the corner layer
    IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
    DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
    ILayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, columnHeaderLayer);
//    ILayer cornerLayer = new CornerLayer(cornerDataLayer, null, columnHeaderLayer);

    //build the grid layer
    GridLayer gridLayer = new GridLayer(viewportLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);
    NatTable natTable = new NatTable(parent, gridLayer);

    setSwtField(natTable);
    m_scoutObject = getScoutObject();
  }

  @Override
  protected void attachScout() {
    System.out.println("attachScout equals ?" + CompareUtility.equals(m_scoutObject, getScoutObject()));
    super.attachScout();
    if (m_scoutTableListener == null) {
      m_scoutTableListener = new P_ScoutTableListener();
      getScoutObject().addUITableListener(m_scoutTableListener);
    }
  }

  @Override
  protected void detachScout() {
    super.detachScout();
    if (getScoutObject() != null) {
      if (m_scoutTableListener != null) {
        getScoutObject().removeTableListener(m_scoutTableListener);
        m_scoutTableListener = null;
      }
    }
  }

  @Override
  public NatTable getSwtField() {
    return (NatTable) super.getSwtField();
  }

  /**
   * scout table observer
   */
  protected boolean isHandleScoutTableEvent(TableEvent[] a) {
    for (TableEvent element : a) {
      switch (element.getType()) {
        case TableEvent.TYPE_REQUEST_FOCUS:
        case TableEvent.TYPE_REQUEST_FOCUS_IN_CELL:
        case TableEvent.TYPE_ROWS_INSERTED:
        case TableEvent.TYPE_ROWS_UPDATED:
        case TableEvent.TYPE_ROWS_DELETED:
        case TableEvent.TYPE_ALL_ROWS_DELETED:
        case TableEvent.TYPE_ROW_ORDER_CHANGED:
        case TableEvent.TYPE_ROW_FILTER_CHANGED:
        case TableEvent.TYPE_COLUMN_ORDER_CHANGED:
        case TableEvent.TYPE_COLUMN_HEADERS_UPDATED:
        case TableEvent.TYPE_COLUMN_STRUCTURE_CHANGED:
        case TableEvent.TYPE_ROWS_SELECTED:
        case TableEvent.TYPE_SCROLL_TO_SELECTION: {
          return true;
        }
      }
    }
    return false;
  }

  protected void handleScoutTableEventInSwt(TableEvent e) {
    if (isDisposed()) {
      return;
    }
    SwtScoutTableEvent swtTableEvent = null;
    /*
     * check the scout observer to filter all events that are used here
     * @see isHandleScoutTableEvent()
     */
    switch (e.getType()) {
      case TableEvent.TYPE_REQUEST_FOCUS: {
        getSwtField().setFocus();
        break;
      }
//      case TableEvent.TYPE_REQUEST_FOCUS_IN_CELL: {
//        //start editing
//        int swtCol = -1;
//        TableColumn[] swtColumns = getSwtField().getColumns();
//        for (int c = 0; c < swtColumns.length; c++) {
//          if (swtColumns[c].getData(KEY_SCOUT_COLUMN) == e.getFirstColumn()) {
//            swtCol = c;
//            break;
//          }
//        }
//        ITableRow scoutRow = e.getFirstRow();
//        if (scoutRow != null && swtCol >= 0) {
//          getSwtTableViewer().editElement(scoutRow, swtCol);
//        }
//        break;
//      }
//      case TableEvent.TYPE_SCROLL_TO_SELECTION: {
//        scrollToSelection();
//        break;
//      }
      case TableEvent.TYPE_ROWS_INSERTED:
      case TableEvent.TYPE_ROWS_UPDATED:
      case TableEvent.TYPE_ROWS_DELETED:
      case TableEvent.TYPE_ALL_ROWS_DELETED:
      case TableEvent.TYPE_ROW_FILTER_CHANGED:
      case TableEvent.TYPE_ROW_ORDER_CHANGED: {
        System.out.println(getScoutObject().getRowCount());
        break;
      }
      case TableEvent.TYPE_COLUMN_ORDER_CHANGED:
      case TableEvent.TYPE_COLUMN_HEADERS_UPDATED:
      case TableEvent.TYPE_COLUMN_STRUCTURE_CHANGED:
      case TableEvent.TYPE_ROWS_SELECTED:
        break;
    }
  }

  private class P_ScoutTableListener implements TableListener {
    @Override
    public void tableChanged(final TableEvent e) {
      if (isHandleScoutTableEvent(new TableEvent[]{e})) {
        if (isIgnoredScoutEvent(TableEvent.class, "" + e.getType())) {
          return;
        }
        Runnable t = new Runnable() {
          @Override
          public void run() {
            try {
              getUpdateSwtFromScoutLock().acquire();
              //
              handleScoutTableEventInSwt(e);
            }
            finally {
              getUpdateSwtFromScoutLock().release();
            }
          }
        };
        getEnvironment().invokeSwtLater(t);
      }
    }

    @Override
    public void tableChangedBatch(final TableEvent[] a) {
      if (isHandleScoutTableEvent(a)) {
        final ArrayList<TableEvent> filteredList = new ArrayList<TableEvent>();
        for (int i = 0; i < a.length; i++) {
          if (!isIgnoredScoutEvent(TableEvent.class, "" + a[i].getType())) {
            filteredList.add(a[i]);
          }
        }
        if (filteredList.size() == 0) {
          return;
        }
        Runnable t = new Runnable() {
          @Override
          public void run() {
            if (isDisposed()) {
              return;
            }
            try {
              getUpdateSwtFromScoutLock().acquire();
              //
              for (TableEvent element : filteredList) {
                handleScoutTableEventInSwt(element);
              }
            }
            finally {
              getUpdateSwtFromScoutLock().release();
            }
          }
        };
        getEnvironment().invokeSwtLater(t);
      }
    }
  }// end P_ScoutTableListener

}
