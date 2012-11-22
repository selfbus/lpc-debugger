package selfbus.debugger.actions;

import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.swing.SwingWorker;

import selfbus.debugger.Application;
import selfbus.debugger.misc.I18n;
import selfbus.debugger.misc.ImageCache;

/**
 * Enable / disable automatic update of the values.
 */
public class AutoUpdateAction extends BasicAction
{
   private static final long serialVersionUID = 6681655204509992504L;
   private SwingWorker<Void, Void> worker;
   private int updateIntervalMsec = 2000;

   /**
    * Create an auto-update action.
    */
   public AutoUpdateAction()
   {
      super(I18n.getMessage("AutoUpdateAction.name"), I18n.getMessage("AutoUpdateAction.toolTip"), ImageCache
         .getIcon("icons/play"));
   }

   /**
    * Update the configuration.
    */
   public void updateConfig()
   {
      Properties props = Application.getInstance().getConfig();
      this.updateIntervalMsec = Integer.parseInt(props.getProperty("autoUpdateMsec", Integer.toString(2000)));
   }

   /**
    * Start the automatic update.
    */
   public synchronized void startAutoUpdate()
   {
      if (worker != null)
         return;

      updateConfig();

      worker = new SwingWorker<Void, Void>()
      {
         protected Void doInBackground() throws Exception
         {
            while (worker != null)
            {
               long start = System.currentTimeMillis();
               Application.getInstance().getController().update();
               long elapsed = System.currentTimeMillis() - start;

               if (updateIntervalMsec > elapsed)
                  Thread.sleep(updateIntervalMsec - elapsed);
            }
            return null;
         }
      };

      worker.execute();

      setIcon(ImageCache.getIcon("icons/stop"));
      setToolTipText(I18n.getMessage("AutoUpdateAction.toolTipStop"));
      ActionFactory.getInstance().getAction("updateAction").setEnabled(false);
   }

   /**
    * Stop the automatic update.
    */
   public synchronized void stopAutoUpdate()
   {
      if (worker == null)
      {
         return;
      }

      worker = null;
      setIcon(ImageCache.getIcon("icons/play"));
      setToolTipText(I18n.getMessage("AutoUpdateAction.toolTip"));
      ActionFactory.getInstance().getAction("updateAction").setEnabled(true);
   }

   public void actionPerformed(ActionEvent e)
   {
      if (worker == null)
      {
         startAutoUpdate();
      }
      else
      {
         stopAutoUpdate();
      }
   }
}
