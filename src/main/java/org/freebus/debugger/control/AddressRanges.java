package org.freebus.debugger.control;

/**
 * Interface for address ranges of the debugged device.
 */
public abstract interface AddressRanges
{
   /**
    * @return The base address for bit fields.
    */
   public abstract int getBitFieldAddr();
}
