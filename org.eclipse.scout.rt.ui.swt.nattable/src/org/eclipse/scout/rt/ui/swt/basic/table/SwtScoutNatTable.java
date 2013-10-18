package org.eclipse.scout.rt.ui.swt.basic.table;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.config.AggregateConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowIdAccessor;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.IUniqueIndexLayer;
import org.eclipse.nebula.widgets.nattable.painter.layer.CellLayerPainter;
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
import org.eclipse.scout.rt.ui.swt.basic.table.command.ComputeRowHeightCommandHander;
import org.eclipse.scout.rt.ui.swt.basic.table.command.ViewportComputeRowHeightCommandHandler;
import org.eclipse.scout.rt.ui.swt.basic.table.configuration.BodyLayerConfiguration;
import org.eclipse.scout.rt.ui.swt.basic.table.configuration.EsColumnHeaderLayerConfiguration;
import org.eclipse.scout.rt.ui.swt.basic.table.configuration.SelectionStyleConfiguration;
import org.eclipse.scout.rt.ui.swt.basic.table.configuration.TableStyleConfiguration;
import org.eclipse.scout.rt.ui.swt.basic.table.dataprovider.ColumnHeaderDataProvider;
import org.eclipse.scout.rt.ui.swt.basic.table.dataprovider.TableDataProvider;
import org.eclipse.scout.rt.ui.swt.basic.table.painter.EsNatLayerPainter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class SwtScoutNatTable extends SwtScoutComposite<ITable> implements ISwtScoutNatTable {

  private ITable m_scoutObject;
  private P_ScoutTableListener m_scoutTableListener;
  private SelectionLayer m_selectionLayer;

  @Override
  protected void initializeSwt(Composite parent) {

    IRowDataProvider<ITableRow> bodyDataProvider = new TableDataProvider(getScoutObject());
    ILayer bodyLayer = createBodyLayer(bodyDataProvider, getScoutObject());

    //build the column header layer
    IDataProvider columnHeaderDataProvider = new ColumnHeaderDataProvider(getScoutObject());
//    DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
    ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(new DataLayer(columnHeaderDataProvider), bodyLayer, m_selectionLayer, false, new CellLayerPainter());
    columnHeaderLayer.addConfiguration(new EsColumnHeaderLayerConfiguration());

    // build composite layer
    CompositeLayer compositeLayer = new CompositeLayer(1, 2);
    compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, columnHeaderLayer, 0, 0);
    compositeLayer.setChildLayer(GridRegion.BODY, bodyLayer, 0, 1);

//    EsCompositeLayer compositeLayer = new EsCompositeLayer(1, 1);
//    compositeLayer.setChildLayer(GridRegion.BODY, bodyLayer, 0, 0);

    NatTable natTable = new NatTable(parent, NatTable.DEFAULT_STYLE_OPTIONS | SWT.BORDER, compositeLayer, false) {
      @Override
      public void redraw(int x, int y, int width, int height, boolean all) {
        super.redraw(x, y, width, height, all);
      }
    };
    natTable.setLayerPainter(new EsNatLayerPainter(natTable));
    natTable.addConfiguration(new TableStyleConfiguration(natTable, getScoutObject(), getEnvironment()));
//    natTable.addConfiguration(new SelectionStyleConfiguration());
    natTable.configure();
    setSwtField(natTable);
    m_scoutObject = getScoutObject();
  }

  public IUniqueIndexLayer createBodyLayer(IRowDataProvider<ITableRow> dataProvider, ITable scoutTable) {
    IUniqueIndexLayer bodyLayer = createDataLayer(dataProvider, scoutTable);
    bodyLayer = createColumnReorderLayer(bodyLayer, scoutTable);
    bodyLayer = createHideShowLayer(bodyLayer, scoutTable);
    bodyLayer = createSelectionLayer(bodyLayer, dataProvider, scoutTable);
    bodyLayer = createViewPortLayer(bodyLayer, scoutTable);
    return bodyLayer;
  }

  /**
   * @param dataProvider
   * @return
   */
  private IUniqueIndexLayer createDataLayer(IDataProvider dataProvider, ITable scoutTable) {
    DataLayer dataLayer = new DataLayer(dataProvider) {
      @Override
      public boolean doCommand(ILayerCommand command) {
        return super.doCommand(command);
      }
    };
    dataLayer.registerCommandHandler(new ComputeRowHeightCommandHander());
    // column width

    IColumn[] scoutColumns = scoutTable.getColumnSet().getVisibleColumns();
    if (scoutTable.isAutoResizeColumns()) {
      int sumOfAllColumns = 0;
      for (int i = 0; i < scoutColumns.length; i++) {
        sumOfAllColumns += scoutColumns[i].getWidth();
      }
      int percentageSpace = 100;
      // percentage calculation
      for (int i = 0; i < scoutColumns.length - 1; i++) {
        int colWidth = (int) (100.0 / (double) sumOfAllColumns * scoutColumns[i].getWidth());
        dataLayer.setColumnWidthByPosition(i, colWidth);
        percentageSpace -= colWidth;
      }
      // last
      dataLayer.setColumnWidthByPosition(scoutColumns.length - 1, percentageSpace);
      dataLayer.setColumnPercentageSizing(true);

    }
    else {
      int i = 0;
      for (IColumn<?> c : scoutColumns) {
        dataLayer.setColumnWidthByPosition(i++, c.getWidth());
      }
    }

    // configuration
    BodyLayerConfiguration bodyConfiguration = new BodyLayerConfiguration(getScoutObject(), getEnvironment());
    dataLayer.addConfiguration(bodyConfiguration);
    dataLayer.setConfigLabelAccumulator(bodyConfiguration);

    return dataLayer;
  }

  /**
   * @param bodyLayer
   * @param scoutTable
   * @return
   */
  private IUniqueIndexLayer createColumnReorderLayer(IUniqueIndexLayer bodyLayer, ITable scoutTable) {
    ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyLayer);
    return columnReorderLayer;
  }

  /**
   * @param bodyLayer
   * @param scoutTable
   * @return
   */
  private IUniqueIndexLayer createHideShowLayer(IUniqueIndexLayer bodyLayer, ITable scoutTable) {
//    ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(bodyLayer);
//    return columnHideShowLayer
    return bodyLayer;
  }

  /**
   * @param bodyLayer
   * @param scoutTable
   * @return
   */
  private IUniqueIndexLayer createSelectionLayer(IUniqueIndexLayer bodyLayer, IRowDataProvider<ITableRow> rowDataProvider, ITable scoutTable) {
    SelectionLayer selectionLayer = new SelectionLayer(bodyLayer, false);
    AggregateConfiguration selectionLayerConfiguration = new AggregateConfiguration();
    selectionLayerConfiguration.addConfiguration(new DefaultSelectionBindings());
    selectionLayerConfiguration.addConfiguration(new DefaultSearchBindings());
    selectionLayerConfiguration.addConfiguration(new DefaultTickUpdateConfiguration());
    selectionLayerConfiguration.addConfiguration(new DefaultMoveSelectionConfiguration());

    selectionLayer.addConfiguration(selectionLayerConfiguration);

    RowSelectionModel selectionModel = new RowSelectionModel<ITableRow>(selectionLayer, rowDataProvider, new IRowIdAccessor<ITableRow>() {
      @Override
      public Serializable getRowId(ITableRow rowObject) {
        return rowObject.hashCode();
      }
    }, false);
    selectionLayer.setSelectionModel(selectionModel);
    selectionLayer.addConfiguration(new SelectionStyleConfiguration());
    m_selectionLayer = selectionLayer;
    return selectionLayer;
  }

  /**
   * @param bodyLayer
   * @param scoutTable
   * @return
   */
  private IUniqueIndexLayer createViewPortLayer(IUniqueIndexLayer bodyLayer, ITable scoutTable) {
    ViewportLayer viewPortLayer = new ViewportLayer(bodyLayer);
    viewPortLayer.registerCommandHandler(new ViewportComputeRowHeightCommandHandler());
    return viewPortLayer;
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
