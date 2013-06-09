package selfbus.debugger.model.cdb;

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
   private int lineNo, numUnresolved;
   private String module;

   /**
    * Create a SDCC .cdb file parser
    * 
    * @param file - the file to parse
    */
   public CdbParser(File file) throws FileNotFoundException
   {
      this(new FileInputStream(file));
      this.location = file.getName();
   }

   /**
    * Create a SDCC .cdb file parser
    * 
    * @param in - an input stream of the file to parse
    */
   public CdbParser(InputStream in)
   {
      this(new BufferedReader(new InputStreamReader(in)));
   }

   /**
    * Create a SDCC .cdb file parser
    * 
    * @param reader - a reader pointing at the file to parse
    */
   public CdbParser(BufferedReader reader)
   {
      this.reader = reader;
   }

   /**
    * @return The current line number of the parser.
    */
   public int getLineNumber()
   {
      return lineNo;
   }

   /**
    * @return The parsed symbols. Key of the returned map is the name of the symbol, value the
    *         symbol.
    */
   public Map<String, Symbol> getSymbols()
   {
      return symbols;
   }

   /**
    * @return The number of unresolved linker symbols. Valid after {@link #parse()}.
    */
   public int getNumUnresolved()
   {
      return numUnresolved;
   }

   /**
    * Parse the file
    * 
    * @throws IOException in case of read errors
    * @throws ParseException in case of file format errors
    */
   public void parse() throws IOException, ParseException
   {
      lineNo = -1;
      numUnresolved = 0;

      while (true)
      {
         String line = readLine();
         if (line == null)
            break;

         String[] parts = split(line, ":", 2, 2);
         String type = parts[0].toUpperCase();
         if (type.equals("M"))
         {
            parseModuleRecord(parts[1]);
         }
         else if (type.equals("F"))
         {
            parseFunctionRecord(parts[1]);
         }
         else if (type.equals("S"))
         {
            parseSymbolRecord(parts[1]);
         }
         else if (type.equals("T"))
         {
            parseTypeRecord(parts[1]);
         }
         else if (type.equals("L"))
         {
            parseLinkerRecord(parts[1]);
         }
      }

      reader.close();
   }

   /**
    * Split the string with the regular expression. Throws an exception if the resuting string does
    * not have at least minParts parts.
    * 
    * @param str - the string to split
    * @param pattern - the regexp pattern to use for splitting
    * @param minParts - the minimum number of parts
    * @param maxParts - the maximum number of parts that will be created
    * 
    * @param The splitted string.
    * @throws ParseException if the splitted string does not have at least minParts parts after
    *            splitting.
    */
   private String[] split(String str, String pattern, int minParts, int maxParts) throws ParseException
   {
      String[] result = str.split(pattern, maxParts);
      if (result.length < minParts)
         throw new ParseException(location + " line " + lineNo + ": invalid line format", lineNo);
      return result;
   }

   /**
    * Parse a module record. The format is:
    * 
    * <pre>
    * M:[fileName]
    * </pre>
    * 
    * @param str - the string to parse
    */
   private void parseModuleRecord(String str)
   {
      this.module = str;
   }

   /**
    * Parse a function record. The format is:
    * 
    * <pre>
    * { G | F[Filename] | L { [function] | "-null-"}}
    * $[Name]
    * $[Level]
    * $[Block]
    * ([TypeRecord])
    * ,[AddressSpace]
    * ,[OnStack]
    * ,[Stack]
    * ,[Interrupt]
    * ,[Interrupt Num]
    * ,[Register Bank]
    * </pre>
    * 
    * @param str - the string to parse
    */
   private void parseFunctionRecord(String str)
   {
   }

   /**
    * Parse a symbol record. The format is (in one line):
    * 
    * <pre>
    * { G | F[Filename] | L { [function] | "-null-" }}
    * $[Name]
    * $[Level]
    * $[Block]
    * ([TypeRecord])
    * ,[AddressSpace]
    * ,[OnStack]
    * ,[Stack]
    * ,[[Reg],{[Reg],}]
    * </pre>
    * 
    * @param str - the string to parse
    */
   private void parseSymbolRecord(String str)
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

         LOGGER.debug("Found symbol {}", name);
         symbols.put(name, new Symbol(name, symbolSpec, addrSpace, module));
      }
   }

   /**
    * Parse a type / structure record. The format is (in one line):
    * 
    * <pre>
    * F[filename]
    * $[name]
    * [[typeMember]{[typeMember]}
    * </pre>
    * 
    * Type members have the format:
    * 
    * <pre>
    * ({[offset]}[symbolRecord])
    * </pre>
    * 
    * @param str - the string to parse
    */
   private void parseTypeRecord(String str)
   {
   }

   /**
    * Parse a linker record. The format is (in one line):
    * 
    * <pre>
    * { G | F[filename] | L[function] | XG | XF[filename] | XL[function] }
    * $[name]
    * $[level]
    * $[block]
    * :[address]
    * </pre>
    * 
    * or a linker ASM line record:
    * 
    * <pre>
    * A
    * $[filename]
    * $[line]
    * $[endAddress]
    * </pre>
    * 
    * or a linker C-line record:
    * 
    * <pre>
    * C
    * $[filename]
    * $[level]
    * $[block]
    * $[endAddress]
    * </pre>
    * 
    * @param str - the string to parse
    */
   private void parseLinkerRecord(String str)
   {
      String[] parts = str.split("[\\$:]");
      Validate.isTrue(parts.length >= 4);

      String type = parts[0];
      int address = Integer.parseInt(parts[(parts.length - 1)], 16);

      if (type.charAt(0) == 'G')
      {
         String name = parts[1];

         Symbol sym = (Symbol) symbols.get(name);
         if (sym == null)
         {
            LOGGER.warn("Ignoring unresolved linker symbol: {}", name);
            numUnresolved++;
            return;
         }

         sym.setAddress(address);
      }
   }

   /**
    * Read the next line.
    * 
    * @return The read line
    * @throws IOException in case of read errors.
    */
   protected String readLine() throws IOException
   {
      this.lineNo += 1;
      return this.reader.readLine();
   }

   /**
    * Parse a symbol specification.
    *
    * @param str - the string to parse
    * @return The symbol specification
    */
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
