package org.freebus.debugger.misc;

public final class ByteUtils
{
   public static int toInteger(byte[] data, int offset, int count)
   {
      int result = 0;

      while (count > 0)
      {
         result = result << 8 | data[(offset++)] & 0xFF;
         count--;
      }

      return result;
   }
}
