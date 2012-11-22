package selfbus.debugger.gui;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import selfbus.debugger.model.Variable;
import sun.swing.DefaultLookup;

/**
 * A table cell renderer that renders the raw bytes of a {@link Variable}.
 * Parts of this implementation are taken from Swing's DefaultTableCellRenderer.
 */
public class VariableBytesCellRenderer extends JPanel implements TableCellRenderer, Serializable
{
   private static final long serialVersionUID = -8279672471105020636L;
   private static final int MAX_BYTES = 32;

   private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
   private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
   protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

   private final JLabel[] labels = new JLabel[MAX_BYTES];
   private final JLabel dotsLabel = new JLabel("...");

   private Color unusedColor = Color.GRAY;
   private Color modifiedColor = Color.RED;
   private Color unselectedForeground, unselectedBackground;

   /**
    * Create a table cell renderer.
    */
   public VariableBytesCellRenderer()
   {
      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

      for (int i = 0; i < MAX_BYTES; ++i)
      {
         JLabel lbl = new JLabel("xx ");
         lbl.setOpaque(false);
         lbl.setVisible(false);

         labels[i] = lbl;
         add(lbl);
      }

      dotsLabel.setOpaque(false);
      add(dotsLabel);
   }

   /**
    * Overrides <code>JComponent.setForeground</code> to assign the unselected-foreground color to
    * the specified color.
    * 
    * @param c set the foreground color to this value
    */
   public void setForeground(Color c)
   {
      super.setForeground(c);
      unselectedForeground = c;
   }

   /**
    * Overrides <code>JComponent.setBackground</code> to assign the unselected-background color to
    * the specified color.
    * 
    * @param c set the background color to this value
    */
   public void setBackground(Color c)
   {
      super.setBackground(c);
      unselectedBackground = c;
   }

   /**
    * Notification from the <code>UIManager</code> that the look and feel [L&F] has changed.
    * Replaces the current UI object with the latest version from the <code>UIManager</code>.
    * 
    * @see JComponent#updateUI
    */
   public void updateUI()
   {
      super.updateUI();
      setForeground(null);
      setBackground(null);
   }

   private Border getNoFocusBorder()
   {
      @SuppressWarnings("restriction")
      Border border = DefaultLookup.getBorder(this, ui, "Table.cellNoFocusBorder");
      if (System.getSecurityManager() != null)
      {
         if (border != null)
            return border;
         return SAFE_NO_FOCUS_BORDER;
      }
      else if (border != null)
      {
         if (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)
         {
            return border;
         }
      }
      return noFocusBorder;
   }

   /**
    * 
    * Returns the default table cell renderer.
    * <p>
    * During a printing operation, this method will be called with <code>isSelected</code> and
    * <code>hasFocus</code> values of <code>false</code> to prevent selection and focus from
    * appearing in the printed output. To do other customization based on whether or not the table
    * is being printed, check the return value from
    * {@link javax.swing.JComponent#isPaintingForPrint()}.
    * 
    * @param table the <code>JTable</code>
    * @param value the value to assign to the cell at <code>[row, column]</code>
    * @param isSelected true if cell is selected
    * @param hasFocus true if cell has focus
    * @param row the row of the cell to render
    * @param column the column of the cell to render
    * @return the default table cell renderer
    * @see javax.swing.JComponent#isPaintingForPrint()
    */
   @SuppressWarnings("restriction")
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column)
   {
      if (table == null)
      {
         return this;
      }

      Color fg = null;
      Color bg = null;

      JTable.DropLocation dropLocation = table.getDropLocation();
      if (dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn()
         && dropLocation.getRow() == row && dropLocation.getColumn() == column)
      {

         fg = DefaultLookup.getColor(this, ui, "Table.dropCellForeground");
         bg = DefaultLookup.getColor(this, ui, "Table.dropCellBackground");

         isSelected = true;
      }

      if (isSelected)
      {
         super.setForeground(fg == null ? table.getSelectionForeground() : fg);
         super.setBackground(bg == null ? table.getSelectionBackground() : bg);
      }
      else
      {
         Color background = unselectedBackground != null ? unselectedBackground : table.getBackground();
         if (background == null || background instanceof javax.swing.plaf.UIResource)
         {
            Color alternateColor = DefaultLookup.getColor(this, ui, "Table.alternateRowColor");
            if (alternateColor != null && row % 2 != 0)
            {
               background = alternateColor;
            }
         }
         super.setForeground(unselectedForeground != null ? unselectedForeground : table.getForeground());
         super.setBackground(background);
      }

      setFont(table.getFont());

      if (hasFocus)
      {
         Border border = null;
         if (isSelected)
         {
            border = DefaultLookup.getBorder(this, ui, "Table.focusSelectedCellHighlightBorder");
         }
         if (border == null)
         {
            border = DefaultLookup.getBorder(this, ui, "Table.focusCellHighlightBorder");
         }
         setBorder(border);

         if (!isSelected && table.isCellEditable(row, column))
         {
            Color col;
            col = DefaultLookup.getColor(this, ui, "Table.focusCellForeground");
            if (col != null)
            {
               super.setForeground(col);
            }
            col = DefaultLookup.getColor(this, ui, "Table.focusCellBackground");
            if (col != null)
            {
               super.setBackground(col);
            }
         }
      }
      else
      {
         setBorder(getNoFocusBorder());
      }

      setBytes((Variable) value);

      return this;
   }

   /**
    * Set the variable's bytes into the corresponding JLabel objects.
    */
   private void setBytes(Variable var)
   {
      byte[] value = var.getValue();
      byte[] prevValue = var.getPrevValue();

      for (int i = 0; i < MAX_BYTES; ++i)
      {
         if (i >= value.length)
         {
            labels[i].setVisible(false);
            continue;
         }

         labels[i].setText(String.format("%02x ", value[i]));
         labels[i].setVisible(true);

         if (prevValue == null)
            labels[i].setForeground(unusedColor);
         else if (value[i] == prevValue[i])
            labels[i].setForeground(unselectedForeground);
         else labels[i].setForeground(modifiedColor);
      }

      dotsLabel.setVisible(value.length > MAX_BYTES);
   }

   /**
    * Overridden for performance reasons.
    */
   @Override
   public boolean isOpaque()
   {
      Color back = getBackground();
      Component p = getParent();
      if (p != null)
      {
         p = p.getParent();
      }

      // p should now be the JTable.
      boolean colorMatch = (back != null) && (p != null) && back.equals(p.getBackground()) && p.isOpaque();
      return !colorMatch && super.isOpaque();
   }

   /**
    * Overridden for performance reasons.
    */
   @Override
   protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      // Strings get interned...
      if (propertyName == "text"
         || propertyName == "labelFor"
         || propertyName == "displayedMnemonic"
         || ((propertyName == "font" || propertyName == "foreground") && oldValue != newValue && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null))
      {

         super.firePropertyChange(propertyName, oldValue, newValue);
      }
   }

   /**
    * Overridden for performance reasons.
    */
   @Override
   public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue)
   {
   }
}
