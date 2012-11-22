package selfbus.debugger.control;

/**
 * Address ranges of a LPC9xx microcontroller.
 */
public class LpcAddressRanges implements AddressRanges
{
   public int getBitFieldAddr()
   {
      return 0x20;
   }
}
