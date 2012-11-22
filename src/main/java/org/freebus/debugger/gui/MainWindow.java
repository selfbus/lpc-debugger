package org.freebus.debugger.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.freebus.debugger.Application;
import org.freebus.debugger.actions.ActionFactory;
import org.freebus.debugger.actions.AutoUpdateAction;
import org.freebus.debugger.actions.ConnectAction;
import org.freebus.debugger.control.DebugController;
import org.freebus.debugger.control.DebugListener;
import org.freebus.debugger.misc.I18n;
import org.freebus.debugger.misc.ImageCache;
import org.freebus.debugger.model.Variable;
import org.freebus.debugger.serial.SerialPortUtil;

/**
 * The main application window.
 */
public class MainWindow extends JFrame implements DebugListener
{
   private static final long serialVersionUID = 8206952584001708390L;
   private static MainWindow instance;

   private Map<String, VariableComponent> varComps = new HashMap<String, VariableComponent>(256);

   private JToolBar toolBar = new JToolBar("mainToolbar");
   private JComboBox<String> cboConnection = new JComboBox<String>();
   private ActionFactory actionFactory = ActionFactory.getInstance();
   private boolean initialUpdate;
   private final Application application;
   private final JPanel variablesPanel = new JPanel();
   private final JScrollPane variablesView = new JScrollPane(this.variablesPanel);
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

      add(this.toolBar, "North");
      setupToolbar();

      this.variablesView.getVerticalScrollBar().setUnitIncrement(25);
      this.variablesView.getVerticalScrollBar().setBlockIncrement(50);
      add(this.variablesView, "Center");

      this.variablesPanel.setLayout(new GridBagLayout());
      this.variablesPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

      this.cboConnection.setMaximumSize(new Dimension(150, 32));
      this.cboConnection.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            DebugController controller = application.getController();

            String connectionName = (String) MainWindow.this.cboConnection.getSelectedItem();
            if (MainWindow.this.demoConnectionName.equals(connectionName))
            {
               connectionName = null;
               application.getConfig().remove("connection");
            }
            else
            {
               application.getConfig().setProperty("connection", connectionName);
            }

            controller.setConnectionName(connectionName);
            controller.close();
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
      this.toolBar.add(this.actionFactory.getAction("connectAction"));
      this.toolBar.add(this.cboConnection);
      this.toolBar.addSeparator();
      this.toolBar.add(this.actionFactory.getAction("openFileAction"));
      this.toolBar.addSeparator();
      this.toolBar.add(this.actionFactory.getAction("updateAction"));
      this.toolBar.add(this.actionFactory.getAction("autoUpdateAction"));
      this.toolBar.add(this.actionFactory.getAction("unusedValuesAction"));
      this.toolBar.addSeparator();
      this.toolBar.add(this.actionFactory.getAction("settingsAction"));
   }

   /**
    * Setup the connections combobox.
    */
   protected void setupConnectOptions()
   {
      String connectionName = this.application.getConfig().getProperty("connection");

      for (String portName : SerialPortUtil.getPortNames())
      {
         this.cboConnection.addItem(portName);

         if (portName.equals(connectionName))
         {
            this.cboConnection.setSelectedIndex(this.cboConnection.getItemCount() - 1);
         }
      }
      if (this.cboConnection.getItemCount() <= 0)
      {
         this.cboConnection.addItem(this.demoConnectionName);
         this.cboConnection.setToolTipText(I18n.getMessage("MainWindow.noConnectionsToolTip"));

         SwingUtilities.invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               Dialogs.showErrorDialog(I18n.getMessage("MainWindow.noConnections"));
            }
         });
      }

      if (this.cboConnection.getSelectedIndex() < 0)
         this.cboConnection.setSelectedIndex(0);
   }

   /**
    * Setup the variables.
    */
   protected void setupVariables()
   {
      this.variablesPanel.removeAll();

      Map<String, VariableComponent> newVarComps = new HashMap<String, VariableComponent>(256);
      Color bgColor1 = this.variablesPanel.getBackground();
      Color bgColor2 = bgColor1.brighter();

      int row = -1;
      VariableComponent.addHeaderTo(this.variablesPanel, 0, ++row, this.variablesPanel.getBackground().darker());

      for (Variable var : this.application.getController().getVariables())
      {
         VariableComponent varComp = new VariableComponent(var, (row & 0x1) == 1 ? bgColor1 : bgColor2);
         varComp.addTo(this.variablesPanel, 0, ++row);

         newVarComps.put(var.getName(), varComp);
      }

      this.variablesPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, ++row, 1, 1, 1.0D, 100.0D, 18, 2,
         new Insets(0, 0, 0, 0), 0, 0));

      this.varComps = newVarComps;
      this.variablesView.updateUI();
   }

   /**
    * Mark all variables as unused.
    */
   public void setVariablesUnused()
   {
      for (VariableComponent varComp : this.varComps.values())
         varComp.initValue();
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
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            VariableComponent varComp = (VariableComponent) MainWindow.this.varComps.get(var.getName());

            if (varComp != null)
            {
               if (MainWindow.this.initialUpdate)
                  varComp.initValue();
               else varComp.valueChanged();
            }
         }
      });
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void beforeUpdate()
   {
      try
      {
         SwingUtilities.invokeAndWait(new Runnable()
         {
            @Override
            public void run()
            {
               for (VariableComponent varComp : MainWindow.this.varComps.values())
                  varComp.oldenValue();
            }
         });
      }
      catch (InvocationTargetException | InterruptedException e)
      {
         Dialogs.showExceptionDialog(e, "GUI update failed");
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void afterUpdate()
   {
      this.initialUpdate = false;
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
            ((ConnectAction) MainWindow.this.actionFactory.getAction("connectAction")).setConnected();

            MainWindow.this.actionFactory.getAction("updateAction").setEnabled(true);
            MainWindow.this.actionFactory.getAction("autoUpdateAction").setEnabled(true);

            MainWindow.this.initialUpdate = true;
            MainWindow.this.actionFactory.getAction("updateAction").actionPerformed(null);
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
            ((ConnectAction) MainWindow.this.actionFactory.getAction("connectAction")).setDisconnected();

            MainWindow.this.actionFactory.getAction("updateAction").setEnabled(false);

            AutoUpdateAction autoUpdate = (AutoUpdateAction) MainWindow.this.actionFactory
               .getAction("autoUpdateAction");
            autoUpdate.setEnabled(false);
            autoUpdate.stopAutoUpdate();
         }
      });
   }
}
