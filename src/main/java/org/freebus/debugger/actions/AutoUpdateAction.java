package org.freebus.debugger.actions;

import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.swing.SwingWorker;

import org.freebus.debugger.Application;
import org.freebus.debugger.misc.I18n;
import org.freebus.debugger.misc.ImageCache;

/**
 * Enable / disable automatic update of the values.
 */
public class AutoUpdateAction extends BasicAction
{
   private static final long serialVersionUID = 6681655204509992504L;
   private SwingWorker<Void, Void> worker;
   private int updateIntervalMsec = 2000;

   public AutoUpdateAction()
   {
      super(I18n.getMessage("AutoUpdateAction.name"), I18n.getMessage("AutoUpdateAction.toolTip"), ImageCache
         .getIcon("icons/play"));
   }

   public void updateConfig()
   {
      Properties props = Application.getInstance().getConfig();
      this.updateIntervalMsec = Integer.parseInt(props.getProperty("autoUpdateMsec", Integer.toString(2000)));
   }

   public synchronized void startAutoUpdate()
   {
      if (worker != null)
         return;

      updateConfig();

      worker = new SwingWorker<Void, Void>()
      {
         protected Void doInBackground() throws Exception
         {
            while (AutoUpdateAction.this.worker != null)
            {
               Application.getInstance().getController().update();

               if (AutoUpdateAction.this.updateIntervalMsec > 0)
               {
                  Thread.sleep(AutoUpdateAction.this.updateIntervalMsec);
               }
            }
            return null;
         }
      };

      worker.execute();

      setIcon(ImageCache.getIcon("icons/stop"));
      setToolTipText(I18n.getMessage("AutoUpdateAction.toolTipStop"));
      ActionFactory.getInstance().getAction("updateAction").setEnabled(false);
   }

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
