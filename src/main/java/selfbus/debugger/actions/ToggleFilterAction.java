package selfbus.debugger.actions;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import selfbus.debugger.gui.MainWindow;
import selfbus.debugger.misc.I18n;
import selfbus.debugger.misc.ImageCache;

/**
 * Enable/disable filtering of displayed variables
 */
public final class ToggleFilterAction extends BasicAction
{
   private static final long serialVersionUID = 642074608298866065L;

   public ToggleFilterAction()
   {
      super(I18n.getMessage("ToggleFilterAction.name"),
            I18n.getMessage("ToggleFilterAction.toolTip"),
            ImageCache.getIcon("icons/filter"));
      putValue("AcceleratorKey", KeyStroke.getKeyStroke(79, 128));
   }

   public void actionPerformed(ActionEvent e)
   {
      MainWindow.getInstance().updateHiddenVariables();
   }
}
