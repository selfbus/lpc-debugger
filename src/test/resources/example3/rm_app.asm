;--------------------------------------------------------
; File Created by SDCC : free open source ANSI-C Compiler
; Version 2.9.0 #5416 (Feb  3 2010) (UNIX)
; This file was generated Thu Nov 15 12:07:56 2012
;--------------------------------------------------------
	.module rm_app
	.optsdcc -mmcs51 --model-small
	
;--------------------------------------------------------
; Public variables in this module
;--------------------------------------------------------
	.globl _P3_1
	.globl _P3_0
	.globl _P1_7
	.globl _P1_6
	.globl _P1_5
	.globl _P1_4
	.globl _P1_3
	.globl _P1_2
	.globl _P1_1
	.globl _P1_0
	.globl _P0_7
	.globl _P0_6
	.globl _P0_5
	.globl _P0_4
	.globl _P0_3
	.globl _P0_2
	.globl _P0_1
	.globl _P0_0
	.globl _I2CON_0
	.globl _I2CON_2
	.globl _I2CON_3
	.globl _I2CON_4
	.globl _I2CON_5
	.globl _I2CON_6
	.globl _SCON_7
	.globl _SCON_6
	.globl _SCON_5
	.globl _SCON_4
	.globl _SCON_3
	.globl _SCON_2
	.globl _SCON_1
	.globl _SCON_0
	.globl _IP0_0
	.globl _IP0_1
	.globl _IP0_2
	.globl _IP0_3
	.globl _IP0_4
	.globl _IP0_5
	.globl _IP0_6
	.globl _IP1_0
	.globl _IP1_1
	.globl _IP1_2
	.globl _IP1_6
	.globl _IEN1_0
	.globl _IEN1_1
	.globl _IEN1_2
	.globl _IEN0_0
	.globl _IEN0_1
	.globl _IEN0_2
	.globl _IEN0_3
	.globl _IEN0_4
	.globl _IEN0_5
	.globl _IEN0_6
	.globl _IEN0_7
	.globl _TCON_0
	.globl _TCON_1
	.globl _TCON_2
	.globl _TCON_3
	.globl _TCON_4
	.globl _TCON_5
	.globl _TCON_6
	.globl _TCON_7
	.globl _PSW_7
	.globl _PSW_6
	.globl _PSW_5
	.globl _PSW_4
	.globl _PSW_3
	.globl _PSW_2
	.globl _PSW_1
	.globl _PSW_0
	.globl _IEN1
	.globl _IP0H
	.globl _WFEED2
	.globl _WFEED1
	.globl _WDL
	.globl _WDCON
	.globl _TRIM
	.globl _TAMOD
	.globl _SSTAT
	.globl _RTCL
	.globl _RTCH
	.globl _RTCCON
	.globl _RSTSRC
	.globl _PT0AD
	.globl _PCONA
	.globl _P3M2
	.globl _P3M1
	.globl _P1M2
	.globl _P1M1
	.globl _P0M2
	.globl _P0M1
	.globl _KBPATN
	.globl _KBMASK
	.globl _KBCON
	.globl _IP1H
	.globl _IP1
	.globl _I2STAT
	.globl _I2SCLL
	.globl _I2SCLH
	.globl _I2DAT
	.globl _I2CON
	.globl _I2ADR
	.globl _FMDATA
	.globl _FMCON
	.globl _FMADRL
	.globl _FMADRH
	.globl _DIVM
	.globl _CMP2
	.globl _CMP1
	.globl _BRGCON
	.globl _BRGR1
	.globl _BRGR0
	.globl _SADEN
	.globl _SADDR
	.globl _AUXR1
	.globl _SBUF
	.globl _SCON
	.globl _IP0
	.globl _IEN0
	.globl _TH1
	.globl _TH0
	.globl _TL1
	.globl _TL0
	.globl _TMOD
	.globl _TCON
	.globl _PCON
	.globl _DPH
	.globl _DPL
	.globl _SP
	.globl _B
	.globl _ACC
	.globl _PSW
	.globl _P3
	.globl _P1
	.globl _P0
	.globl _fernalarm
	.globl _stoerung_obj
	.globl _alarm_obj
	.globl _stoerung
	.globl _alarm
	.globl _event
	.globl _timer_stoerung
	.globl _timer_alarm
	.globl _timer
	.globl _write_value_req
	.globl _read_value_req
	.globl _read_obj_value
	.globl _key
	.globl _delay_timer
	.globl _restart_app
;--------------------------------------------------------
; special function registers
;--------------------------------------------------------
	.area RSEG    (DATA)
G$P0$0$0 == 0x0080
_P0	=	0x0080
G$P1$0$0 == 0x0090
_P1	=	0x0090
G$P3$0$0 == 0x00b0
_P3	=	0x00b0
G$PSW$0$0 == 0x00d0
_PSW	=	0x00d0
G$ACC$0$0 == 0x00e0
_ACC	=	0x00e0
G$B$0$0 == 0x00f0
_B	=	0x00f0
G$SP$0$0 == 0x0081
_SP	=	0x0081
G$DPL$0$0 == 0x0082
_DPL	=	0x0082
G$DPH$0$0 == 0x0083
_DPH	=	0x0083
G$PCON$0$0 == 0x0087
_PCON	=	0x0087
G$TCON$0$0 == 0x0088
_TCON	=	0x0088
G$TMOD$0$0 == 0x0089
_TMOD	=	0x0089
G$TL0$0$0 == 0x008a
_TL0	=	0x008a
G$TL1$0$0 == 0x008b
_TL1	=	0x008b
G$TH0$0$0 == 0x008c
_TH0	=	0x008c
G$TH1$0$0 == 0x008d
_TH1	=	0x008d
G$IEN0$0$0 == 0x00a8
_IEN0	=	0x00a8
G$IP0$0$0 == 0x00b8
_IP0	=	0x00b8
G$SCON$0$0 == 0x0098
_SCON	=	0x0098
G$SBUF$0$0 == 0x0099
_SBUF	=	0x0099
G$AUXR1$0$0 == 0x00a2
_AUXR1	=	0x00a2
G$SADDR$0$0 == 0x00a9
_SADDR	=	0x00a9
G$SADEN$0$0 == 0x00b9
_SADEN	=	0x00b9
G$BRGR0$0$0 == 0x00be
_BRGR0	=	0x00be
G$BRGR1$0$0 == 0x00bf
_BRGR1	=	0x00bf
G$BRGCON$0$0 == 0x00bd
_BRGCON	=	0x00bd
G$CMP1$0$0 == 0x00ac
_CMP1	=	0x00ac
G$CMP2$0$0 == 0x00ad
_CMP2	=	0x00ad
G$DIVM$0$0 == 0x0095
_DIVM	=	0x0095
G$FMADRH$0$0 == 0x00e7
_FMADRH	=	0x00e7
G$FMADRL$0$0 == 0x00e6
_FMADRL	=	0x00e6
G$FMCON$0$0 == 0x00e4
_FMCON	=	0x00e4
G$FMDATA$0$0 == 0x00e5
_FMDATA	=	0x00e5
G$I2ADR$0$0 == 0x00db
_I2ADR	=	0x00db
G$I2CON$0$0 == 0x00d8
_I2CON	=	0x00d8
G$I2DAT$0$0 == 0x00da
_I2DAT	=	0x00da
G$I2SCLH$0$0 == 0x00dd
_I2SCLH	=	0x00dd
G$I2SCLL$0$0 == 0x00dc
_I2SCLL	=	0x00dc
G$I2STAT$0$0 == 0x00d9
_I2STAT	=	0x00d9
G$IP1$0$0 == 0x00f8
_IP1	=	0x00f8
G$IP1H$0$0 == 0x00f7
_IP1H	=	0x00f7
G$KBCON$0$0 == 0x0094
_KBCON	=	0x0094
G$KBMASK$0$0 == 0x0086
_KBMASK	=	0x0086
G$KBPATN$0$0 == 0x0093
_KBPATN	=	0x0093
G$P0M1$0$0 == 0x0084
_P0M1	=	0x0084
G$P0M2$0$0 == 0x0085
_P0M2	=	0x0085
G$P1M1$0$0 == 0x0091
_P1M1	=	0x0091
G$P1M2$0$0 == 0x0092
_P1M2	=	0x0092
G$P3M1$0$0 == 0x00b1
_P3M1	=	0x00b1
G$P3M2$0$0 == 0x00b2
_P3M2	=	0x00b2
G$PCONA$0$0 == 0x00b5
_PCONA	=	0x00b5
G$PT0AD$0$0 == 0x00f6
_PT0AD	=	0x00f6
G$RSTSRC$0$0 == 0x00df
_RSTSRC	=	0x00df
G$RTCCON$0$0 == 0x00d1
_RTCCON	=	0x00d1
G$RTCH$0$0 == 0x00d2
_RTCH	=	0x00d2
G$RTCL$0$0 == 0x00d3
_RTCL	=	0x00d3
G$SSTAT$0$0 == 0x00ba
_SSTAT	=	0x00ba
G$TAMOD$0$0 == 0x008f
_TAMOD	=	0x008f
G$TRIM$0$0 == 0x0096
_TRIM	=	0x0096
G$WDCON$0$0 == 0x00a7
_WDCON	=	0x00a7
G$WDL$0$0 == 0x00c1
_WDL	=	0x00c1
G$WFEED1$0$0 == 0x00c2
_WFEED1	=	0x00c2
G$WFEED2$0$0 == 0x00c3
_WFEED2	=	0x00c3
G$IP0H$0$0 == 0x00b7
_IP0H	=	0x00b7
G$IEN1$0$0 == 0x00e8
_IEN1	=	0x00e8
;--------------------------------------------------------
; special function bits
;--------------------------------------------------------
	.area RSEG    (DATA)
G$PSW_0$0$0 == 0x00d0
_PSW_0	=	0x00d0
G$PSW_1$0$0 == 0x00d1
_PSW_1	=	0x00d1
G$PSW_2$0$0 == 0x00d2
_PSW_2	=	0x00d2
G$PSW_3$0$0 == 0x00d3
_PSW_3	=	0x00d3
G$PSW_4$0$0 == 0x00d4
_PSW_4	=	0x00d4
G$PSW_5$0$0 == 0x00d5
_PSW_5	=	0x00d5
G$PSW_6$0$0 == 0x00d6
_PSW_6	=	0x00d6
G$PSW_7$0$0 == 0x00d7
_PSW_7	=	0x00d7
G$TCON_7$0$0 == 0x008f
_TCON_7	=	0x008f
G$TCON_6$0$0 == 0x008e
_TCON_6	=	0x008e
G$TCON_5$0$0 == 0x008d
_TCON_5	=	0x008d
G$TCON_4$0$0 == 0x008c
_TCON_4	=	0x008c
G$TCON_3$0$0 == 0x008b
_TCON_3	=	0x008b
G$TCON_2$0$0 == 0x008a
_TCON_2	=	0x008a
G$TCON_1$0$0 == 0x0089
_TCON_1	=	0x0089
G$TCON_0$0$0 == 0x0088
_TCON_0	=	0x0088
G$IEN0_7$0$0 == 0x00af
_IEN0_7	=	0x00af
G$IEN0_6$0$0 == 0x00ae
_IEN0_6	=	0x00ae
G$IEN0_5$0$0 == 0x00ad
_IEN0_5	=	0x00ad
G$IEN0_4$0$0 == 0x00ac
_IEN0_4	=	0x00ac
G$IEN0_3$0$0 == 0x00ab
_IEN0_3	=	0x00ab
G$IEN0_2$0$0 == 0x00aa
_IEN0_2	=	0x00aa
G$IEN0_1$0$0 == 0x00a9
_IEN0_1	=	0x00a9
G$IEN0_0$0$0 == 0x00a8
_IEN0_0	=	0x00a8
G$IEN1_2$0$0 == 0x00ea
_IEN1_2	=	0x00ea
G$IEN1_1$0$0 == 0x00e9
_IEN1_1	=	0x00e9
G$IEN1_0$0$0 == 0x00e8
_IEN1_0	=	0x00e8
G$IP1_6$0$0 == 0x00fe
_IP1_6	=	0x00fe
G$IP1_2$0$0 == 0x00fa
_IP1_2	=	0x00fa
G$IP1_1$0$0 == 0x00f9
_IP1_1	=	0x00f9
G$IP1_0$0$0 == 0x00f8
_IP1_0	=	0x00f8
G$IP0_6$0$0 == 0x00be
_IP0_6	=	0x00be
G$IP0_5$0$0 == 0x00bd
_IP0_5	=	0x00bd
G$IP0_4$0$0 == 0x00bc
_IP0_4	=	0x00bc
G$IP0_3$0$0 == 0x00bb
_IP0_3	=	0x00bb
G$IP0_2$0$0 == 0x00ba
_IP0_2	=	0x00ba
G$IP0_1$0$0 == 0x00b9
_IP0_1	=	0x00b9
G$IP0_0$0$0 == 0x00b8
_IP0_0	=	0x00b8
G$SCON_0$0$0 == 0x0098
_SCON_0	=	0x0098
G$SCON_1$0$0 == 0x0099
_SCON_1	=	0x0099
G$SCON_2$0$0 == 0x009a
_SCON_2	=	0x009a
G$SCON_3$0$0 == 0x009b
_SCON_3	=	0x009b
G$SCON_4$0$0 == 0x009c
_SCON_4	=	0x009c
G$SCON_5$0$0 == 0x009d
_SCON_5	=	0x009d
G$SCON_6$0$0 == 0x009e
_SCON_6	=	0x009e
G$SCON_7$0$0 == 0x009f
_SCON_7	=	0x009f
G$I2CON_6$0$0 == 0x00de
_I2CON_6	=	0x00de
G$I2CON_5$0$0 == 0x00dd
_I2CON_5	=	0x00dd
G$I2CON_4$0$0 == 0x00dc
_I2CON_4	=	0x00dc
G$I2CON_3$0$0 == 0x00db
_I2CON_3	=	0x00db
G$I2CON_2$0$0 == 0x00da
_I2CON_2	=	0x00da
G$I2CON_0$0$0 == 0x00d8
_I2CON_0	=	0x00d8
G$P0_0$0$0 == 0x0080
_P0_0	=	0x0080
G$P0_1$0$0 == 0x0081
_P0_1	=	0x0081
G$P0_2$0$0 == 0x0082
_P0_2	=	0x0082
G$P0_3$0$0 == 0x0083
_P0_3	=	0x0083
G$P0_4$0$0 == 0x0084
_P0_4	=	0x0084
G$P0_5$0$0 == 0x0085
_P0_5	=	0x0085
G$P0_6$0$0 == 0x0086
_P0_6	=	0x0086
G$P0_7$0$0 == 0x0087
_P0_7	=	0x0087
G$P1_0$0$0 == 0x0090
_P1_0	=	0x0090
G$P1_1$0$0 == 0x0091
_P1_1	=	0x0091
G$P1_2$0$0 == 0x0092
_P1_2	=	0x0092
G$P1_3$0$0 == 0x0093
_P1_3	=	0x0093
G$P1_4$0$0 == 0x0094
_P1_4	=	0x0094
G$P1_5$0$0 == 0x0095
_P1_5	=	0x0095
G$P1_6$0$0 == 0x0096
_P1_6	=	0x0096
G$P1_7$0$0 == 0x0097
_P1_7	=	0x0097
G$P3_0$0$0 == 0x00b0
_P3_0	=	0x00b0
G$P3_1$0$0 == 0x00b1
_P3_1	=	0x00b1
;--------------------------------------------------------
; overlayable register banks
;--------------------------------------------------------
	.area REG_BANK_0	(REL,OVR,DATA)
	.ds 8
;--------------------------------------------------------
; internal ram data
;--------------------------------------------------------
	.area DSEG    (DATA)
G$timer$0$0==.
_timer::
	.ds 2
G$timer_alarm$0$0==.
_timer_alarm::
	.ds 2
G$timer_stoerung$0$0==.
_timer_stoerung::
	.ds 2
;--------------------------------------------------------
; overlayable items in internal ram 
;--------------------------------------------------------
	.area	OSEG    (OVR,DATA)
;--------------------------------------------------------
; indirectly addressable internal ram data
;--------------------------------------------------------
	.area ISEG    (DATA)
;--------------------------------------------------------
; absolute internal ram data
;--------------------------------------------------------
	.area IABS    (ABS,DATA)
	.area IABS    (ABS,DATA)
;--------------------------------------------------------
; bit data
;--------------------------------------------------------
	.area BSEG    (BIT)
G$event$0$0==.
_event::
	.ds 1
G$alarm$0$0==.
_alarm::
	.ds 1
G$stoerung$0$0==.
_stoerung::
	.ds 1
G$alarm_obj$0$0==.
_alarm_obj::
	.ds 1
G$stoerung_obj$0$0==.
_stoerung_obj::
	.ds 1
G$fernalarm$0$0==.
_fernalarm::
	.ds 1
;--------------------------------------------------------
; paged external ram data
;--------------------------------------------------------
	.area PSEG    (PAG,XDATA)
;--------------------------------------------------------
; external ram data
;--------------------------------------------------------
	.area XSEG    (XDATA)
;--------------------------------------------------------
; absolute external ram data
;--------------------------------------------------------
	.area XABS    (ABS,XDATA)
;--------------------------------------------------------
; external initialized ram data
;--------------------------------------------------------
	.area XISEG   (XDATA)
	.area HOME    (CODE)
	.area GSINIT0 (CODE)
	.area GSINIT1 (CODE)
	.area GSINIT2 (CODE)
	.area GSINIT3 (CODE)
	.area GSINIT4 (CODE)
	.area GSINIT5 (CODE)
	.area GSINIT  (CODE)
	.area GSFINAL (CODE)
	.area CSEG    (CODE)
;--------------------------------------------------------
; global & static initialisations
;--------------------------------------------------------
	.area HOME    (CODE)
	.area GSINIT  (CODE)
	.area GSFINAL (CODE)
	.area GSINIT  (CODE)
;--------------------------------------------------------
; Home
;--------------------------------------------------------
	.area HOME    (CODE)
	.area HOME    (CODE)
;--------------------------------------------------------
; code
;--------------------------------------------------------
	.area CSEG    (CODE)
;------------------------------------------------------------
;Allocation info for local variables in function 'write_value_req'
;------------------------------------------------------------
;objno                     Allocated to registers r2 
;------------------------------------------------------------
	G$write_value_req$0$0 ==.
	C$rm_app.c$28$0$0 ==.
;	rm_app.c:28: void write_value_req(void)	// Empfangenes write_value_request Telegramm verarbeiten
;	-----------------------------------------
;	 function write_value_req
;	-----------------------------------------
_write_value_req:
	ar2 = 0x02
	ar3 = 0x03
	ar4 = 0x04
	ar5 = 0x05
	ar6 = 0x06
	ar7 = 0x07
	ar0 = 0x00
	ar1 = 0x01
	C$rm_app.c$32$1$1 ==.
;	rm_app.c:32: objno=find_first_objno(telegramm[3],telegramm[4]);
	mov	dpl,(_telegramm + 0x0003)
	mov	_find_first_objno_PARM_2,(_telegramm + 0x0004)
	lcall	_find_first_objno
	mov	r2,dpl
	C$rm_app.c$34$1$1 ==.
;	rm_app.c:34: if (objno==2) {	// nur das Objekt für Alarm Fernauslösung darf beschrieben werden
	cjne	r2,#0x02,00106$
	C$rm_app.c$35$2$2 ==.
;	rm_app.c:35: if (telegramm[7]&0x01) {	// Alarm ein
	mov	a,(_telegramm + 0x0007)
	jnb	acc.0,00102$
	C$rm_app.c$36$3$3 ==.
;	rm_app.c:36: PIN_DATA = 0;
	clr	_P1_2
	C$rm_app.c$37$3$3 ==.
;	rm_app.c:37: fernalarm=1;
	setb	_fernalarm
	ret
00102$:
	C$rm_app.c$40$3$4 ==.
;	rm_app.c:40: PIN_DATA = 1;
	setb	_P1_2
	C$rm_app.c$41$3$4 ==.
;	rm_app.c:41: fernalarm=0;
	clr	_fernalarm
00106$:
	C$rm_app.c$44$2$1 ==.
	XG$write_value_req$0$0 ==.
	ret
;------------------------------------------------------------
;Allocation info for local variables in function 'read_value_req'
;------------------------------------------------------------
;------------------------------------------------------------
	G$read_value_req$0$0 ==.
	C$rm_app.c$48$2$1 ==.
;	rm_app.c:48: void read_value_req(void)	// Empfangenes read_value_request Telegramm verarbeiten
;	-----------------------------------------
;	 function read_value_req
;	-----------------------------------------
_read_value_req:
	C$rm_app.c$53$2$1 ==.
;	rm_app.c:53: }
	C$rm_app.c$53$2$1 ==.
	XG$read_value_req$0$0 ==.
	ret
;------------------------------------------------------------
;Allocation info for local variables in function 'read_obj_value'
;------------------------------------------------------------
;objno                     Allocated to registers r2 
;return_value              Allocated to registers r3 
;------------------------------------------------------------
	G$read_obj_value$0$0 ==.
	C$rm_app.c$56$2$1 ==.
;	rm_app.c:56: unsigned long read_obj_value(unsigned char objno)
;	-----------------------------------------
;	 function read_obj_value
;	-----------------------------------------
_read_obj_value:
	mov	r2,dpl
	C$rm_app.c$58$1$1 ==.
;	rm_app.c:58: unsigned char return_value=0;
	mov	r3,#0x00
	C$rm_app.c$60$1$1 ==.
;	rm_app.c:60: if(objno==0) return_value=alarm_obj;
	mov	a,r2
	jnz	00102$
	mov	c,_alarm_obj
	clr	a
	rlc	a
	mov	r3,a
00102$:
	C$rm_app.c$61$1$1 ==.
;	rm_app.c:61: if(objno==1) return_value=stoerung_obj;
	cjne	r2,#0x01,00104$
	mov	c,_stoerung_obj
	clr	a
	rlc	a
	mov	r3,a
00104$:
	C$rm_app.c$63$1$1 ==.
;	rm_app.c:63: return(return_value);
	mov	r2,#0x00
	mov	r4,#0x00
	mov	r5,#0x00
	mov	dpl,r3
	mov	dph,r2
	mov	b,r4
	mov	a,r5
	C$rm_app.c$64$1$1 ==.
	XG$read_obj_value$0$0 ==.
	ret
;------------------------------------------------------------
;Allocation info for local variables in function 'key'
;------------------------------------------------------------
;port                      Allocated to registers r2 
;------------------------------------------------------------
	G$key$0$0 ==.
	C$rm_app.c$67$1$1 ==.
;	rm_app.c:67: void key(void) __interrupt (7)		// Abfrage der beiden Statusleitungen
;	-----------------------------------------
;	 function key
;	-----------------------------------------
_key:
	push	acc
	push	ar2
	push	psw
	mov	psw,#0x00
	C$rm_app.c$71$1$1 ==.
;	rm_app.c:71: EKBI=0;				// keyboard Interrupt sperren
	clr	_IEN1_1
	C$rm_app.c$73$1$1 ==.
;	rm_app.c:73: port=P0&0x03;
	mov	a,#0x03
	anl	a,_P0
	mov	r2,a
	C$rm_app.c$74$1$1 ==.
;	rm_app.c:74: if(port==2) alarm=1;		// Alarm wenn P0.0 low
	cjne	r2,#0x02,00102$
	setb	_alarm
00102$:
	C$rm_app.c$75$1$1 ==.
;	rm_app.c:75: if(port==1) stoerung=1;		// Störung wenn P0.1 low
	cjne	r2,#0x01,00104$
	setb	_stoerung
00104$:
	C$rm_app.c$76$1$1 ==.
;	rm_app.c:76: if(port==0) {				// Aufhebung der Meldung wenn beide low
	mov	a,r2
	jnz	00106$
	C$rm_app.c$77$2$2 ==.
;	rm_app.c:77: alarm=0;
	clr	_alarm
	C$rm_app.c$78$2$2 ==.
;	rm_app.c:78: stoerung=0;
	clr	_stoerung
00106$:
	C$rm_app.c$80$1$1 ==.
;	rm_app.c:80: event=1;				// zeigt an, dass der Rauchmelder etwas gesendet hat
	setb	_event
	C$rm_app.c$81$1$1 ==.
;	rm_app.c:81: interrupted=1;			// Flag setzen, dass unterbrochen wurde
	setb	_interrupted
	C$rm_app.c$82$1$1 ==.
;	rm_app.c:82: KBCON=0;				// Interrupt-Flag löschen
	mov	_KBCON,#0x00
	C$rm_app.c$83$1$1 ==.
;	rm_app.c:83: KBPATN=port;			// aktuellen Portwert als neuen Ausgangspunkt für Änderung nehmen
	mov	_KBPATN,r2
	C$rm_app.c$84$1$1 ==.
;	rm_app.c:84: EKBI=1;					// Keyboard Interrupt wieder frei geben
	setb	_IEN1_1
	pop	psw
	pop	ar2
	pop	acc
	C$rm_app.c$85$1$1 ==.
	XG$key$0$0 ==.
	reti
;	eliminated unneeded push/pop dpl
;	eliminated unneeded push/pop dph
;	eliminated unneeded push/pop b
;------------------------------------------------------------
;Allocation info for local variables in function 'delay_timer'
;------------------------------------------------------------
;------------------------------------------------------------
	G$delay_timer$0$0 ==.
	C$rm_app.c$88$1$1 ==.
;	rm_app.c:88: void delay_timer(void)	// zählt jede Sekunde die Variable Timer hoch und prüft ob zyklisch gesendet werden soll
;	-----------------------------------------
;	 function delay_timer
;	-----------------------------------------
_delay_timer:
	C$rm_app.c$92$1$1 ==.
;	rm_app.c:92: RTCCON=0x60;		// RTC anhalten und Flag löschen
	mov	_RTCCON,#0x60
	C$rm_app.c$93$1$1 ==.
;	rm_app.c:93: RTCH=0xE1;			// reload Real Time Clock (1s=0xE100)
	mov	_RTCH,#0xE1
	C$rm_app.c$94$1$1 ==.
;	rm_app.c:94: RTCL=0x00;
	mov	_RTCL,#0x00
	C$rm_app.c$96$1$1 ==.
;	rm_app.c:96: timer++;			// Zählervariable jede Sekunde hochzählen
	inc	_timer
	clr	a
	cjne	a,_timer,00121$
	inc	(_timer + 1)
00121$:
	C$rm_app.c$98$1$1 ==.
;	rm_app.c:98: if (timer_alarm==timer && eeprom[A_ZYKLISCH]) {		// wenn Alarm zyklisch gesendet werden soll
	mov	a,_timer
	cjne	a,_timer_alarm,00105$
	mov	a,(_timer + 1)
	cjne	a,(_timer_alarm + 1),00105$
	mov	dptr,#(_eeprom + 0x00f4)
	clr	a
	movc	a,@a+dptr
	jz	00105$
	C$rm_app.c$99$2$2 ==.
;	rm_app.c:99: send_obj_value(0);
	mov	dpl,#0x00
	lcall	_send_obj_value
	C$rm_app.c$100$2$2 ==.
;	rm_app.c:100: if (eeprom[A_BASIS]) timer_alarm=timer+eeprom[A_FAKTOR]*60;	// Minuten
	mov	dptr,#(_eeprom + 0x00f6)
	clr	a
	movc	a,@a+dptr
	jz	00102$
	mov	dptr,#(_eeprom + 0x00f5)
	clr	a
	movc	a,@a+dptr
	mov	r2,a
	mov	b,#0x3C
	mul	ab
	add	a,_timer
	mov	_timer_alarm,a
	mov	a,(_timer + 1)
	addc	a,b
	mov	(_timer_alarm + 1),a
	sjmp	00105$
00102$:
	C$rm_app.c$101$2$2 ==.
;	rm_app.c:101: else timer_alarm=timer+eeprom[A_FAKTOR];					// Sekunden
	mov	dptr,#(_eeprom + 0x00f5)
	clr	a
	movc	a,@a+dptr
	mov	r2,a
	mov	r3,#0x00
	mov	a,r2
	add	a,_timer
	mov	_timer_alarm,a
	mov	a,r3
	addc	a,(_timer + 1)
	mov	(_timer_alarm + 1),a
00105$:
	C$rm_app.c$103$1$1 ==.
;	rm_app.c:103: if (timer_stoerung==timer && eeprom[S_ZYKLISCH]) {	// wenn Störung zyklisch gesendet werden soll
	mov	a,_timer
	cjne	a,_timer_stoerung,00111$
	mov	a,(_timer + 1)
	cjne	a,(_timer_stoerung + 1),00111$
	mov	dptr,#(_eeprom + 0x00f7)
	clr	a
	movc	a,@a+dptr
	jz	00111$
	C$rm_app.c$104$2$3 ==.
;	rm_app.c:104: send_obj_value(1);
	mov	dpl,#0x01
	lcall	_send_obj_value
	C$rm_app.c$105$2$3 ==.
;	rm_app.c:105: if (eeprom[S_BASIS]) timer_stoerung=timer+eeprom[S_FAKTOR]*60;	// Minuten
	mov	dptr,#(_eeprom + 0x00f9)
	clr	a
	movc	a,@a+dptr
	jz	00108$
	mov	dptr,#(_eeprom + 0x00f8)
	clr	a
	movc	a,@a+dptr
	mov	r2,a
	mov	b,#0x3C
	mul	ab
	add	a,_timer
	mov	_timer_stoerung,a
	mov	a,(_timer + 1)
	addc	a,b
	mov	(_timer_stoerung + 1),a
	sjmp	00111$
00108$:
	C$rm_app.c$106$2$3 ==.
;	rm_app.c:106: else timer_stoerung=timer+eeprom[S_FAKTOR];						// Sekunden
	mov	dptr,#(_eeprom + 0x00f8)
	clr	a
	movc	a,@a+dptr
	mov	r2,a
	mov	r3,#0x00
	mov	a,r2
	add	a,_timer
	mov	_timer_stoerung,a
	mov	a,r3
	addc	a,(_timer + 1)
	mov	(_timer_stoerung + 1),a
00111$:
	C$rm_app.c$108$1$1 ==.
;	rm_app.c:108: RTCCON=0x61;		// RTC starten
	mov	_RTCCON,#0x61
	C$rm_app.c$109$1$1 ==.
	XG$delay_timer$0$0 ==.
	ret
;------------------------------------------------------------
;Allocation info for local variables in function 'restart_app'
;------------------------------------------------------------
;------------------------------------------------------------
	G$restart_app$0$0 ==.
	C$rm_app.c$112$1$1 ==.
;	rm_app.c:112: void restart_app(void)		// Alle Applikations-Parameter zurücksetzen
;	-----------------------------------------
;	 function restart_app
;	-----------------------------------------
_restart_app:
	C$rm_app.c$114$1$1 ==.
;	rm_app.c:114: P0M1= 0x00;			// alle Pins von Port0 als bidirectional definieren,
	mov	_P0M1,#0x00
	C$rm_app.c$115$1$1 ==.
;	rm_app.c:115: P0M2= 0x00;
	mov	_P0M2,#0x00
	C$rm_app.c$116$1$1 ==.
;	rm_app.c:116: P0= 0xFF;
	mov	_P0,#0xFF
	C$rm_app.c$120$1$1 ==.
;	rm_app.c:120: alarm=0;
	clr	_alarm
	C$rm_app.c$121$1$1 ==.
;	rm_app.c:121: stoerung=0;
	clr	_stoerung
	C$rm_app.c$122$1$1 ==.
;	rm_app.c:122: alarm_obj=0;
	clr	_alarm_obj
	C$rm_app.c$123$1$1 ==.
;	rm_app.c:123: stoerung_obj=0;
	clr	_stoerung_obj
	C$rm_app.c$124$1$1 ==.
;	rm_app.c:124: event=0;
	clr	_event
	C$rm_app.c$125$1$1 ==.
;	rm_app.c:125: fernalarm=0;
	clr	_fernalarm
	C$rm_app.c$126$1$1 ==.
;	rm_app.c:126: PIN_DATA=1;		// Fernauslösung Alarm ausschalten
	setb	_P1_2
	C$rm_app.c$129$1$1 ==.
;	rm_app.c:129: KBMASK=0x03;	// P0.0 & P0.1 enabled for KB-Interrupt
	mov	_KBMASK,#0x03
	C$rm_app.c$130$1$1 ==.
;	rm_app.c:130: KBPATN=0x03;	// Pattern
	mov	_KBPATN,#0x03
	C$rm_app.c$131$1$1 ==.
;	rm_app.c:131: KBCON=0;		// Interrupt when port not equal to pattern
	mov	_KBCON,#0x00
	C$rm_app.c$132$1$1 ==.
;	rm_app.c:132: EKBI=1;
	setb	_IEN1_1
	C$rm_app.c$134$1$1 ==.
;	rm_app.c:134: RTCH=0xE1;		// Real Time Clock auf 1s laden
	mov	_RTCH,#0xE1
	C$rm_app.c$135$1$1 ==.
;	rm_app.c:135: RTCL=0x00;		// (RTC ist ein down-counter mit 128 bit prescaler und osc-clock)
	mov	_RTCL,#0x00
	C$rm_app.c$136$1$1 ==.
;	rm_app.c:136: RTCCON=0x61;	// ... und starten
	mov	_RTCCON,#0x61
	C$rm_app.c$138$1$1 ==.
;	rm_app.c:138: timer=0;
	clr	a
	mov	_timer,a
	mov	(_timer + 1),a
	C$rm_app.c$139$1$1 ==.
;	rm_app.c:139: if (eeprom[A_ZYKLISCH]) timer_alarm=timer+2;
	mov	dptr,#(_eeprom + 0x00f4)
	clr	a
	movc	a,@a+dptr
	jz	00102$
	mov	_timer_alarm,#0x02
	clr	a
	mov	(_timer_alarm + 1),a
00102$:
	C$rm_app.c$140$1$1 ==.
;	rm_app.c:140: if (eeprom[S_ZYKLISCH]) timer_stoerung=timer+4;
	mov	dptr,#(_eeprom + 0x00f7)
	clr	a
	movc	a,@a+dptr
	jz	00104$
	mov	_timer_stoerung,#0x04
	clr	a
	mov	(_timer_stoerung + 1),a
00104$:
	C$rm_app.c$143$1$1 ==.
;	rm_app.c:143: EA=0;						// Interrupts sperren, damit flashen nicht unterbrochen wird
	clr	_IEN0_7
	C$rm_app.c$144$1$1 ==.
;	rm_app.c:144: START_WRITECYCLE;
	mov	_FMCON,#0x00
	C$rm_app.c$145$1$1 ==.
;	rm_app.c:145: WRITE_BYTE(0x01,0x03,0x00);	// Herstellercode 0x0000 = Freebus
	mov	_FMADRH,#0x1D
	mov	_FMADRL,#0x03
	mov	_FMDATA,#0x00
	C$rm_app.c$146$1$1 ==.
;	rm_app.c:146: WRITE_BYTE(0x01,0x04,0x00);
	mov	_FMADRH,#0x1D
	mov	_FMADRL,#0x04
	mov	_FMDATA,#0x00
	C$rm_app.c$147$1$1 ==.
;	rm_app.c:147: WRITE_BYTE(0x01,0x05,0x10);	// Devicetype 0x1003
	mov	_FMADRH,#0x1D
	mov	_FMADRL,#0x05
	mov	_FMDATA,#0x10
	C$rm_app.c$148$1$1 ==.
;	rm_app.c:148: WRITE_BYTE(0x01,0x06,0x03);
	mov	_FMADRH,#0x1D
	mov	_FMADRL,#0x06
	mov	_FMDATA,#0x03
	C$rm_app.c$149$1$1 ==.
;	rm_app.c:149: WRITE_BYTE(0x01,0x07,0x01);	// Versionnumber of application programm
	mov	_FMADRH,#0x1D
	mov	_FMADRL,#0x07
	mov	_FMDATA,#0x01
	C$rm_app.c$150$1$1 ==.
;	rm_app.c:150: WRITE_BYTE(0x01,0x0C,0x00);	// PORT A Direction Bit Setting
	mov	_FMADRH,#0x1D
	mov	_FMADRL,#0x0C
	mov	_FMDATA,#0x00
	C$rm_app.c$151$1$1 ==.
;	rm_app.c:151: WRITE_BYTE(0x01,0x0D,0xFF);	// Run-Status (00=stop FF=run)
	mov	_FMADRH,#0x1D
	mov	_FMADRL,#0x0D
	mov	_FMDATA,#0xFF
	C$rm_app.c$152$1$1 ==.
;	rm_app.c:152: WRITE_BYTE(0x01,0x12,0xA0);	// COMMSTAB Pointer
	mov	_FMADRH,#0x1D
	mov	_FMADRL,#0x12
	mov	_FMDATA,#0xA0
	C$rm_app.c$153$1$1 ==.
;	rm_app.c:153: STOP_WRITECYCLE;
	mov	_FMCON,#0x68
	C$rm_app.c$154$1$1 ==.
;	rm_app.c:154: EA=1;						// Interrupts freigeben
	setb	_IEN0_7
	C$rm_app.c$155$1$1 ==.
	XG$restart_app$0$0 ==.
	ret
	.area CSEG    (CODE)
	.area CONST   (CODE)
	.area XINIT   (CODE)
	.area CABS    (ABS,CODE)
