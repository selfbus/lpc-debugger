package org.freebus.debugger.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import org.freebus.debugger.actions.ActionFactory;
import org.freebus.debugger.actions.AutoUpdateAction;
import org.freebus.debugger.misc.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The settings dialog. Used for configuring the application.
 */
public class SettingsDialog extends JDialog
{
   private static final long serialVersionUID = 2238040856367925931L;
   private static final Logger LOGGER = LoggerFactory.getLogger(SettingsDialog.class);

   private static final Insets PARAGRAPH_INSETS = new Insets(10, 8, 2, 8);
   private static final Insets INSETS = new Insets(2, 8, 2, 8);
   private static final int[] AUTO_UPDATE_MSEC = { 0, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000 };
   private static final int DEFAULT_AUTO_UPDATE_MSEC = 2000;

   private final JComboBox<Integer> cboBaudRate = new JComboBox<Integer>();
   private final JComboBox<String> cboAutoUpdate = new JComboBox<String>();
   private final Properties props;

   /**
    * Create a settings dialog.
    *
    * @param owner - the owning window.
    * @param props - the application's configuration.
    */
   public SettingsDialog(Frame owner, Properties props)
   {
      super(owner, I18n.formatMessage("Settings.title", new String[] { I18n.getMessage("App.name") }));
      this.props = props;

      addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            SettingsDialog.this.toConfig();
         }
      });
      setMinimumSize(new Dimension(400, 300));
      setLayout(new GridBagLayout());
      int row = -1;

      add(new JLabel(I18n.getMessage("Settings.baudRate")), new GridBagConstraints(0, ++row, 1, 1, 1.0D, 1.0D, 18, 0,
         PARAGRAPH_INSETS, 0, 0));
      add(this.cboBaudRate, new GridBagConstraints(0, ++row, 1, 1, 1.0D, 1.0D, 18, 0, INSETS, 0, 0));

      add(new JLabel(I18n.getMessage("Settings.autoUpdateInterval")), new GridBagConstraints(0, ++row, 1, 1, 1.0D,
         1.0D, 18, 0, PARAGRAPH_INSETS, 0, 0));
      add(this.cboAutoUpdate, new GridBagConstraints(0, ++row, 1, 1, 1.0D, 1.0D, 18, 0, INSETS, 0, 0));

      add(Box.createVerticalGlue(), new GridBagConstraints(0, ++row, 1, 1, 1.0D, 100.0D, 18, 2, new Insets(0, 0, 0, 0),
         0, 0));

      JButton closeButton = new JButton(I18n.getMessage("Settings.close"));
      add(closeButton, new GridBagConstraints(0, ++row, 1, 1, 1.0D, 1.0D, 14, 0, new Insets(20, 8, 8, 8), 0, 0));
      closeButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            SettingsDialog.this.toConfig();
            SettingsDialog.this.dispose();
         }
      });
      setupBaudRates();
      setupAutoUpdates();

      fromConfig();
   }

   /**
    * Setup the baud-rates combobox.
    */
   protected void setupBaudRates()
   {
      this.cboBaudRate.addItem(Integer.valueOf(4800));
      this.cboBaudRate.addItem(Integer.valueOf(9600));
      this.cboBaudRate.addItem(Integer.valueOf(19200));
      this.cboBaudRate.addItem(Integer.valueOf(38400));
      this.cboBaudRate.addItem(Integer.valueOf(57600));
      this.cboBaudRate.addItem(Integer.valueOf(115200));
      this.cboBaudRate.addItem(Integer.valueOf(230400));
   }

   /**
    * Setup the auto-updates combobox.
    */
   protected void setupAutoUpdates()
   {
      for (int updateMsec : AUTO_UPDATE_MSEC)
      {
         if (updateMsec <= 1000)
         {
            this.cboAutoUpdate.addItem(I18n.formatMessage("Settings.autoUpdateMsec",
               new String[] { Integer.toString(updateMsec) }));
         }
         else
         {
            this.cboAutoUpdate.addItem(I18n.formatMessage("Settings.autoUpdateSec",
               new String[] { Float.toString(updateMsec * 0.001F) }));
         }
      }
   }

   /**
    * Initialize the dialog values from the configuration.
    */
   public void fromConfig()
   {
      int baudRate = Integer.parseInt(this.props.getProperty("serial.baudRate", "115200"));

      for (int i = 0; i < this.cboBaudRate.getItemCount(); i++)
      {
         if (((Integer) this.cboBaudRate.getItemAt(i)).intValue() == baudRate)
         {
            this.cboBaudRate.setSelectedIndex(i);
            break;
         }
      }

      int updateMsec = Integer.parseInt(this.props.getProperty("autoUpdateMsec", Integer.toString(DEFAULT_AUTO_UPDATE_MSEC)));
      for (int i = 0; i < this.cboAutoUpdate.getItemCount(); i++)
      {
         if (AUTO_UPDATE_MSEC[i] == updateMsec)
         {
            this.cboAutoUpdate.setSelectedIndex(i);
            break;
         }
      }
   }

   /**
    * Write the dialog values to the configuration.
    */
   public void toConfig()
   {
      LOGGER.debug("Writing configuration");
      this.props.setProperty("serial.baudRate",
         Integer.toString(((Integer) this.cboBaudRate.getSelectedItem()).intValue()));
      this.props.setProperty("autoUpdateMsec",
         Integer.toString(AUTO_UPDATE_MSEC[this.cboAutoUpdate.getSelectedIndex()]));

      ActionFactory actionFactory = ActionFactory.getInstance();
      AutoUpdateAction autoUpdateAction = (AutoUpdateAction) actionFactory.getAction("autoUpdateAction");
      autoUpdateAction.updateConfig();
   }
}
