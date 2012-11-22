Damit der LPC Debugger funktioniert muss der SDCC mit --debug aufgerufen werden.
Das muss beim Compiler und beim Linker angegeben werden.

Weiters, wenn Eclipse verwendet wird, muss man vom SDCC Make auf den Eclipse eigenen
CDT Internal Builder umstellen, sonst fehlen in den Debug Informationen alle
Variablen und Funktionen. Das gilt zumindest für SDCC 3.1.x

Die nötigen Einstellungen nimmt man im Eclipse so vor:

Rechte Maus auf das Projekt, Properties, C/C++ Build, Settings
Bei SDCC Compiler beim Command --debug dazu schreiben
Bei SDCC Linker beim Command --debug dazu schreiben

Dann wieder links im Properties Fenster auf Tool Chain Editor gehen
(der nächste Menüpunkt unter Settings).

Dort die Option Tool Chain Editor auf CDT Internal Builder einstellen.
