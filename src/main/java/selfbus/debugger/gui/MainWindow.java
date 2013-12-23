package selfbus.debugger.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import selfbus.debugger.Application;
import selfbus.debugger.actions.ActionFactory;
import selfbus.debugger.actions.AutoUpdateAction;
import selfbus.debugger.actions.ConnectAction;
import selfbus.debugger.control.DebugController;
import selfbus.debugger.control.DebugListener;
import selfbus.debugger.gui.table.ExtTableColumnModel;
import selfbus.debugger.gui.table.ExtTableHeader;
import selfbus.debugger.gui.table.TableUtils;
import selfbus.debugger.gui.table.VariableBytesCellRenderer;
import selfbus.debugger.gui.table.VariableValueCellRenderer;
import selfbus.debugger.gui.table.VariablesTableModel;
import selfbus.debugger.misc.I18n;
import selfbus.debugger.misc.ImageCache;
import selfbus.debugger.model.Variable;
import selfbus.debugger.serial.SerialPortUtil;

/**
 * The main application window.
 */
public class MainWindow extends JFrame implements DebugListener
{
   private static final long serialVersionUID = 8206952584001708390L;
   private static final int TABLE_COLUMNS_MARGIN = 4;

   private static MainWindow instance;

   private JToolBar toolBar = new JToolBar("mainToolbar");
   private JComboBox<String> cboConnection = new JComboBox<String>();
   private VariablesTableModel varsTableModel;
   private TableRowSorter<VariablesTableModel> rowSorter;
   private ExtTableColumnModel varsTableColumnModel = new ExtTableColumnModel();
   private ActionFactory actionFactory = ActionFactory.getInstance();
   private boolean initialUpdate;
   private boolean filterVariables;
   private final Application application;
   private final JTable varsTable = new JTable();
   private final JScrollPane variablesView;
   private final String demoConnectionName = I18n.getMessage("MainWindow.simulatedConnection");
   private final JPopupMenu tablePopup = new JPopupMenu();
   private JToggleButton filterVarsButton;

   /**
    * Create a main application window.
    * 
    * @param application - the application that owns this window.
    */
   public MainWindow(final Application application)
   {
      super(I18n.formatMessage("App.name", new String[] { application.getVersion() }));

      this.application = application;
      instance = this;

      filterVariables = "1".equals(application.getConfig().getProperty("filterVariables", "1"));

      setDefaultCloseOperation(2);
      addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            application.exit();
         }
      });
      ImageIcon appIcon = ImageCache.getIcon("misc/app-icon");
      if (appIcon != null)
      {
         setIconImage(appIcon.getImage());
      }
      setMinimumSize(new Dimension(700, 500));
      setLayout(new BorderLayout());

      add(toolBar, "North");
      setupToolbar();

      varsTable.setGridColor(getBackground());
      varsTable.setColumnModel(varsTableColumnModel);
      ExtTableHeader tableHeader = new ExtTableHeader(varsTable.getColumnModel());
      tableHeader.setPopupMenu(tablePopup);
      varsTable.setTableHeader(tableHeader);

      variablesView = new JScrollPane(varsTable);
      variablesView.getVerticalScrollBar().setUnitIncrement(25);
      variablesView.getVerticalScrollBar().setBlockIncrement(50);
      variablesView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      variablesView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      add(variablesView, "Center");

      cboConnection.setMaximumSize(new Dimension(150, 32));
      cboConnection.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            final DebugController controller = application.getController();

            String connectionName = (String) cboConnection.getSelectedItem();
            if (demoConnectionName.equals(connectionName))
            {
               connectionName = null;
               application.getConfig().remove("connection");
            }
            else
            {
               application.getConfig().setProperty("connection", connectionName);
            }

            final String conName = connectionName;
            SwingUtilities.invokeLater(new Runnable()
            {
               @Override
               public void run()
               {
                  controller.close();
                  controller.setConnectionName(conName);
               }
            });
         }
      });

      connectionClosed();
      setupConnectOptions();
   }

   /**
    * @return The global main-window instance.
    */
   public static MainWindow getInstance()
   {
      return instance;
   }

   /**
    * Setup the tablePopup.
    */
   protected void setupTablePopup()
   {
      final Properties config = application.getConfig();
      tablePopup.removeAll();

      for (int idx = 0; idx < varsTableModel.getColumnCount(); ++idx)
      {
         final TableColumn col = varsTable.getColumnModel().getColumn(idx);
         final int cidx = idx;

         String label;
         if (idx == VariablesTableModel.VISIBLE_COLUMN)
            label = I18n.getMessage("VariableComponent.visibleHeader");
         else label = varsTableModel.getColumnName(idx);
         
         final JCheckBox cbx = new JCheckBox(label);
         boolean isVisible = "1".equals(config.getProperty("columnVisible" + cidx, "1"));
         cbx.setSelected(isVisible);

         if (!isVisible)
            varsTable.getColumnModel().removeColumn(col);
         
         cbx.addActionListener(new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               SwingUtilities.invokeLater(new Runnable()
               {
                  @Override
                  public void run()
                  {
                     if (cbx.isSelected())
                     {
                        config.setProperty("columnVisible" + cidx, "1");
                        varsTableColumnModel.insertColumn(cidx, col);
                     }
                     else
                     {
                        config.setProperty("columnVisible" + cidx, "0");
                        varsTable.getColumnModel().removeColumn(col);
                     }
                  }
               });
            }
         });

         tablePopup.add(cbx);
      }

      varsTable.getTableHeader().add(tablePopup);
   }

   /**
    * Setup the toolbar.
    */
   protected void setupToolbar()
   {
      filterVarsButton = new JToggleButton(actionFactory.getAction("toggleFilterAction"));
      filterVarsButton.setSelected(filterVariables);
      filterVarsButton.setText(null);

      toolBar.add(actionFactory.getAction("connectAction"));
      toolBar.add(cboConnection);
      toolBar.addSeparator();
      toolBar.add(actionFactory.getAction("openFileAction"));
      toolBar.add(actionFactory.getAction("reloadFileAction"));
      toolBar.addSeparator();
      toolBar.add(actionFactory.getAction("updateAction"));
      toolBar.add(actionFactory.getAction("autoUpdateAction"));
      toolBar.add(actionFactory.getAction("unusedValuesAction"));
      toolBar.addSeparator();
      toolBar.add(actionFactory.getAction("showColumnsChooserAction"));
      toolBar.add(filterVarsButton);
      toolBar.add(actionFactory.getAction("settingsAction"));
   }

   /**
    * Setup the connections combobox.
    */
   protected void setupConnectOptions()
   {
      String connectionName = application.getConfig().getProperty("connection");

      for (String portName : SerialPortUtil.getPortNames())
      {
         cboConnection.addItem(portName);

         if (portName.equals(connectionName))
         {
            cboConnection.setSelectedIndex(cboConnection.getItemCount() - 1);
         }
      }
      if (cboConnection.getItemCount() <= 0)
      {
         cboConnection.addItem(demoConnectionName);
         cboConnection.setToolTipText(I18n.getMessage("MainWindow.noConnectionsToolTip"));

         SwingUtilities.invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               Dialogs.showErrorDialog(I18n.getMessage("MainWindow.noConnections"));
            }
         });
      }

      if (cboConnection.getSelectedIndex() < 0)
         cboConnection.setSelectedIndex(0);
   }

   /**
    * Setup the variables.
    */
   protected void setupVariables()
   {
      Set<Variable> varsSet = application.getController().getVariables();
      Vector<Variable> newVars = new Vector<Variable>(varsSet.size());
      newVars.addAll(varsSet);

      varVisibilityFromConfig();

      VariablesTableModel newModel = new VariablesTableModel(newVars);

      RowFilter<VariablesTableModel, Integer> filter = new RowFilter<VariablesTableModel, Integer>()
      {
         @Override
         public boolean include(javax.swing.RowFilter.Entry<? extends VariablesTableModel, ? extends Integer> entry)
         {
            if (!filterVariables)
               return true;

            if (entry.getValueCount() > VariablesTableModel.VISIBLE_COLUMN)
            {
               Object v = entry.getValue(VariablesTableModel.VISIBLE_COLUMN);
               if (v instanceof Boolean) return (Boolean) v;
            }
            return true;
         }
      };

      rowSorter = new TableRowSorter<VariablesTableModel>(newModel);
      rowSorter.setRowFilter(filter);
      varsTable.setRowSorter(rowSorter);

      if (varsTableModel != null)
         varsTableModel.removeTableModelListener(varVisibilityListener);
      newModel.addTableModelListener(varVisibilityListener);
      
      varsTable.setModel(newModel);
      varsTableModel = newModel;

      TableColumnModel columnModel = varsTable.getColumnModel(); 
      columnModel.getColumn(VariablesTableModel.VALUE_COLUMN).setCellRenderer(new VariableValueCellRenderer());
      columnModel.getColumn(VariablesTableModel.BYTES_COLUMN).setCellRenderer(new VariableBytesCellRenderer());

//      variablesView.updateUI();
      setupTablePopup();
   }

   /**
    * Called when the application is shutting down.
    */
   public void beforeShutdown()
   {
   }

   /**
    * Trigger an initial update of the variables. Does nothing if
    * the connection is closed.
    */
   public void initialUpdate()
   {
      initialUpdate = true;
      actionFactory.getAction("updateAction").actionPerformed(null);
   }

   /**
    * Mark all variables as unused.
    */
   public void markVariablesUnused()
   {
      if (varsTableModel != null)
         varsTableModel.markAllUnused();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void variablesChanged()
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            MainWindow.this.setupVariables();
         }
      });
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void valueChanged(final Variable var)
   {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void beforeUpdate()
   {
   }

   /**
    * Pack the columns of the variables table
    */
   public void packTable()
   {
      TableColumnModel colModel = varsTable.getColumnModel();

      for (int columnIndex = colModel.getColumnCount() - 1; columnIndex >= 0; --columnIndex)
      {
         TableColumn col = colModel.getColumn(columnIndex);
         int width = TableUtils.getPreferredColumnWidth(varsTable, columnIndex) + (TABLE_COLUMNS_MARGIN << 1);

         col.setPreferredWidth(width);
         col.setMinWidth(width);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void afterUpdate()
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            if (varsTableModel == null)
               return;

            if (initialUpdate)
            {
               packTable();
               varsTableModel.markAllUnused();
            }
            else varsTableModel.fireValuesChanged();

            initialUpdate = false;
         }
      });
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void connectionOpened()
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            ((ConnectAction) actionFactory.getAction("connectAction")).setConnected();

            actionFactory.getAction("updateAction").setEnabled(true);
            actionFactory.getAction("autoUpdateAction").setEnabled(true);

            try
            {
               Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
            }

            Properties config = Application.getInstance().getConfig();
            if (Boolean.parseBoolean(config.getProperty("initialUpdate", "true")))
               initialUpdate();
         }
      });
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void connectionClosed()
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            ((ConnectAction) actionFactory.getAction("connectAction")).setDisconnected();

            actionFactory.getAction("updateAction").setEnabled(false);

            AutoUpdateAction autoUpdate = (AutoUpdateAction) actionFactory.getAction("autoUpdateAction");
            autoUpdate.setEnabled(false);
            autoUpdate.stopAutoUpdate();
         }
      });
   }

   /**
    * Show the popup menu for selecting the visible table columns.
    * 
    * @param x - the X position of the popup menu
    * @param y - the Y position of the popup menu
    */
   public void showColumnsPopup(int x, int y)
   {
      tablePopup.show(this, x, y);
   }

   /**
    * Update the visibility of hidden variables.
    */
   public void updateHiddenVariables()
   {
      filterVariables = filterVarsButton.isSelected();
      application.getConfig().setProperty("filterVariables", filterVariables ? "1" : "0");

      DebugController controller = Application.getInstance().getController();
      if (controller != null)
      {
         controller.setFilterVariables(filterVariables);

         if (!filterVariables && controller.isOpen())
            controller.update();
      }

      filterVarsButton.setIcon(ImageCache.getIcon(filterVariables ? "icons/filter" : "icons/filter-disabled"));

      if (rowSorter != null)
         rowSorter.sort();
   }

   /**
    * Store visibility information in the config.
    */
   void varVisibilityToConfig()
   {
      StringBuilder sb = new StringBuilder();

      for (Variable var : application.getController().getVariables())
      {
         if (!var.isVisible())
            sb.append(var.getName()).append(';');
      }

      application.getConfig().setProperty("hiddenVariables", sb.toString());
   }

   /**
    * Apply the variable visibility from the config to the variables.
    */
   void varVisibilityFromConfig()
   {
      String[] names = application.getConfig().getProperty("hiddenVariables", "").split(";");
      Set<String> hidden = new HashSet<String>();

      for (String name : names)
         hidden.add(name);

      for (Variable var : application.getController().getVariables())
      {
         var.setVisible(!hidden.contains(var.getName()));
      }
   }

   /**
    * Private listener for visibility change of single variables by the user.
    */
   private TableModelListener varVisibilityListener = new TableModelListener()
   {
      boolean updatePending;

      @Override
      public void tableChanged(TableModelEvent e)
      {
         if (!updatePending && e.getColumn() == VariablesTableModel.VISIBLE_COLUMN && filterVariables && rowSorter != null)
         {
            updatePending = true;

            SwingUtilities.invokeLater(new Runnable()
            {
               @Override
               public void run()
               {
                  updatePending = false;

                  rowSorter.sort();
                  varVisibilityToConfig();
               }
            });
         }
      }
   };
}
