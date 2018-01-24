package dibugger.userinterface;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import dibugger.debuglogic.debugger.DebugLogicFacade;
import dibugger.debuglogic.exceptions.DIbuggerLogicException;
import dibugger.debuglogic.interpreter.ScopeTuple;
import dibugger.userinterface.dibuggerpopups.ErrorPopUp;
import dibugger.userinterface.dibuggerpopups.ExpressionChangePopUp;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * class that represents a WatchExpressionPanel where a user can see, edit and delete his watch-expressions
 */
public class WatchExpressionPanel extends ExpressionPanel {

    private MainInterface mainInterface;
    private Object[][] dataEntries;
    private HashMap<Integer, Integer> idMap = new HashMap<>();
    private int currentHighestId = 0;
    private WatchExpressionPanel thisWEP = this;
    private HashMap<Integer, ArrayList<ScopeTuple>> scopes = new HashMap<>();
    private JTable table;
    private DefaultTableModel tableModel;

    private static WatchExpressionPanel singleton = null;

    private WatchExpressionPanel(MainInterface mainInterface) {
        this.mainInterface = mainInterface;
        initComponents();
        this.setVisible(true);
    }

  /**
   * method that implements the singleton pattern of this class
   * @param mainInterface MainInterface on which this panel should be displayed
   * @return a WatchExpressionPanel (a new one if none existed, the existing one if there exists one)
   */
    public static WatchExpressionPanel getWatchExpressionPanel(MainInterface mainInterface) {
        if (singleton == null) {
            singleton = new WatchExpressionPanel(mainInterface);
        }
        return singleton;
    }

  /**
   * method that implements the observer pattern
   * is called by the model part of the mvc pattern
   * updates the important values that are calculated by the model part
   */
  public void update() {
        DebugLogicFacade debugLogicFacade = mainInterface.getControlFacade().getDebugLogicFacade();
        for (int i = 0; i <= currentHighestId; i++) {
            try {
				dataEntries[i][2] = debugLogicFacade.getWEValue(i);
			} catch (DIbuggerLogicException e) {
				// TODO do something or not
			}
            table.updateUI();
        }
        // TODO: check
    }

    private void initComponents() {

        idMap.put(0, 0);

        panelType = "Watch Expressions:";

        String[] columnTitles = { "Opt", panelType, "=" };
        dataEntries = new Object[1][3];
        dataEntries[0][0] = " ";
        dataEntries[0][1] = "5 = 5";
        dataEntries[0][2] = " ";
        mainInterface.getControlFacade().createWatchExpression(0, "5 = 5");
        tableModel = new DefaultTableModel(dataEntries, columnTitles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(10);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Point p = mouseEvent.getPoint();
                if ((table.columnAtPoint(p) == 0)) {
                    int row = table.rowAtPoint(p);
                    int id = idMap.get(row);
                    new ExpressionChangePopUp(mainInterface, "WatchExpression", row, table, thisWEP, id);
                }
                if (table.rowAtPoint(p) == table.getRowCount() - 1 & table.columnAtPoint(p) == 1) {
                    addRow(p);
                    getWatchExpressionPanel(mainInterface).updateUI();
                }
                saveWEs();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });
        table.setSize(50, 50);

        JScrollPane tableContainer = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableContainer.setViewportView(table);
        tableContainer.createVerticalScrollBar();
        tableContainer.setPreferredSize(new Dimension(290, 250));

        this.add(tableContainer);

    }

  /**
   * method to save the scopes of a watch-expression
   * @param id ID of the watch-expression
   * @param scopeTupels scopeTupel of the type (start, end) of the watch-expression
   */
    public void saveScopes(int id, ArrayList<ScopeTuple> scopeTupels) {
        scopes.put(id, scopeTupels);
        //TODO: weitergeben, evtl. schon bei MouseExcited
    }

  /**
   * method to delete an entry in the WatchExpressionPanel, deletes the watch-expression that is displayed in this row
   * @param rowToDelete row as int (from 0 to n) in which the watch-expression is to be deleted
   */
  public void deleteEntry(int rowToDelete) {
      ArrayList<Object[]> dataEntriesAsList = new ArrayList<>(Arrays.asList(dataEntries));
        if(dataEntriesAsList.size() > 1) {
          tableModel.removeRow(rowToDelete);
          dataEntriesAsList.remove(rowToDelete);
          for (int row: idMap.keySet()) {
            if (row > rowToDelete) {
              idMap.put(row - 1, idMap.get(row));
            }
          }
        }
        dataEntriesAsList.toArray(dataEntries);
    }

  /**
   * method to reset the WatchExpressionPanel
   */
  public void reset() {
        singleton = new WatchExpressionPanel(mainInterface);
    }

  /**
   * method to save all the watch-expressions that are displayed in this WatchExpressionPanel
   */
  public void saveWEs() {
      for (int j = 0; j < table.getRowCount(); j++) {
        mainInterface.getControlFacade().changeWatchExpression(idMap.get(j),
            table.getModel().getValueAt(j, 1).toString(), scopes.get(idMap.get(j)));
      }
    }

    private void addRow(Point p) {
      int row = table.rowAtPoint(p) + 1;
      idMap.put(row, currentHighestId + 1);
      currentHighestId++;
      Object[] newRow = { " ", "5 = 5", " " };
      tableModel.addRow(newRow);
      ArrayList<Object[]> dataAsList = new ArrayList<Object[]>(dataEntries.length);
      dataAsList.addAll(Arrays.asList(dataEntries));
      dataAsList.add(newRow);
      dataEntries = new Object[dataAsList.size()][];
      for (int j = 0; j < dataAsList.size(); j++) {
        dataEntries[j] = dataAsList.get(j);
      }
      mainInterface.getControlFacade().createWatchExpression(currentHighestId, "5 = 5");
    }

}
