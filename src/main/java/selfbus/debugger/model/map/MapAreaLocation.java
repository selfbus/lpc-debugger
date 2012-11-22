package selfbus.debugger.model.map;

public class MapAreaLocation
{
   private final int address;
   private final String name;
   private final String module;

   public MapAreaLocation(String name, int address, String module)
   {
      this.address = address;
      this.name = name;
      this.module = module;
   }

   public int getAddress()
   {
      return this.address;
   }

   public String getName()
   {
      return this.name;
   }

   public String getModule()
   {
      return this.module;
   }
}
