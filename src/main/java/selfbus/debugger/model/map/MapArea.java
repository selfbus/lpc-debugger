package selfbus.debugger.model.map;

import java.util.List;

public class MapArea
{
   private final String name;
   private final int address;
   private final int size;
   private List<MapAreaLocation> locations;

   public MapArea(String name, int address, int size)
   {
      this.name = name;
      this.address = address;
      this.size = size;
   }

   public List<MapAreaLocation> getLocations()
   {
      return this.locations;
   }

   public void setLocations(List<MapAreaLocation> locations)
   {
      this.locations = locations;
   }

   public String getName()
   {
      return this.name;
   }

   public int getAddress()
   {
      return this.address;
   }

   public int getSize()
   {
      return this.size;
   }
}
