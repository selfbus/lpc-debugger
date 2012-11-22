package selfbus.debugger.model.cdb;

/**
 * A symbol specification.
 */
public class SymbolSpec
{
   private final SymbolType type;
   private final SymbolSign sign;
   private final int size;

   public SymbolSpec(SymbolType type, int size)
   {
      this(type, size, null);
   }

   public SymbolSpec(SymbolType type, int size, SymbolSign sign)
   {
      this.type = type;
      this.sign = sign;
      this.size = size;
   }

   public SymbolType getType()
   {
      return this.type;
   }

   public SymbolSign getSign()
   {
      return this.sign;
   }

   public int getSize()
   {
      return this.size;
   }
}
