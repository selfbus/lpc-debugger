package org.freebus.debugger;

import java.io.File;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * The application's main
 */
public class Main
{
   public static void main(String[] args)
   {
      SLF4JBridgeHandler.install();

      Application app = new Application();
      app.start();

      if (args.length > 0)
      {
         app.setCdbFile(new File(args[0]));
      }
      else
      {
         String lastCdbFileName = app.getConfig().getProperty("lastCdbFile");
         if (lastCdbFileName != null)
         {
            File lastCdbFile = new File(lastCdbFileName);
            if (lastCdbFile.exists())
            {
               app.setCdbFile(lastCdbFile);
            }
         }
      }
      if ("true".equals(app.getConfig().getProperty("connected").toLowerCase()))
         app.getController().open();
   }
}
