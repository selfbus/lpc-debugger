package selfbus.debugger.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.lang3.Validate;

import selfbus.debugger.Application;
import selfbus.debugger.misc.I18n;
import selfbus.debugger.serial.SerialPortWrapper;

/**
 * A connection to the debugged device that uses the serial port.
 */
public class SerialConnection implements Connection
{
   private final String portName;
   private final SerialPortWrapper port = new SerialPortWrapper();
   private InputStream portIn;
   private OutputStream portOut;

   /**
    * Create a new serial connection.
    *
    * @param portName - the name of the port.
    */
   public SerialConnection(String portName)
   {
      this.portName = portName;
   }

   /**
    * @return The name of the port.
    */
   public String getPortName()
   {
      return portName;
   }

   /**
    * {@inheritDoc}
    */
   public void open()
   {
      Validate.isTrue(!port.isOpened());

      Properties props = Application.getInstance().getConfig();
      int baudRate = Integer.parseInt(props.getProperty("serial.baudRate", "115200"));
      port.setResetOnOpen(Integer.parseInt(props.getProperty("resetOnOpen", "0")) == 1);

      try
      {
         port.open(portName, baudRate, 8, 1, 0);

         portIn = port.getInputStream();
         portOut = port.getOutputStream();
      }
      catch (IOException e)
      {
         throw new RuntimeException(I18n.formatMessage("SerialConnection.errOpen", new String[] { this.portName }), e);
      }
   }

   /**
    * {@inheritDoc}
    */
   public void close()
   {
      portIn = null;
      portOut = null;

      port.close();
   }

   /**
    * {@inheritDoc}
    */
   public byte[] readMem(int address, int size) throws IOException
   {
      Validate.isTrue(this.port.isOpened());

      byte[] data = new byte[size];
      for (int idx = 0; idx < size; idx++)
      {
         data[idx] = readMem(address + idx);
      }

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public byte readMem(int address) throws IOException
   {
      if (this.portIn != null)
      {
         this.portOut.write(address);

         int val = this.portIn.read();
         if (val == -1)
         {
            throw new IOException(I18n.getMessage("SerialConnection.ReadTimeout"));
         }
         return (byte) val;
      }

      return 0;
   }
}
