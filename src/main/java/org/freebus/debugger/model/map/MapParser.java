package org.freebus.debugger.model.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parser for SDCC .map files.
 */
public class MapParser
{
   private static final Logger LOGGER = LoggerFactory.getLogger(MapParser.class);

   private final Map<String, MapArea> areas = new TreeMap<String, MapArea>();
   private final BufferedReader in;
   private int lineNo;

   public MapParser(InputStream in)
   {
      this(new BufferedReader(new InputStreamReader(in)));
   }

   public MapParser(BufferedReader in)
   {
      this.in = in;
   }

   public MapArea getArea(String name)
   {
      return (MapArea) this.areas.get(name);
   }

   public Map<String, MapArea> getAreas()
   {
      return this.areas;
   }

   public void parse() throws IOException
   {
      LOGGER.info("Parsing map file");

      List<MapAreaLocation> locations = null;
      MapArea area = null;
      String lastAreaName = null;

      this.areas.clear();
      this.lineNo = 0;

      if (!skipToNewPage())
      {
         return;
      }

      do
      {
         if (skipToLine("--------------") == null)
         {
            break;
         }
         String header = readLine();
         if (header == null)
         {
            break;
         }
         Validate.isTrue((header != null) && (header.length() > 56), "Header line too short \"" + header
            + "\" at line " + this.lineNo, new Object[0]);

         String areaName = header.substring(0, 32).trim().replaceAll("^\\.\\s+", "");
         int areaAddr = Integer.parseInt(header.substring(34, 44).trim(), 16);
         int areaSize = Integer.parseInt(header.substring(46, 56).trim(), 16);

         if (!areaName.equals(lastAreaName))
         {
            LOGGER.info("Area " + areaName + " at " + areaAddr + " size " + areaSize + " bytes");
            area = new MapArea(areaName, areaAddr, areaSize);
            locations = new ArrayList<MapAreaLocation>();
            area.setLocations(locations);
            this.areas.put(areaName, area);
            lastAreaName = areaName;
         }
      }
      while (readArea(locations));
   }

   boolean readArea(List<MapAreaLocation> locations) throws IOException
   {
      if (skipToLine("      -----") == null)
      {
         return false;
      }
      while (true)
      {
         String line = readLine();
         if (line == null)
            return false;
         if ((line.startsWith("\f")) || (line.trim().isEmpty()))
         {
            return true;
         }
         Validate.isTrue(line.length() > 15, "Line too short \"" + line + "\" at line " + this.lineNo, new Object[0]);

         int address = Integer.parseInt(line.substring(5, 13).trim(), 16);
         String name = line.substring(14, 45).trim();
         String module = line.length() > 46 ? line.substring(46).trim() : null;

         locations.add(new MapAreaLocation(name, address, module));
      }
   }

   String readLine() throws IOException
   {
      this.lineNo += 1;
      return this.in.readLine();
   }

   boolean skipToNewPage() throws IOException
   {
      String line;
      do
      {
         line = readLine();
         if (line == null)
            return false;
      }
      while (!line.startsWith("\f"));
      return true;
   }

   String skipToLine(String startsWith) throws IOException
   {
      String line;
      do
      {
         line = readLine();
         if (line == null)
            return null;
      }
      while (!line.startsWith(startsWith));
      return line;
   }
}
