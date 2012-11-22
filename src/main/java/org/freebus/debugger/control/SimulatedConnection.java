package org.freebus.debugger.control;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simulated connection to a debugged device.
 */
public class SimulatedConnection implements Connection
{
   private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedConnection.class);
   private static final int MAX_ADDR = 256;
   private static final int VALUE_CHANGES_PER_READ = 5;
   private final byte[] ram = new byte[256];

   public void open()
   {
      LOGGER.debug("Opening simulated connection");
   }

   public void close()
   {
      LOGGER.debug("Closing simulated connection");
   }

   public byte[] readMem(int address, int size) throws IOException
   {
      for (int i = VALUE_CHANGES_PER_READ; i > 0; i--)
      {
         this.ram[((int) (Math.random() * MAX_ADDR))] = ((byte) (int) (Math.random() * MAX_ADDR));
      }
      byte[] data = new byte[size];
      for (int idx = 0; idx < size; idx++)
      {
         data[idx] = this.ram[((address + idx) % MAX_ADDR)];
      }

      return data;
   }

   public byte readMem(int address) throws IOException
   {
      return readMem(address, 1)[0];
   }
}
