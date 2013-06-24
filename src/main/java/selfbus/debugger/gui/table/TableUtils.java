package selfbus.debugger.gui.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Utility methods for {@link JTree} trees.
 */
public final class TableUtils
{
   /**
    * Pack the table such that all columns are just as wide as their contents
    * requires.
    * 
    * @param table - the table to pack.
    * @param margin - the extra space to add to both sides of every column.
    */
   public static void pack(JTable table, int margin)
   {
      final TableColumnModel colModel = table.getColumnModel();

      for (int columnIndex = colModel.getColumnCount() - 1; columnIndex >= 0; --columnIndex)
      {
         final TableColumn col = colModel.getColumn(columnIndex);
         int width = getPreferredColumnWidth(table, columnIndex);
         col.setPreferredWidth(width + (margin << 1));
      }
   }

   /**
    * Get the preferred width of a column.
    * 
    * @param table - the table to process
    * @param columnIndex - the index of the requested column
    * 
    * @return The preferred width of the column.
    */
   public static int getPreferredColumnWidth(JTable table, int columnIndex)
   {
      final TableColumn col = table.getColumnModel().getColumn(columnIndex);
      int width = 0;

      TableCellRenderer renderer = col.getHeaderRenderer();
      if (renderer == null)
         renderer = table.getTableHeader().getDefaultRenderer();

      Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
      width = comp.getPreferredSize().width;

      // Get maximum width of column data
      for (int r = 0; r < table.getRowCount(); r++)
      {
         renderer = table.getCellRenderer(r, columnIndex);
         comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, columnIndex), false, false, r, columnIndex);
         width = Math.max(width, comp.getPreferredSize().width);
      }

      return width;
   }

   /**
    * Create a string that contains the names of the table's columns in their
    * current order, separated by comma.
    * 
    * @param table - the table to process.
    * @return a string containing the comma separated names of the columns.
    * 
    * @see #applyColumnOrderString
    */
   public static String createColumnOrderString(final JTable table)
   {
      final StringBuilder sb = new StringBuilder();

      final TableColumnModel columnModel = table.getColumnModel();
      final int numColumns = columnModel.getColumnCount();
      for (int i = 0; i < numColumns; ++i)
      {
         if (i > 0) sb.append(',');
         sb.append(columnModel.getColumn(i).getHeaderValue().toString());
      }

      return sb.toString();
   }

   /**
    * Apply the order of the columns to the table.
    * 
    * @param table - the table to process.
    * @param orderStr - the string with the column names, separated by comma.
    * 
    * @see #createColumnOrderString
    */
   public static void applyColumnOrderString(final JTable table, final String orderStr)
   {
      final TableColumnModel columnModel = table.getColumnModel();
      final String[] colNames = orderStr.split(",");

      for (int i = 0; i < colNames.length; ++i)
      {
         try
         {
            final int idx = columnModel.getColumnIndex(colNames[i]);
            if (idx >= 0) columnModel.moveColumn(idx, i);
         }
         catch (IllegalArgumentException e)
         {
         }
      }
   }

   /**
    * Disabled constructor.
    */
   private TableUtils()
   {
   }
}
