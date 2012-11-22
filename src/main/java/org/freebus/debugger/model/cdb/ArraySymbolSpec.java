package org.freebus.debugger.model.cdb;

/**
 * An array symbol specification.
 */
public class ArraySymbolSpec extends SymbolSpec
{
   private final int count;

   public ArraySymbolSpec(SymbolType type, int size, int count)
   {
      super(type, size);
      this.count = count;
   }

   public int getCount()
   {
      return this.count;
   }
}
