package selfbus.debugger.model.cdb;

/**
 * A symbol specification.
 */
public class Symbol
{
   private final String name;
   private final String module;
   private final SymbolSpec spec;
   private final AddressSpace addrSpace;
   private int address = -1;

   public Symbol(String name, SymbolSpec spec, AddressSpace addrSpace, String module)
   {
      this.name = name;
      this.spec = spec;
      this.addrSpace = addrSpace;
      this.module = module;
   }

   public String getName()
   {
      return this.name;
   }

   public SymbolSpec getSymbolSpec()
   {
      return this.spec;
   }

   public AddressSpace getAddressSpace()
   {
      return this.addrSpace;
   }

   public String getModule()
   {
      return this.module;
   }

   public SymbolType getType()
   {
      return this.spec == null ? null : this.spec.getType();
   }

   public int getAddress()
   {
      return this.address;
   }

   public void setAddress(int address)
   {
      this.address = address;
   }
}
