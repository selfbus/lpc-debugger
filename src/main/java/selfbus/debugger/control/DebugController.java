package selfbus.debugger.control;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import selfbus.debugger.gui.Dialogs;
import selfbus.debugger.misc.I18n;
import selfbus.debugger.model.Variable;
import selfbus.debugger.model.cdb.SymbolSpec;
import selfbus.debugger.model.cdb.SymbolType;

/**
 * The debug controller handles the communication with the debugged device.
 */
public class DebugController extends AbstractDebugController implements Closeable
{
   private static final Logger LOGGER = LoggerFactory.getLogger(DebugController.class);
   private static final int MAX_ADDR = 256;
   private Connection connection;
   private Set<Variable> variables;
   private String connectionName;
   private final short[] memCache = new short[256];
   private AddressRanges addrRanges = new LpcAddressRanges();

   public synchronized void setConnectionName(String name)
   {
      this.connectionName = name;
   }

   public synchronized void open()
   {
      if (this.connectionName == null)
         connection = new SimulatedConnection();
      else connection = new SerialConnection(this.connectionName);

      connection.open();
      fireConnectionOpened();
   }

   public synchronized void close()
   {
      if (this.connection != null)
      {
         this.connection.close();
         this.connection = null;

         fireConnectionClosed();
      }
   }

   public synchronized boolean isOpen()
   {
      return this.connection != null;
   }

   public void update()
   {
      LOGGER.trace("Updating the variables");

      fireBeforeUpdate();
      clearMemCache();

      try
      {
         for (Variable var : this.variables)
            update(var);
      }
      finally
      {
         fireAfterUpdate();
      }
   }

   public void update(Variable var)
   {
      int address = var.getAddress();
      int size = var.size();

      if ((this.connection == null) || (address == -1))
      {
         return;
      }
      try
      {
         SymbolSpec spec = var.getSpec();
         SymbolType type = spec.getType();
         byte[] newValue;
         if (type == SymbolType.BIT_FIELD)
         {
            int bitMask = 1 << (address & 0x7);
            address = this.addrRanges.getBitFieldAddr() + (address >> 3);
            newValue = readMem(address, 1);
            if (newValue != null)
               newValue[0] = ((byte) ((newValue[0] & bitMask) == bitMask ? 1 : 0));
         }
         else
         {
            newValue = readMem(address, size);
         }

         if (newValue != null)
         {
            if (var.getPrevValue() != null || !Arrays.equals(newValue, var.getValue()))
            {
               var.setValue(newValue);
               fireValueChanged(var);
            }
         }
      }
      catch (IOException e)
      {
         LOGGER.warn("Communication error, closing connection");
         close();

         Dialogs.showExceptionDialog(e, I18n.getMessage("Error.variableUpdate"));
      }
   }

   protected void clearMemCache()
   {
      for (int i = 0; i < MAX_ADDR; i++)
         this.memCache[i] = -1;
   }

   protected byte[] readMem(int address, int size) throws IOException
   {
      if (address > MAX_ADDR)
      {
         return this.connection.readMem(address, size);
      }
      byte[] result = new byte[size];
      for (int pos = 0; pos < size; pos++)
      {
         result[pos] = readMem(address + pos);
      }
      return result;
   }

   protected byte readMem(int address) throws IOException
   {
      if (address > MAX_ADDR)
      {
         return this.connection.readMem(address);
      }
      if (this.memCache[address] < 0)
      {
         this.memCache[address] = this.connection.readMem(address);
      }
      return (byte) this.memCache[address];
   }

   public void setVariables(Set<Variable> variables)
   {
      this.variables = variables;
      fireVariablesChanged();
   }

   public Set<Variable> getVariables()
   {
      return this.variables;
   }
}
