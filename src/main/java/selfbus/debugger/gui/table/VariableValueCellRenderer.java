package selfbus.debugger.gui.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import selfbus.debugger.misc.ByteUtils;
import selfbus.debugger.model.Variable;
import selfbus.debugger.model.cdb.ArraySymbolSpec;
import selfbus.debugger.model.cdb.SymbolSpec;
import selfbus.debugger.model.cdb.SymbolType;

/**
 * A table cell renderer that renders the value of a {@link Variable}.
 */
public class VariableValueCellRenderer extends DefaultTableCellRenderer
{
   private static final long serialVersionUID = -880338873722517799L;

   private Color unusedColor = Color.GRAY;
   private Color modifiedColor = Color.RED;

   /**
    * Create a table cell renderer.
    */
   public VariableValueCellRenderer()
   {
   }

   /**
    * {@inheritDoc}
    */
   public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
      int row, int column)
   {
      Variable var = (Variable) obj;
      Component comp = super.getTableCellRendererComponent(table, valueStr(var), isSelected, hasFocus, row, column);

      if (var.isModified())
         comp.setForeground(modifiedColor);
      else if (var.isUnused())
         comp.setForeground(unusedColor);

      return comp;
   }

   /**
    * Create a string containing the variable's value. The string depends on the type of the variable.
    *
    * @param var - the variable to process
    * @return The variable's value as string.
    */
   protected String valueStr(Variable var)
   {
      SymbolSpec spec = var.getSpec();
      SymbolType type = var.getType();
      int size = var.size();

      if ((spec instanceof ArraySymbolSpec))
      {
         if (SymbolType.BIT_FIELD.equals(type))
         {
            return Integer.toBinaryString(ByteUtils.toInteger(var.getValue(), 0, size));
         }
      }
      else if ((type == null) && (size > 0) && (size <= 4))
      {
         return Integer.toString(ByteUtils.toInteger(var.getValue(), 0, size));
      }
      else if (SymbolType.SBIT.equals(type))
      {
         return Integer.toString(var.getValue()[0]);
      }
      else if ((SymbolType.CHAR.equals(type)) || (SymbolType.SHORT.equals(type)) || (SymbolType.INT.equals(type))
         || (SymbolType.LONG.equals(type)))
      {
         int val = ByteUtils.toInteger(var.getValue(), 0, size);
         return Integer.toString(val);
      }

      return "";
   }
}
