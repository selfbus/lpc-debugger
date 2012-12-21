/*
 * Example program for P89LPC922 controller
 */

#include <P89LPC922.h>

#include "debug.h"

// Setup the debug variables
DEBUG_VARIABLES;

// This global variable is visible in the debugger
int count = 0;

void main()
{
   // Initialize the debugging
	DEBUG_SETUP;

	P0M1 = 0xff;
	P0M2 = 0xff;

	while (1)
	{
	   // Here happens the serial communication with the PC
		DEBUG_POINT;

		++count;
		if (count == 0)
			P0_1 = !P0_1;
	}
}

