package selfbus.debugger.model.cdb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import org.junit.Test;

import selfbus.debugger.model.cdb.CdbParser;
import selfbus.debugger.model.cdb.Symbol;

public class TestCdbParser
{
   @Test
   public void testExample1() throws IOException, ParseException
   {
      File file = new File(getClass().getClassLoader().getResource("example1/out.cdb").getPath());
      assertNotNull(file);
      
      CdbParser parser = new CdbParser(file);
      parser.parse();

      Map<String, Symbol> syms = parser.getSymbols();
      assertNotNull(syms);
      assertTrue(!syms.isEmpty());

      Symbol sym = syms.get("portbuffer");
      assertNotNull(sym);
   }
}
