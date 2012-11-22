package selfbus.debugger.model;

import java.util.Arrays;

import selfbus.debugger.model.cdb.SymbolSpec;
import selfbus.debugger.model.cdb.SymbolType;
import selfbus.debugger.model.map.MapAreaLocation;

/**
 * A variable.
 */
public class Variable extends MapAreaLocation implements Comparable<Variable>
{
   private byte[] value, prevValue;
   private SymbolSpec spec;
   private boolean modified;

   public Variable(String name, int address, int size, String module)
   {
      super(name, address, module);
      this.value = new byte[size];
   }

   public Variable(MapAreaLocation location, int size)
   {
      super(location.getName(), location.getAddress(), location.getModule());
      this.value = new byte[size];
   }

   /**
    * @return The symbol specification.
    */
   public SymbolSpec getSpec()
   {
      return this.spec;
   }

   /**
    * Set the symbol specification.
    * 
    * @param spec - the symbol specification.
    */
   public void setSpec(SymbolSpec spec)
   {
      this.spec = spec;
   }

   /**
    * Get the symbol type. This is a shortcut for <code>obj.getSpec().getType()</code>.
    * 
    * @return The symbol type.
    */
   public SymbolType getType()
   {
      return spec == null ? null : spec.getType();
   }

   /**
    * @return The size of the variable in bytes.
    */
   public int size()
   {
      return value.length;
   }

   /**
    * Set the value. Moves the current value to the previous value.
    * 
    * @param value - the value to set.
    */
   public void setValue(byte[] value)
   {
      this.prevValue = this.value;
      this.value = value;

      modified = prevValue != null && !Arrays.equals(value, prevValue);
   }

   /**
    * @return The value of the variable.
    */
   public byte[] getValue()
   {
      return value;
   }

   /**
    * @return The previous value of the variable, or null if the variable's value was never changed.
    */
   public byte[] getPrevValue()
   {
      return prevValue;
   }

   /**
    * Test if the current and the previous value differ.
    * 
    * @return True if current and previous value are different, false if they are the same or the
    *         previous value is null.
    */
   public boolean isModified()
   {
      return modified;
   }

   /**
    * @return True if the variable was never set (the previous value is null).
    */
   public boolean isUnused()
   {
      return prevValue == null;
   }

   /**
    * Mark the variable as unused by removing the previous value.
    */
   public void markUnused()
   {
      prevValue = null;
      modified = false;
   }

   /**
    * {@inheritDoc}
    */
   public int compareTo(Variable o)
   {
      return getName().compareTo(o.getName());
   }
}
