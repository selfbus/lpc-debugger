package org.freebus.debugger.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.Validate;
import org.freebus.debugger.Application;
import org.freebus.debugger.misc.I18n;
import org.freebus.debugger.serial.SerialPortWrapper;

/**
 * A connection to the debugged device that uses the serial port.
 */
public class SerialConnection implements Connection
{
   private final String portName;
   private final SerialPortWrapper port = new SerialPortWrapper();
   private InputStream portIn;
   private OutputStream portOut;

   public SerialConnection(String portName)
   {
      this.portName = portName;
   }

   public String getPortName()
   {
      return this.portName;
   }

   public void open()
   {
      Validate.isTrue(!this.port.isOpened());

      int baudRate = Integer.parseInt(Application.getInstance().getConfig().getProperty("serial.baudRate", "115200"));
      try
      {
         this.port.open(this.portName, baudRate, 8, 1, 0);

         this.portIn = this.port.getInputStream();
         this.portOut = this.port.getOutputStream();
      }
      catch (IOException e)
      {
         throw new RuntimeException(I18n.formatMessage("SerialConnection.errOpen", new String[] { this.portName }), e);
      }
   }

   public void close()
   {
      this.portIn = null;
      this.portOut = null;

      this.port.close();
   }

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
