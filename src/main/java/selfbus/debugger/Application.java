package selfbus.debugger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.Validate;

import selfbus.debugger.control.DebugController;
import selfbus.debugger.gui.Dialogs;
import selfbus.debugger.gui.MainWindow;
import selfbus.debugger.misc.AbstractApplication;
import selfbus.debugger.misc.I18n;
import selfbus.debugger.misc.LookAndFeelManager;
import selfbus.debugger.model.Variable;
import selfbus.debugger.model.cdb.AddressSpace;
import selfbus.debugger.model.cdb.CdbParser;
import selfbus.debugger.model.cdb.Symbol;
import selfbus.debugger.model.cdb.SymbolSpec;
import selfbus.debugger.model.cdb.SymbolType;
import selfbus.debugger.model.map.MapParser;
import selfbus.debugger.model.map.MapUtils;

/**
 * The application class.
 */
public final class Application extends AbstractApplication
{
   private final DebugController controller = new DebugController();
   private File cdbFile;
   private MainWindow win;

   public static Application getInstance()
   {
      return (Application) AbstractApplication.getInstance();
   }

   public DebugController getController()
   {
      return this.controller;
   }

   protected void startup()
   {
      LookAndFeelManager.setDefaultLookAndFeel();

      File configFile = getConfigFile();
      if (configFile.exists())
      {
         loadConfig(configFile);
      }
      win = new MainWindow(this);
      controller.addListener(win);
      win.setVisible(true);
   }

   protected void shutdown()
   {
      getConfig().setProperty("connected", this.controller.isOpen() ? "true" : "false");

      this.controller.removeListener(this.win);

      saveConfig(getConfigFile(), "lpc-debugger configuration");
   }

   public File getConfigFile()
   {
      return new File(getAppDir("lpc-debugger") + ".conf");
   }

   public void setCdbFile(File file)
   {
      Validate.notNull(file);
      CdbParser parser;

      try
      {
         parser = new CdbParser(file);
         parser.parse();
      }
      catch (FileNotFoundException e)
      {
         Dialogs.showExceptionDialog(e, I18n.formatMessage("Error.fileNotFound", new String[] { file.toString() }));
         return;
      }
      catch (IOException e)
      {
         Dialogs.showExceptionDialog(e, I18n.formatMessage("Error.fileRead", new String[] { file.toString() }));
         return;
      }
      catch (ParseException e)
      {
         Dialogs.showExceptionDialog(e, I18n.formatMessage("Error.parseException", new String[] { file.toString() }));
         return;
      }

      Set<Variable> variables = new TreeSet<Variable>();
      for (Symbol sym : parser.getSymbols().values())
      {
         SymbolSpec spec = sym.getSymbolSpec();
         AddressSpace addrSpace = sym.getAddressSpace();

         if (!SymbolType.FUNCTION.equals(spec.getType()))
         {
            if ((AddressSpace.RAM_INT_LOW.equals(addrSpace)) || (AddressSpace.RAM_INT.equals(addrSpace))
               || (AddressSpace.RAM_EXT.equals(addrSpace)) || (AddressSpace.BIT.equals(addrSpace)))
            {
               Variable var = new Variable(sym.getName(), sym.getAddress(), spec.getSize(), sym.getModule());
               var.setSpec(spec);
               variables.add(var);
            }
         }
      }

      controller.setVariables(variables);
      win.setTitle(file.getName() + " - " + I18n.getMessage("App.name"));
      cdbFile = file;

      win.initialUpdate();
   }

   public void setMapFile(File file)
   {
      MapParser parser;
      try
      {
         parser = new MapParser(new FileInputStream(file));
         parser.parse();
      }
      catch (FileNotFoundException e)
      {
         Dialogs.showExceptionDialog(e, I18n.formatMessage("Error.fileNotFound", new String[] { file.toString() }));
         return;
      }
      catch (IOException e)
      {
         Dialogs.showExceptionDialog(e, I18n.formatMessage("Error.fileRead", new String[] { file.toString() }));
         return;
      }

      Set<Variable> variables = new TreeSet<Variable>();
      variables.addAll(MapUtils.createVariablesForArea(parser.getArea(".ABS.")));
      variables.addAll(MapUtils.createVariablesForArea(parser.getArea("DSEG")));

      controller.setVariables(variables);

      getConfig().setProperty("lastOpenDir", file.getParentFile().getAbsolutePath());
      win.setTitle(file.getName() + " - " + I18n.getMessage("App.name"));
   }

   public void reloadFiles()
   {
      if (cdbFile != null)
         setCdbFile(cdbFile);
   }
}
