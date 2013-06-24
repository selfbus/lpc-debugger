package selfbus.debugger.gui.table;

import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class ExtTableColumnModel extends DefaultTableColumnModel
{
   private static final long serialVersionUID = -2173386193764225L;

   /**
    * Inserts <code>aColumn</code> at the index <code>idx</code> into the
    * <code>tableColumns</code> array. This method also posts the
    * <code>columnAdded</code> event to its listeners.
    * 
    * @param aColumn the <code>TableColumn</code> to be added
    * @exception IllegalArgumentException if <code>aColumn</code> is <code>null</code>
    * @see #removeColumn
    */
   public void insertColumn(int idx, TableColumn aColumn)
   {
      if (aColumn == null)
      {
         throw new IllegalArgumentException("Object is null");
      }

      tableColumns.insertElementAt(aColumn, idx);
      aColumn.addPropertyChangeListener(this);
      totalColumnWidth = -1;

      // Post columnAdded event notification
      fireColumnAdded(new TableColumnModelEvent(this, 0, idx));
   }
}
