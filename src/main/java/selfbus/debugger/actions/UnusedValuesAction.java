package selfbus.debugger.actions;

import java.awt.event.ActionEvent;
import javax.swing.SwingWorker;

import selfbus.debugger.gui.MainWindow;
import selfbus.debugger.misc.I18n;
import selfbus.debugger.misc.ImageCache;

/**
 * Mark all variable values as unused.
 */
public class UnusedValuesAction extends BasicAction
{
   private static final long serialVersionUID = 500131997127756668L;

   public UnusedValuesAction()
   {
      super(I18n.getMessage("UnusedValuesAction.name"), I18n.getMessage("UnusedValuesAction.toolTip"), ImageCache
         .getIcon("icons/eraser"));
   }

   public void actionPerformed(ActionEvent e)
   {
      new SwingWorker<Void,Void>()
      {
         protected Void doInBackground() throws Exception
         {
            MainWindow.getInstance().markVariablesUnused();
            return null;
         }
      }.execute();
   }
}
