package org.freebus.debugger.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.freebus.debugger.misc.ByteUtils;
import org.freebus.debugger.misc.HexString;
import org.freebus.debugger.misc.I18n;
import org.freebus.debugger.model.Variable;
import org.freebus.debugger.model.cdb.ArraySymbolSpec;
import org.freebus.debugger.model.cdb.StructureSymbolSpec;
import org.freebus.debugger.model.cdb.SymbolSign;
import org.freebus.debugger.model.cdb.SymbolSpec;
import org.freebus.debugger.model.cdb.SymbolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariableComponent
{
   private static final Logger LOGGER = LoggerFactory.getLogger(VariableComponent.class);
   private static final Insets INSETS = new Insets(0, 0, 0, 1);
   private static final Border BORDER = BorderFactory.createEmptyBorder(0, 3, 0, 3);
   private final Variable var;
   private final Color normalColor;
   private final Color unusedColor;
   private final Color changedColor;
   private final JLabel lblType;
   private final JLabel lblName;
   private final JLabel lblValue;
   private final JLabel lblBytes;
   private int maxValueWidth = 32;

   public VariableComponent(Variable var, Color backgroundColor)
   {
      this.var = var;

      String toolTip = createToolTip(var);

      this.lblType = newLabel(getTypeStr(var.getSpec()), backgroundColor);
      this.lblType.setToolTipText(toolTip);

      this.lblName = newLabel(var.getName().replaceFirst("^_", ""), backgroundColor);
      this.lblName.setToolTipText(toolTip);

      this.lblValue = newLabel(" ", backgroundColor);
      this.lblValue.setMinimumSize(this.lblValue.getSize());
      this.lblValue.setHorizontalAlignment(4);
      this.lblValue.setText(" ");

      this.lblBytes = newLabel(" ", backgroundColor);

      this.normalColor = this.lblName.getForeground();
      this.unusedColor = this.normalColor.brighter().brighter();
      this.changedColor = Color.RED;

      updateValue();

      this.lblValue.setForeground(this.unusedColor);
      this.lblBytes.setForeground(this.unusedColor);

      if (var.getAddress() == -1)
      {
         this.lblType.setForeground(this.unusedColor);
         this.lblName.setForeground(this.unusedColor);
      }
   }

   String createToolTip(Variable var)
   {
      SymbolSpec spec = var.getSpec();

      StringBuilder sb = new StringBuilder(200);
      sb.append("<html><table><tr><td>");
      sb.append(I18n.getMessage("VariableComponent.addressHeader"));
      sb.append("</td><td>");
      if (var.getAddress() == -1)
         sb.append(I18n.getMessage("VariableComponent.addressUnknown"));
      else sb.append(String.format("0x%1$04x", new Object[] { Integer.valueOf(var.getAddress()) }));
      sb.append("</td></tr><tr><td>");
      sb.append(I18n.getMessage("VariableComponent.typeHeader"));
      sb.append("</td><td>").append(spec.getType().toString());
      sb.append("</td></tr><tr><td>");
      sb.append(I18n.getMessage("VariableComponent.sizeHeader"));
      sb.append("</td><td>").append(spec.getSize());
      sb.append("</td></tr></table></html>");

      return sb.toString();
   }

   public static void addHeaderTo(JPanel parent, int gridX, int gridY, Color bgcolor)
   {
      JLabel lbl = newLabel(I18n.getMessage("VariableComponent.typeHeader"), bgcolor);
      parent.add(lbl, new GridBagConstraints(gridX++, gridY, 1, 1, 1.0D, 1.0D, 18, 2, INSETS, 0, 0));

      lbl = newLabel(I18n.getMessage("VariableComponent.nameHeader"), bgcolor);
      parent.add(lbl, new GridBagConstraints(gridX++, gridY, 1, 1, 1.0D, 1.0D, 18, 2, INSETS, 0, 0));

      lbl = newLabel(I18n.getMessage("VariableComponent.valueHeader"), bgcolor);
      parent.add(lbl, new GridBagConstraints(gridX++, gridY, 1, 1, 1.0D, 1.0D, 12, 2, INSETS, 0, 0));

      lbl = newLabel(I18n.getMessage("VariableComponent.bytesHeader"), bgcolor);
      parent.add(lbl, new GridBagConstraints(gridX++, gridY, 1, 1, 1.0D, 1.0D, 18, 2, INSETS, 0, 0));
   }

   static JLabel newLabel(String text, Color bgcolor)
   {
      JLabel lbl = new JLabel(text);
      lbl.setOpaque(true);
      lbl.setBackground(bgcolor);
      lbl.setBorder(BORDER);
      return lbl;
   }

   public void addTo(JPanel parent, int gridX, int gridY)
   {
      parent.add(this.lblType, new GridBagConstraints(gridX++, gridY, 1, 1, 1.0D, 1.0D, 18, 2, INSETS, 0, 0));

      parent.add(this.lblName, new GridBagConstraints(gridX++, gridY, 1, 1, 1.0D, 1.0D, 18, 2, INSETS, 0, 0));

      parent.add(this.lblValue, new GridBagConstraints(gridX++, gridY, 1, 1, 1.0D, 1.0D, 12, 2, INSETS, 0, 0));

      parent.add(this.lblBytes, new GridBagConstraints(gridX++, gridY, 1, 1, 1.0D, 1.0D, 18, 2, INSETS, 0, 0));
   }

   protected String getTypeStr(SymbolSpec spec)
   {
      if (spec == null)
      {
         return " ";
      }
      SymbolType type = spec.getType();
      SymbolSign sign = spec.getSign();

      if (SymbolType.SBIT.equals(type))
      {
         sign = null;
      }
      String signStr = SymbolSign.UNSIGNED.equals(sign) ? "u" : "";
      String typeStr = signStr + type.toString().toLowerCase();

      if ((spec instanceof ArraySymbolSpec))
      {
         return typeStr + '[' + ((ArraySymbolSpec) spec).getCount() + ']';
      }
      if ((spec instanceof StructureSymbolSpec))
      {
         return "struct " + ((StructureSymbolSpec) spec).getName();
      }

      return typeStr;
   }

   public void valueChanged()
   {
      updateValue();

      this.lblValue.setForeground(this.changedColor);
      this.lblBytes.setForeground(this.changedColor);
   }

   public void initValue()
   {
      updateValue();

      this.lblValue.setForeground(this.unusedColor);
      this.lblBytes.setForeground(this.unusedColor);
   }

   public void oldenValue()
   {
      if (this.changedColor.equals(this.lblValue.getForeground()))
      {
         this.lblValue.setForeground(this.normalColor);
         this.lblBytes.setForeground(this.normalColor);
      }
   }

   protected void updateValue()
   {
      SymbolSpec spec = this.var.getSpec();
      SymbolType type = this.var.getType();
      int size = this.var.size();
      String newValue = " ";

      if ((spec instanceof ArraySymbolSpec))
      {
         if (SymbolType.BIT_FIELD.equals(type))
         {
            newValue = Integer.toBinaryString(ByteUtils.toInteger(this.var.getValue(), 0, size));
         }
      }
      else if ((type == null) && (size > 0) && (size <= 4))
      {
         newValue = Integer.toString(ByteUtils.toInteger(this.var.getValue(), 0, size));
      }
      else if (SymbolType.SBIT.equals(type))
      {
         newValue = Integer.toString(this.var.getValue()[0]);
      }
      else if ((SymbolType.CHAR.equals(type)) || (SymbolType.SHORT.equals(type)) || (SymbolType.INT.equals(type))
         || (SymbolType.LONG.equals(type)))
      {
         int val = ByteUtils.toInteger(this.var.getValue(), 0, size);
         newValue = Integer.toString(val);
      }

      this.lblValue.setText(newValue);

      String bytesStr = HexString.toString(this.var.getValue(), 0, Math.min(size, this.maxValueWidth));
      if (size > this.maxValueWidth)
      {
         bytesStr = bytesStr + "...";
      }
      this.lblBytes.setText(bytesStr);

      if (LOGGER.isTraceEnabled())
         LOGGER.trace(this.var.getName() + " = " + newValue + " [" + bytesStr + "]");
   }
}
