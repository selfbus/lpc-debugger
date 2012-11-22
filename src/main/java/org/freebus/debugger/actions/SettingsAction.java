package org.freebus.debugger.actions;

import java.awt.event.ActionEvent;
import java.util.Properties;
import org.freebus.debugger.Application;
import org.freebus.debugger.gui.MainWindow;
import org.freebus.debugger.gui.SettingsDialog;
import org.freebus.debugger.misc.I18n;
import org.freebus.debugger.misc.ImageCache;

/**
 * Open the settings dialog.
 */
public class SettingsAction extends BasicAction
{
   private static final long serialVersionUID = 1726019092024249094L;

   public SettingsAction()
   {
      super(I18n.getMessage("SettingsAction.name"), I18n.getMessage("SettingsAction.toolTip"), ImageCache
         .getIcon("icons/configure"));
   }

   public synchronized void actionPerformed(ActionEvent e)
   {
      Properties config = Application.getInstance().getConfig();
      SettingsDialog dialog = new SettingsDialog(MainWindow.getInstance(), config);

      dialog.setDefaultCloseOperation(2);
      dialog.setModal(true);
      dialog.setVisible(true);
   }
}
