package selfbus.debugger.actions;

import java.awt.event.ActionEvent;

import selfbus.debugger.Application;
import selfbus.debugger.control.DebugController;
import selfbus.debugger.misc.I18n;

/**
 * Connect / disconnect from the debugged device.
 */
public class ConnectAction extends BasicAction
{
   private static final long serialVersionUID = -1969938201317728166L;

   public ConnectAction()
   {
      super(I18n.getMessage("ConnectAction.connect"), I18n.getMessage("ConnectAction.toolTipConnect"), null);
   }

   public void actionPerformed(ActionEvent e)
   {
      DebugController controller = Application.getInstance().getController();
      if (controller.isOpen())
      {
         controller.close();
      }
      else
      {
         controller.open();
      }
   }

   public void setConnected()
   {
      setName(I18n.getMessage("ConnectAction.disconnect"));
      setToolTipText(I18n.getMessage("ConnectAction.toolTipDisconnect"));
   }

   public void setDisconnected()
   {
      setName(I18n.getMessage("ConnectAction.connect"));
      setToolTipText(I18n.getMessage("ConnectAction.toolTipConnect"));
   }
}
