package selfbus.debugger.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import selfbus.debugger.Application;
import selfbus.debugger.actions.ActionFactory;
import selfbus.debugger.actions.AutoUpdateAction;
import selfbus.debugger.actions.ConnectAction;
import selfbus.debugger.control.DebugController;
import selfbus.debugger.control.DebugListener;
import selfbus.debugger.misc.I18n;
import selfbus.debugger.misc.ImageCache;
import selfbus.debugger.model.Variable;
import selfbus.debugger.model.VariablesTableModel;
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
   private ActionFactory actionFactory = ActionFactory.getInstance();
   private boolean initialUpdate;
   private final Application application;
   private final JTable varsTable = new JTable();
   private final JScrollPane variablesView = new JScrollPane(varsTable);
   private final String demoConnectionName = I18n.getMessage("MainWindow.simulatedConnection");

   /**
    * Create a main application window.
    * 
    * @param application - the application that owns this window.
    */
   public MainWindow(final Application application)
   {
      super(I18n.getMessage("App.name"));

      this.application = application;
      instance = this;

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

      variablesView.getVerticalScrollBar().setUnitIncrement(25);
      variablesView.getVerticalScrollBar().setBlockIncrement(50);
      variablesView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      variablesView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      add(variablesView, "Center");

      varsTable.setGridColor(getBackground());

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
    * Setup the toolbar.
    */
   protected void setupToolbar()
   {
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
      
      VariablesTableModel newModel = new VariablesTableModel(newVars);

      varsTable.setModel(newModel);
      varsTableModel = newModel;

      TableColumnModel columnModel = varsTable.getColumnModel(); 
      columnModel.getColumn(VariablesTableModel.VALUE_COLUMN).setCellRenderer(new VariableValueCellRenderer());
      columnModel.getColumn(VariablesTableModel.BYTES_COLUMN).setCellRenderer(new VariableBytesCellRenderer());

      this.variablesView.updateUI();
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
}
