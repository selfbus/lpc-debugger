package selfbus.debugger.control;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface for connections that the {@link DebugController} uses for connecting to the debugged
 * device.
 */
public interface Connection extends Closeable
{
   /**
    * Open the connection.
    */
   public void open();

   /**
    * Close the connection.
    */
   public void close();

   /**
    * Read a block of memory from the device.
    * 
    * @param addr - the start address of the memory to read.
    * @param count - the number of bytes to read.
    * @return The read memory.
    * @throws IOException in case of communication errors.
    */
   public byte[] readMem(int addr, int count) throws IOException;

   /**
    * Read a single byte from the device.
    * 
    * @param addr - the address of the byte to read.
    * @return The read byte.
    * @throws IOException in case of communication errors.
    */
   public byte readMem(int addr) throws IOException;
}
