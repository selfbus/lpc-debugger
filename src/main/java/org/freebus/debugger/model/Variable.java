/*     */ package org.freebus.debugger.model;
/*     */ 
/*     */ import org.freebus.debugger.model.cdb.SymbolSpec;
/*     */ import org.freebus.debugger.model.cdb.SymbolType;
/*     */ import org.freebus.debugger.model.map.MapAreaLocation;
/*     */ 
/*     */ public class Variable extends MapAreaLocation
/*     */   implements Comparable<Variable>
/*     */ {
/*     */   private byte[] value;
/*  14 */   private boolean autoUpdate = false;
/*  15 */   private boolean visible = true;
/*     */   private SymbolSpec spec;
/*     */ 
/*     */   public Variable(String name, int address, int size, String module)
/*     */   {
/*  29 */     super(name, address, module);
/*  30 */     this.value = new byte[size];
/*     */   }
/*     */ 
/*     */   public Variable(MapAreaLocation location, int size)
/*     */   {
/*  41 */     super(location.getName(), location.getAddress(), location.getModule());
/*  42 */     this.value = new byte[size];
/*     */   }
/*     */ 
/*     */   public SymbolSpec getSpec()
/*     */   {
/*  50 */     return this.spec;
/*     */   }
/*     */ 
/*     */   public SymbolType getType()
/*     */   {
/*  58 */     return this.spec == null ? null : this.spec.getType();
/*     */   }
/*     */ 
/*     */   public void setSpec(SymbolSpec spec)
/*     */   {
/*  68 */     this.spec = spec;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  76 */     return this.value.length;
/*     */   }
/*     */ 
/*     */   public void setValue(byte[] value)
/*     */   {
/*  86 */     this.value = value;
/*     */   }
/*     */ 
/*     */   public byte[] getValue()
/*     */   {
/*  94 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean isAutoUpdate()
/*     */   {
/* 102 */     return this.autoUpdate;
/*     */   }
/*     */ 
/*     */   public void setAutoUpdate(boolean enable)
/*     */   {
/* 112 */     this.autoUpdate = enable;
/*     */   }
/*     */ 
/*     */   public boolean isVisible()
/*     */   {
/* 120 */     return this.visible;
/*     */   }
/*     */ 
/*     */   public void setVisible(boolean visible)
/*     */   {
/* 130 */     this.visible = visible;
/*     */   }
/*     */ 
/*     */   public int compareTo(Variable o)
/*     */   {
/* 139 */     return getName().compareTo(o.getName());
/*     */   }
/*     */ }

/* Location:           /home/stefan/Downloads/lpc-debugger/libs/lpc-debugger-0.1.7-SNAPSHOT.jar
 * Qualified Name:     org.freebus.debugger.model.Variable
 * JD-Core Version:    0.6.2
 */