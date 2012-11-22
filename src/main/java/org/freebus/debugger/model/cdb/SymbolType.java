package org.freebus.debugger.model.cdb;

import org.slf4j.LoggerFactory;

/**
 * A symbol type.
 */
public enum SymbolType
{
   ARRAY("DA"),

   FUNCTION("DF"),

   POINTER("DG"),

   CODE_POINTER("DC"),

   RAM_POINTER_EXT("DX"),

   RAM_POINTER_INT("DD"),

   PAGED_POINTER("DP"),

   UPPER_POINTER("DI"),

   LONG("SL"),

   INT("SI"),

   CHAR("SC"),

   SHORT("SS"),

   VOID("SV"),

   FLOAT("SF"),

   STRUCTURE("ST"),

   SBIT("SX"),

   BIT_FIELD("SB");

   public final String type;

   public static SymbolType valueOfType(String cdbType)
   {
      for (SymbolType spc : values())
      {
         if (spc.type.equals(cdbType))
            return spc;
      }
      LoggerFactory.getLogger(SymbolType.class).warn("Unknown symbol type encountered: {}", cdbType);
      return null;
   }

   private SymbolType(String type)
   {
      this.type = type;
   }
}
