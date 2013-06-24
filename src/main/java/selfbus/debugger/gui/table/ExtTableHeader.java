package selfbus.debugger.gui.table;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class ExtTableHeader extends JTableHeader
{
   private static final long serialVersionUID = -8087494029020747559L;

   protected JPopupMenu popup;
   protected boolean interceptMouseListener = false;

   public ExtTableHeader(TableColumnModel columnModel)
   {
      super(columnModel);
   }

   @Override
   public void updateUI()
   {
      interceptMouseListener = true;
      super.updateUI();
      interceptMouseListener = false;
   }

   @Override
   public synchronized void addMouseListener(MouseListener l)
   {
      if (interceptMouseListener)
         super.addMouseListener(new ExtTableHeaderMouseHandler(l));
      else super.addMouseListener(l);
   }

   public void setPopupMenu(JPopupMenu popup)
   {
      this.popup = popup;
   }

   public JPopupMenu getPopupMenu()
   {
      return popup;
   }

   class ExtTableHeaderMouseHandler implements MouseListener
   {
      private final MouseListener target;

      ExtTableHeaderMouseHandler(MouseListener target)
      {
         this.target = target;
      }

      @Override
      public void mouseClicked(MouseEvent e)
      {
         target.mouseClicked(e);
      }

      @Override
      public void mousePressed(MouseEvent e)
      {
         if (SwingUtilities.isRightMouseButton(e))
         {
            ExtTableHeader header = ExtTableHeader.this;
            JPopupMenu popup = getPopupMenu();

            if (popup != null)
               popup.show(header, e.getX(), e.getY());
         }
         else target.mousePressed(e);
      }

      @Override
      public void mouseReleased(MouseEvent e)
      {
//         if (SwingUtilities.isRightMouseButton(e))
//         {
//            JPopupMenu popup = getPopupMenu();
//
//            if (popup != null)
//               popup.setVisible(false);
//         }
//         else
           target.mouseReleased(e);
      }

      @Override
      public void mouseEntered(MouseEvent e)
      {
         target.mouseEntered(e);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
         target.mouseExited(e);
      }
   }
}
