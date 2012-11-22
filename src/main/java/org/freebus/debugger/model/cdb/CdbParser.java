package org.freebus.debugger.model.cdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parser for SDCC .cdb files.
 */
public class CdbParser
{
   private static final Logger LOGGER = LoggerFactory.getLogger(CdbParser.class);

   private final Map<String, Symbol> symbols = new TreeMap<String, Symbol>();
   private final BufferedReader reader;
   private String location = "<stream>";
   private int lineNo;
   private String module;

   public CdbParser(File file) throws FileNotFoundException
   {
      this(new FileInputStream(file));
      this.location = file.getName();
   }

   public CdbParser(InputStream in)
   {
      this(new BufferedReader(new InputStreamReader(in)));
   }

   public CdbParser(BufferedReader reader)
   {
      this.reader = reader;
   }

   public int getLineNumber()
   {
      return this.lineNo;
   }

   // ERROR //
   public void parse() throws IOException, ParseException
   {
      // Byte code:
      //   0: getstatic 27	org/freebus/debugger/model/cdb/CdbParser:LOGGER	Lorg/slf4j/Logger;
      //   3: ldc 88
      //   5: invokeinterface 90 2 0
      //   10: aload_0
      //   11: iconst_0
      //   12: putfield 81	org/freebus/debugger/model/cdb/CdbParser:lineNo	I
      //   15: aload_0
      //   16: getfield 73	org/freebus/debugger/model/cdb/CdbParser:symbols	Ljava/util/Map;
      //   19: invokeinterface 96 1 0
      //   24: aload_0
      //   25: aconst_null
      //   26: putfield 101	org/freebus/debugger/model/cdb/CdbParser:module	Ljava/lang/String;
      //   29: aload_0
      //   30: invokevirtual 103	org/freebus/debugger/model/cdb/CdbParser:readLine	()Ljava/lang/String;
      //   33: astore_1
      //   34: aload_1
      //   35: ifnonnull +6 -> 41
      //   38: goto +256 -> 294
      //   41: aload_1
      //   42: invokevirtual 106	java/lang/String:isEmpty	()Z
      //   45: ifeq +6 -> 51
      //   48: goto -19 -> 29
      //   51: aload_1
      //   52: ldc 112
      //   54: iconst_2
      //   55: invokevirtual 114	java/lang/String:split	(Ljava/lang/String;I)[Ljava/lang/String;
      //   58: astore_2
      //   59: aload_2
      //   60: arraylength
      //   61: iconst_2
      //   62: if_icmpeq +31 -> 93
      //   65: new 86	java/text/ParseException
      //   68: dup
      //   69: new 118	java/lang/StringBuilder
      //   72: dup
      //   73: ldc 120
      //   75: invokespecial 122	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   78: aload_1
      //   79: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   82: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   85: aload_0
      //   86: getfield 81	org/freebus/debugger/model/cdb/CdbParser:lineNo	I
      //   89: invokespecial 131	java/text/ParseException:<init>	(Ljava/lang/String;I)V
      //   92: athrow
      //   93: aload_2
      //   94: iconst_0
      //   95: aaload
      //   96: astore_3
      //   97: aload_3
      //   98: ldc 134
      //   100: invokevirtual 136	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   103: ifeq +13 -> 116
      //   106: aload_0
      //   107: aload_2
      //   108: iconst_1
      //   109: aaload
      //   110: invokespecial 140	org/freebus/debugger/model/cdb/CdbParser:parseModuleLine	(Ljava/lang/String;)V
      //   113: goto -84 -> 29
      //   116: aload_3
      //   117: ldc 143
      //   119: invokevirtual 136	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   122: ifeq +13 -> 135
      //   125: aload_0
      //   126: aload_2
      //   127: iconst_1
      //   128: aaload
      //   129: invokespecial 145	org/freebus/debugger/model/cdb/CdbParser:parseFunctionLine	(Ljava/lang/String;)V
      //   132: goto -103 -> 29
      //   135: aload_3
      //   136: ldc 148
      //   138: invokevirtual 136	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   141: ifeq +13 -> 154
      //   144: aload_0
      //   145: aload_2
      //   146: iconst_1
      //   147: aaload
      //   148: invokespecial 150	org/freebus/debugger/model/cdb/CdbParser:parseSymbolLine	(Ljava/lang/String;)V
      //   151: goto -122 -> 29
      //   154: aload_3
      //   155: ldc 153
      //   157: invokevirtual 136	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   160: ifeq +13 -> 173
      //   163: aload_0
      //   164: aload_2
      //   165: iconst_1
      //   166: aaload
      //   167: invokespecial 155	org/freebus/debugger/model/cdb/CdbParser:parseStructureLine	(Ljava/lang/String;)V
      //   170: goto -141 -> 29
      //   173: aload_3
      //   174: ldc 158
      //   176: invokevirtual 136	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   179: ifeq +13 -> 192
      //   182: aload_0
      //   183: aload_2
      //   184: iconst_1
      //   185: aaload
      //   186: invokespecial 160	org/freebus/debugger/model/cdb/CdbParser:parseLinkerLine	(Ljava/lang/String;)V
      //   189: goto -160 -> 29
      //   192: getstatic 27	org/freebus/debugger/model/cdb/CdbParser:LOGGER	Lorg/slf4j/Logger;
      //   195: ldc 163
      //   197: aload_0
      //   198: getfield 81	org/freebus/debugger/model/cdb/CdbParser:lineNo	I
      //   201: invokestatic 165	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   204: aload_3
      //   205: invokeinterface 171 4 0
      //   210: goto -181 -> 29
      //   213: astore_1
      //   214: getstatic 27	org/freebus/debugger/model/cdb/CdbParser:LOGGER	Lorg/slf4j/Logger;
      //   217: new 118	java/lang/StringBuilder
      //   220: dup
      //   221: aload_1
      //   222: invokevirtual 175	java/lang/Object:getClass	()Ljava/lang/Class;
      //   225: invokevirtual 179	java/lang/Class:getSimpleName	()Ljava/lang/String;
      //   228: invokestatic 184	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
      //   231: invokespecial 122	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   234: ldc 187
      //   236: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   239: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   242: new 118	java/lang/StringBuilder
      //   245: dup
      //   246: aload_0
      //   247: getfield 49	org/freebus/debugger/model/cdb/CdbParser:location	Ljava/lang/String;
      //   250: invokestatic 184	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
      //   253: invokespecial 122	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   256: ldc 189
      //   258: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   261: aload_0
      //   262: getfield 81	org/freebus/debugger/model/cdb/CdbParser:lineNo	I
      //   265: invokevirtual 191	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   268: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   271: aload_1
      //   272: invokevirtual 194	java/lang/Exception:getMessage	()Ljava/lang/String;
      //   275: invokeinterface 199 4 0
      //   280: aload_1
      //   281: athrow
      //   282: astore 4
      //   284: aload_0
      //   285: getfield 77	org/freebus/debugger/model/cdb/CdbParser:reader	Ljava/io/BufferedReader;
      //   288: invokevirtual 202	java/io/BufferedReader:close	()V
      //   291: aload 4
      //   293: athrow
      //   294: aload_0
      //   295: getfield 77	org/freebus/debugger/model/cdb/CdbParser:reader	Ljava/io/BufferedReader;
      //   298: invokevirtual 202	java/io/BufferedReader:close	()V
      //   301: return
      //
      // Exception table:
      //   from	to	target	type
      //   29	213	213	java/lang/Exception
      //   29	282	282	finally
   }

   public Map<String, Symbol> getSymbols()
   {
      return this.symbols;
   }

   private void parseModuleLine(String str)
   {
      this.module = str;
   }

   private void parseFunctionLine(String str)
   {
   }

   private void parseSymbolLine(String str)
   {
      String[] parts = str.split("\\$", 4);
      Validate.isTrue(parts.length == 4);

      String type = parts[0];
      String name = parts[1];

      String rest = parts[3];

      if ("G".equals(type))
      {
         int begParen = rest.indexOf('(');
         int endParen = rest.lastIndexOf(')');
         Validate.isTrue((begParen > 0) && (endParen > 0));

         String[] restParts = rest.substring(endParen + 2).split(",", 4);
         Validate.isTrue(restParts.length >= 3);

         String typeRecord = rest.substring(begParen + 1, endParen);
         SymbolSpec symbolSpec = parseSymbolSpec(typeRecord);

         AddressSpace addrSpace = AddressSpace.valueOfType(restParts[0]);

         this.symbols.put(name, new Symbol(name, symbolSpec, addrSpace, this.module));
      }
   }

   private void parseStructureLine(String str)
   {
   }

   private void parseLinkerLine(String str)
   {
      String[] parts = str.split("[\\$:]");
      Validate.isTrue(parts.length >= 4);

      String type = parts[0];
      int address = Integer.parseInt(parts[(parts.length - 1)], 16);

      if (type.charAt(0) == 'G')
      {
         String name = parts[1];

         Symbol sym = (Symbol) this.symbols.get(name);
         if (sym == null)
         {
            LOGGER.warn("Ignoring unresolved linker symbol: {}", name);
            return;
         }

         sym.setAddress(address);
      }
   }

   protected String readLine() throws IOException
   {
      this.lineNo += 1;
      return this.reader.readLine();
   }

   SymbolSpec parseSymbolSpec(String str)
   {
      Validate.isTrue(str.charAt(0) == '{');
      String[] parts = str.substring(1).split("[}:]");
      Validate.isTrue(parts.length >= 2);

      int size = Integer.parseInt(parts[0]);
      SymbolType type = SymbolType.valueOfType(parts[1].substring(0, 2));

      if (SymbolType.ARRAY.equals(type))
      {
         String[] countRest = parts[1].substring(2).split(",", 2);
         int count = Integer.parseInt(countRest[0]);
         type = SymbolType.valueOfType(countRest[1].substring(0, 2));

         return new ArraySymbolSpec(type, size, count);
      }
      if (SymbolType.BIT_FIELD.equals(type))
      {
         String[] countRest = parts[1].substring(2).split(",", 2);
         String[] bounds = countRest[0].split("\\$");
         int count = Integer.parseInt(bounds[1]);

         return new ArraySymbolSpec(type, size, count);
      }
      if (SymbolType.STRUCTURE.equals(type))
      {
         String name = parts[1].substring(2);
         return new StructureSymbolSpec(name, size);
      }

      SymbolSign sign = parts.length >= 3 ? SymbolSign.valueOfType(parts[2].charAt(0)) : null;
      return new SymbolSpec(type, size, sign);
   }
}
