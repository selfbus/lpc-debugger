package selfbus.debugger.model.cdb;

import org.slf4j.LoggerFactory;

/**
 * An address space type.
 */
public enum AddressSpace
{
   STACK_EXT("A"),

   STACK_INT("B"),

   CODE("C"),

   CODE_STATIC("D"),

   RAM_INT_LOW("E"),

   RAM_EXT("F"),

   RAM_INT("G"),

   BIT("H"),

   SFR("I"),

   SBIT("J"),

   REGISTER("R"),

   UNDEF("Z");

   public final String type;

   public static AddressSpace valueOfType(String cdbType)
   {
      for (AddressSpace spc : values())
      {
         if (spc.type.equals(cdbType))
            return spc;
      }
      LoggerFactory.getLogger(AddressSpace.class).warn("Unknown address-space type encountered: {}", cdbType);
      return null;
   }

   private AddressSpace(String type)
   {
      this.type = type;
   }
}
