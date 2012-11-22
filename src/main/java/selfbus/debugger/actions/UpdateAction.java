package selfbus.debugger.actions;

import java.awt.event.ActionEvent;

import javax.swing.SwingWorker;

import selfbus.debugger.Application;
import selfbus.debugger.misc.I18n;
import selfbus.debugger.misc.ImageCache;

/**
 * Update the variables from the debugged device.
 */
public class UpdateAction extends BasicAction
{
   private static final long serialVersionUID = -3319541524309142507L;

   public UpdateAction()
   {
      super(I18n.getMessage("UpdateAction.name"), I18n.getMessage("UpdateAction.toolTip"), ImageCache
         .getIcon("icons/reload"));
   }

   public void actionPerformed(ActionEvent e)
   {
      new SwingWorker<Void,Void>()
      {
         protected Void doInBackground() throws Exception
         {
            Application.getInstance().getController().update();
            return null;
         }
      }.execute();
   }
}
