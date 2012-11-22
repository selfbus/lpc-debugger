package org.freebus.debugger.model.cdb;

/**
 * A structure symbol specification.
 */
public class StructureSymbolSpec extends SymbolSpec
{
   private final String name;

   public StructureSymbolSpec(String name, int size)
   {
      super(SymbolType.STRUCTURE, size);
      this.name = name;
   }

   public String getName()
   {
      return this.name;
   }
}
