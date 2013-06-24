package selfbus.debugger.gui.table;

import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;

class ExtTableHeaderUI extends BasicTableHeaderUI
{
   public ExtTableHeaderUI(TableHeaderUI ui)
   {
      // TODO Auto-generated constructor stub
   }

   /**
    * {@inheritDoc}
    */
   protected MouseInputListener createMouseInputListener()
   {
      return new ExtMouseInputHandler();
   }

   class ExtMouseInputHandler extends BasicTableHeaderUI.MouseInputHandler
   {
      public void mousePressed(MouseEvent e)
      {
         if (SwingUtilities.isRightMouseButton(e))
         {
            JPopupMenu popup = ((ExtTableHeader) header).getPopupMenu();

//            if (popup != null)
               popup.show(header, e.getX(), e.getY());
         }
         super.mousePressed(e);
      }

      public void mouseReleased(MouseEvent e)
      {
         super.mouseReleased(e);
      }
   }
}
