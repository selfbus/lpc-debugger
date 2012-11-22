package org.freebus.debugger.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import org.freebus.debugger.Application;
import org.freebus.debugger.gui.GlobFileFilter;
import org.freebus.debugger.gui.MainWindow;
import org.freebus.debugger.misc.I18n;
import org.freebus.debugger.misc.ImageCache;

/**
 * Open a file that contains debug information.
 */
public final class OpenFileAction extends BasicAction
{
   private static final long serialVersionUID = -3518750343333514078L;

   public OpenFileAction()
   {
      super(I18n.getMessage("OpenFileAction.name"), I18n.getMessage("OpenFileAction.toolTip"), ImageCache
         .getIcon("icons/fileopen"));
      putValue("AcceleratorKey", KeyStroke.getKeyStroke(79, 128));
   }

   public void actionPerformed(ActionEvent e)
   {
      Application app = Application.getInstance();
      MainWindow mainWin = MainWindow.getInstance();
      Properties config = app.getConfig();

      File dir = new File(config.getProperty("lastOpenDir", app.getHomeDir().getAbsolutePath()));
      if (!dir.isDirectory())
      {
         dir = app.getHomeDir();
      }
      JFileChooser dlg = new JFileChooser(dir);

      FileFilter fileFilter = new GlobFileFilter(I18n.getMessage("FileFilter.cdb"), "*.cdb");
      dlg.addChoosableFileFilter(fileFilter);
      dlg.addChoosableFileFilter(dlg.getAcceptAllFileFilter());
      dlg.setFileFilter(fileFilter);
      dlg.setDialogTitle(I18n.formatMessage("OpenFileAction.title", new String[] { I18n.getMessage("App.name") }));

      if (dlg.showOpenDialog(mainWin) != 0)
         return;
 
      File file = dlg.getSelectedFile();
      if (file == null)
         return;

      app.setCdbFile(file);
      app.getConfig().setProperty("lastOpenDir", file.getParentFile().getAbsolutePath());
      app.getConfig().setProperty("lastCdbFile", file.getAbsolutePath());
   }
}
