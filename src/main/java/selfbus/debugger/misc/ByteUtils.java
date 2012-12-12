package selfbus.debugger.misc;

public final class ByteUtils
{
   public static int toInteger(byte[] data, int offset, int count)
   {
      int result = 0;

      for (int pos = offset + count - 1; pos >= offset; --pos)
      {
         result = result << 8 | data[pos] & 0xFF;
      }

      return result;
   }
}
