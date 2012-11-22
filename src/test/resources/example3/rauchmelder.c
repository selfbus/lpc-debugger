/*
 *      __________  ________________  __  _______
 *     / ____/ __ \/ ____/ ____/ __ )/ / / / ___/
 *    / /_  / /_/ / __/ / __/ / __  / / / /\__ \
 *   / __/ / _, _/ /___/ /___/ /_/ / /_/ /___/ /
 *  /_/   /_/ |_/_____/_____/_____/\____//____/
 *
 *  Copyright (c) 2012 Andreas Krebs <kubi@krebsworld.de>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *
 *  1.00	erste Version mit Alarm und Störungsobjekt sowie zyklischem Senden
 *  1.01	Fernauslöseobjekt für lokalen Alarm hinzugefügt
 *
 */


#include <P89LPC922.h>
#include "../lib_LPC922/fb_lpc922.h"
#include "rm_app.h"




void main(void)
{
	unsigned char n;


	restart_hw();				// Hardware zuruecksetzen

	for (n=0;n<50;n++) {		// Warten bis Bus stabil, nach Busspannungswiederkehr
		TR0=0;					// Timer 0 anhalten
		TH0=eeprom[ADDRTAB+1];	// Timer 0 setzen mit phys. Adr. damit Geräte unterschiedlich beginnen zu senden
		TL0=eeprom[ADDRTAB+2];
		TF0=0;					// Überlauf-Flag zurücksetzen
		TR0=1;					// Timer 0 starten
		while(!TF0);
	}
	restart_app();				// Anwendungsspezifische Einstellungen zuruecksetzen


	do  {
		if(APPLICATION_RUN) {	// nur wenn run-mode gesetzt

			if (event) {				// wenn Rauchmelder etwas gesendet hat
				if (alarm != alarm_obj && !fernalarm) {	// wenn Alarm aber keine Fernauslösung vorliegt
					send_obj_value(0);					// Telegramm senden
					alarm_obj = alarm;					// Objektwert setzen
				}
				if (stoerung != stoerung_obj) {			// bei Störung
					send_obj_value(1);					// Telegramm senden
					stoerung_obj = stoerung;			// Objektwert setzen
				}
				event=0;	// Ereignismelder zurücksetzen
			}
		}

		if(tel_arrived) process_tel();		// empfangenes Telegramm abarbeiten

		if(RTCCON>=0x80) delay_timer();		// Realtime clock Ueberlauf


		// Abfrage Programmier-Taster
		TASTER=1;					// Pin als Eingang schalten um Taster abzufragen
		if(!TASTER) {				// Taster gedrückt
			for(n=0;n<100;n++) {}	// Entprell-Zeit
			while(!TASTER);			// warten bis Taster losgelassen
			status60^=0x81;			// Prog-Bit und Parity-Bit im system_state toggeln
		}
		TASTER=!(status60 & 0x01);	// LED entsprechend Prog-Bit schalten (low=LED an)
		for(n=0;n<100;n++) {}		// falls Hauptroutine keine Zeit verbraucht, der LED etwas Zeit geben, damit sie auch leuchten kann
  } while(1);
}
