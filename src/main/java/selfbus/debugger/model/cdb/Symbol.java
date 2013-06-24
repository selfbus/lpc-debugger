package selfbus.debugger.model.cdb;

/**
 * A symbol.
 */
public class Symbol
{
   private final String name;
   private final String module;
   private final SymbolSpec spec;
   private final AddressSpace addrSpace;
   private int address = -1;

   /**
    * Create a symbol.
    *
    * @param name - the name of the symbol
    * @param spec - the symbol specification
    * @param addrSpace - the address space of the symbol
    * @param module - the name of the module
    */
   public Symbol(String name, SymbolSpec spec, AddressSpace addrSpace, String module)
   {
      this.name = name;
      this.spec = spec;
      this.addrSpace = addrSpace;
      this.module = module;
   }

   /**
    * @return The name of the symbol
    */
   public String getName()
   {
      return name;
   }

   /**
    * @return The symbol specification
    */
   public SymbolSpec getSymbolSpec()
   {
      return spec;
   }

   /**
    * @return The address space type
    */
   public AddressSpace getAddressSpace()
   {
      return addrSpace;
   }

   /**
    * @return The name of the module
    */
   public String getModule()
   {
      return module;
   }

   /**
    * @return The symbol type. This is a shortcut for <code>getSymbolSpec().getType()</code>.
    */
   public SymbolType getType()
   {
      return spec == null ? null : spec.getType();
   }

   /**
    * @return The address
    */
   public int getAddress()
   {
      return address;
   }

   /**
    * Set the address.
    *
    * @param address - the address to set
    */
   public void setAddress(int address)
   {
      this.address = address;
   }
}
