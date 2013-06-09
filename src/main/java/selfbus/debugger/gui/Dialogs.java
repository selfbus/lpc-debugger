package selfbus.debugger.gui;

import java.awt.Cursor;

import javax.swing.JOptionPane;

import org.slf4j.LoggerFactory;

import selfbus.debugger.misc.I18n;

/**
 * Factory for custom dialogs
 */
public final class Dialogs
{
   /**
    * Show an exception dialog.
    * 
    * @param e - the exception.
    * @param message - some human readable message (not {@link Exception#getMessage}!)
    */
   public static void showExceptionDialog(Throwable e, String message)
   {
      LoggerFactory.getLogger(Dialogs.class).warn(message, e);

      final String fmtMessage = formatExceptionMessage(e, message);
      showErrorDialog(I18n.getMessage("Dialogs.Exception_Title"), fmtMessage);
   }

   /**
    * Show an error dialog.
    * 
    * @param title - The title of the dialog window
    * @param message - Some human readable message (not {@link Exception#getMessage}!)
    */
   public static void showErrorDialog(String title, String message)
   {
      MainWindow appWin = MainWindow.getInstance();

      if (appWin != null)
         appWin.setCursor(Cursor.getDefaultCursor());

      if (!message.startsWith("<html>"))
         message = "<html><body width=\"400px\">" + message.replace("\n", "<br>") + "</body></html>";

      JOptionPane.showMessageDialog(appWin, message, title, JOptionPane.ERROR_MESSAGE);
   }

   /**
    * Show an error dialog with the default error-dialog window title.
    * 
    * @param message - Some human readable message (not {@link Exception#getMessage()}!)
    * 
    * @see #showExceptionDialog(Exception, String)
    */
   public static void showErrorDialog(String message)
   {
      showErrorDialog(I18n.getMessage("Dialogs.Error_Title"), message);
   }

   /**
    * Show a warning dialog.
    * 
    * @param title - The title of the dialog window
    * @param message - Some human readable message (not {@link Exception#getMessage}!)
    */
   public static void showWarningDialog(String title, String message)
   {
      MainWindow appWin = MainWindow.getInstance();

      if (appWin != null)
         appWin.setCursor(Cursor.getDefaultCursor());

      if (!message.startsWith("<html>"))
         message = "<html><body width=\"400px\">" + message.replace("\n", "<br>") + "</body></html>";

      JOptionPane.showMessageDialog(appWin, message, title, JOptionPane.WARNING_MESSAGE);
   }

   /**
    * Show a warning dialog with the default error-dialog window title.
    * 
    * @param message - Some human readable message (not {@link Exception#getMessage()}!)
    */
   public static void showWarningDialog(String message)
   {
      showWarningDialog(I18n.getMessage("Dialogs.Warning_Title"), message);
   }

   /**
    * Format the exception and the message as it is used in
    * {@link #showExceptionDialog(Exception, String)}.
    * 
    * @param e - the exception.
    * @param message - some human readable message (not {@link Exception#getMessage}!)
    * 
    * @return The formatted exception.
    */
   public static String formatExceptionMessage(Throwable e, String message)
   {
      final StringBuffer sb = new StringBuffer();

      sb.append("<html><body width=\"500px\"><h2>").append(I18n.getMessage("Dialogs.Exception_Caption"));
      sb.append("</h2><b>").append(message.replace("\n", "<br />"));

      if (message.isEmpty())
         sb.append(e.getClass().getName());
      else if (!message.endsWith(".") && !message.endsWith("?") && !message.endsWith("!"))
         sb.append('.');

      sb.append("</b><br /><br /><hr /><br />");

      for (Throwable ee = e; ee != null; ee = ee.getCause())
      {
         String msg = ee.getMessage();
         if (msg == null)
            continue;

         sb.append("<b>").append(ee.getClass().getSimpleName()).append("</b><br />");

         for (String line : msg.split("\r*\n"))
         {
            line = line.trim();

            if (line.isEmpty())
               continue;

            if (line.length() > 1000)
               line = line.substring(0, 1000) + " ...";

            line = line.replace('`', '\'').replace("<", "&lt;");
            sb.append(line).append("<br /><br />");
         }
      }

      sb.append("</body></html>");
      return sb.toString();
   }
}
