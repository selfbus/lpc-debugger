package selfbus.debugger.serial;

import gnu.io.CommPortIdentifier;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Utility low-level methods for accessing serial ports.
 */
public final class SerialPortUtil
{
   /**
    * Get a list of the available serial ports.
    * 
    * @return the names of the available serial ports, alphabetically sorted.
    */
   public static String[] getPortNames()
   {
      final Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
      final Vector<String> portNames = new Vector<String>(20);

      while (portList.hasMoreElements())
      {
         CommPortIdentifier portIdent = (CommPortIdentifier) portList.nextElement();

         if (portIdent.getPortType() == CommPortIdentifier.PORT_SERIAL)
            portNames.add(portIdent.getName());
      }

      final String[] result = new String[portNames.size()];
      portNames.toArray(result);

      Arrays.sort(result);
      return result;
   }
}
