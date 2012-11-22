package org.freebus.debugger.actions;

import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import org.freebus.debugger.Application;
import org.freebus.debugger.misc.I18n;
import org.freebus.debugger.misc.ImageCache;

/**
 * Reload the currently opened file.
 */
public final class ReloadFileAction extends BasicAction
{
   private static final long serialVersionUID = 7176946150752605923L;

   public ReloadFileAction()
   {
      super(I18n.getMessage("ReloadFileAction.name"), I18n.getMessage("ReloadFileAction.toolTip"), ImageCache
         .getIcon("icons/fileopen"));
      putValue("AcceleratorKey", KeyStroke.getKeyStroke("F5"));
   }

   public void actionPerformed(ActionEvent e)
   {
      Application.getInstance().reloadFiles();
   }
}
