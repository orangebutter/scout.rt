package org.eclipse.scout.rt.ui.swt.basic.table;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AggregateConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowIdAccessor;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.search.config.DefaultSearchBindings;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultMoveSelectionConfiguration;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultSelectionBindings;
import org.eclipse.nebula.widgets.nattable.tickupdate.config.DefaultTickUpdateConfiguration;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.TableEvent;
import org.eclipse.scout.rt.client.ui.basic.table.TableListener;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.ui.swt.basic.SwtScoutComposite;
import org.eclipse.scout.rt.ui.swt.basic.table.configuration.SelectionStyleConfiguration;
import org.eclipse.scout.rt.ui.swt.basic.table.configuration.TableStyleConfiguration;
import org.eclipse.swt.widgets.Composite;

public class SwtScoutNatTable extends SwtScoutComposite<ITable> implements ISwtScoutNatTable {

  private ITable m_scoutObject;
  private P_ScoutTableListener m_scoutTableListener;

  @Override
  protected void initializeSwt(Composite parent) {

    IRowDataProvider<ITableRow> bodyDataProvider = new TableDataProvider(getScoutObject());
    DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
    // column reorder layer
    ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);

    // column width

    IColumn[] scoutColumns = getScoutObject().getColumnSet().getVisibleColumns();
    if (getScoutObject().isAutoResizeColumns()) {
      int sumOfAllColumns = 0;
      for (int i = 0; i < scoutColumns.length; i++) {
        sumOfAllColumns += scoutColumns[i].getWidth();
      }
      int percentageSpace = 100;
      // percentage calculation
      for (int i = 0; i < scoutColumns.length - 1; i++) {
        int colWidth = (int) (100.0 / (double) sumOfAllColumns * scoutColumns[i].getWidth());
        bodyDataLayer.setColumnWidthByPosition(i, colWidth);
        percentageSpace -= colWidth;
      }
      // last
      bodyDataLayer.setColumnWidthByPosition(scoutColumns.length - 1, percentageSpace);
      bodyDataLayer.setColumnPercentageSizing(true);
    }
    else {
      int i = 0;
      for (IColumn<?> c : scoutColumns) {
        bodyDataLayer.setColumnWidthByPosition(i++, c.getWidth());
      }
    }

    // configuration
    BodyLayerConfiguration bodyConfiguration = new BodyLayerConfiguration(getScoutObject());
    bodyDataLayer.addConfiguration(bodyConfiguration);
    bodyDataLayer.setConfigLabelAccumulator(bodyConfiguration);

    ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
    SelectionLayer selectionLayer = new SelectionLayer(columnHideShowLayer, false);
    AggregateConfiguration selectionLayerConfiguration = new AggregateConfiguration();
    selectionLayerConfiguration.addConfiguration(new DefaultSelectionBindings());
    selectionLayerConfiguration.addConfiguration(new DefaultSearchBindings());
    selectionLayerConfiguration.addConfiguration(new DefaultTickUpdateConfiguration());
    selectionLayerConfiguration.addConfiguration(new DefaultMoveSelectionConfiguration());

    selectionLayer.addConfiguration(selectionLayerConfiguration);

    RowSelectionModel selectionModel = new RowSelectionModel<ITableRow>(selectionLayer, bodyDataProvider, new IRowIdAccessor<ITableRow>() {
      @Override
      public Serializable getRowId(ITableRow rowObject) {
        return rowObject.hashCode();
      }
    }, false);
    selectionLayer.setSelectionModel(selectionModel);
    selectionLayer.addConfiguration(new SelectionStyleConfiguration());

    ViewportLayer bodyLayer = new ViewportLayer(selectionLayer);

    //build the column header layer
    IDataProvider columnHeaderDataProvider = new ColumnHeaderDataProvider(getScoutObject());
    DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
    ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayer, selectionLayer, true);

    // build composite layer
    CompositeLayer compositeLayer = new CompositeLayer(1, 2);
    compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, columnHeaderLayer, 0, 0);
    compositeLayer.setChildLayer(GridRegion.BODY, bodyLayer, 0, 1);

    NatTable natTable = new NatTable(parent, compositeLayer, false);
    natTable.addConfiguration(new TableStyleConfiguration(getScoutObject(), getEnvironment()));
//    natTable.addConfiguration(new SelectionStyleConfiguration());
    natTable.configure();
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
