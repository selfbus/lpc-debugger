package selfbus.debugger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
   private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

   private final DebugController controller = new DebugController();
   private File cdbFile;
   private MainWindow win;
   private String version;

   /**
    * @return The application instance.
    */
   public static Application getInstance()
   {
      return (Application) AbstractApplication.getInstance();
   }

   /**
    * @return The controller.
    */
   public DebugController getController()
   {
      return this.controller;
   }

   /**
    * @return The application's version.
    */
   public String getVersion()
   {
      return version == null ? "(devel)" : version;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void startup()
   {
      LookAndFeelManager.setDefaultLookAndFeel();

      File configFile = getConfigFile();
      if (configFile.exists())
      {
         loadConfig(configFile);
      }

      Properties props = getManifestProperties(getClass());
      version = props.getProperty("version");

      win = new MainWindow(this);
      controller.addListener(win);
      win.setVisible(true);
   }

   @Override
   protected void ready()
   {
      win.updateHiddenVariables();
   }

   @Override
   protected void shutdown()
   {
      getConfig().setProperty("connected", controller.isOpen() ? "true" : "false");

      controller.removeListener(win);
      win.beforeShutdown();

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

      if (variables.size() == 0 && parser.getNumUnresolved() > 0)
      {
         Dialogs.showWarningDialog(I18n.getMessage("Error.noSymbolsFound") + "\n\n" +
            I18n.getMessage("Error.noSymbolsFoundDetails"));
      }

      LOGGER.debug("{} variables loaded", variables.size());
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


   /**
    * Get the manifest properties of the JAR that contains the given class.
    * 
    * @param clazz - the class to use for searching the manifest properties. 
    * 
    * @return the manifest's properties.
    */
   public Properties getManifestProperties(Class<?> clazz)
   {
      Properties manifestProps = new Properties();

      try
      {
         final String classContainer = clazz.getProtectionDomain().getCodeSource().getLocation().toString();

         if (classContainer.toLowerCase().endsWith(".jar"))
         {
            final URL manifestUrl = new URL("jar:" + classContainer + "!/META-INF/MANIFEST.MF");
            manifestProps.load(manifestUrl.openStream());
         }
         else
         {
            LOGGER.info("FTS is not started from a jar. Manifest information is unavailable.");
         }
      }
      catch (IOException e)
      {
         Dialogs.formatExceptionMessage(e, "Failed to load the manifest from the FTS jar");
      }

      return manifestProps;
   }
}
