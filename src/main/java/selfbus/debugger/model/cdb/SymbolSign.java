package selfbus.debugger.model.cdb;

import org.slf4j.LoggerFactory;

/**
 * The sign of a symbol.
 */
public enum SymbolSign
{
   SIGNED('S'),

   UNSIGNED('U');

   public final char type;

   public static SymbolSign valueOfType(char cdbType)
   {
      for (SymbolSign spc : values())
      {
         if (spc.type == cdbType)
            return spc;
      }
      LoggerFactory.getLogger(SymbolSign.class).warn("Unknown symbol sign encountered: {}", Character.valueOf(cdbType));
      return null;
   }

   private SymbolSign(char type)
   {
      this.type = type;
   }
}
