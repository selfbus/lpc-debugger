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
 */

#ifndef FB_APP_RM
#define FB_APP_RM


#define A_ZYKLISCH 	0xF4
#define A_FAKTOR	0xF5
#define A_BASIS		0xF6
#define S_ZYKLISCH 	0xF7
#define S_FAKTOR	0xF8
#define S_BASIS		0xF9

#define PIN_ALARM		P0_1	// kommt 10mS low bei Alarm
#define PIN_STOERUNG	P0_0	// kommt 10mS low bei Störung
#define PIN_RESET		P0_2	// kommt bei Alarm und Störung mit, bei RESET aber nur alleine
#define PIN_DATA		P1_2	// DATA Pin der 3-pol. stiftleiste(innen), wird via 2k2 auf Masse gezogen bei Fernalarm
// 10k zw. 11/12 und 9/10

extern __bit event, alarm, stoerung, alarm_obj, stoerung_obj, fernalarm;
extern unsigned int timer, timer_alarm, timer_stoerung;

void write_value_req(void);		// Hauptroutine für Ausgänge schalten gemäß EIS 1 Protokoll (an/aus)
void read_value_req(void);
void send_value(unsigned char type, unsigned char objno, unsigned int sval);
void delay_timer(void);			// zählt alle 130ms die Variable Timer hoch und prüft Queue
void key(void) __interrupt (7);


#endif
