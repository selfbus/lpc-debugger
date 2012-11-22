package org.freebus.debugger.model.map;

import java.util.List;
import java.util.Vector;
import org.freebus.debugger.model.Variable;

public final class MapUtils
{
   public static List<Variable> createVariablesForArea(MapArea area)
   {
      List<MapAreaLocation> locations = area.getLocations();
      List<Variable> result = new Vector<Variable>(locations.size());

      for (int i = 0; i < locations.size(); i++)
      {
         MapAreaLocation location = (MapAreaLocation) locations.get(i);
         int nextAddr;
         if (i < locations.size() - 1)
            nextAddr = ((MapAreaLocation) locations.get(i + 1)).getAddress();
         else nextAddr = area.getAddress() + area.getSize();

         int size = nextAddr - location.getAddress();
         result.add(new Variable(location, size < 0 ? 0 : size));
      }

      return result;
   }
}
