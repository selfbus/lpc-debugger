package org.freebus.debugger.misc;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Message translation funtions.
 */
public final class I18n
{
   public static final String BUNDLE_NAME = "messages";
   public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

   /**
    * Returns the message string for the given message-id for the active language.
    * 
    * @param msgid - the message id.
    * @return the message string.
    */
   public static String getMessage(String msgid)
   {
      try
      {
         if (BUNDLE != null)
         {
            return BUNDLE.getString(msgid);
         }
      }
      catch (MissingResourceException localMissingResourceException)
      {
      }
      return '!' + msgid + '!';
   }

   /**
    * Returns the message string for the given message-id for the active language. The variables
    * {0}, {1}, ... are replaced with the arguments given in <code>args</code>.
    * 
    * The class {@link MessageFormat} contains more further information on formatting the arguments.
    * 
    * @param msgid - the message id
    * @param args - an array of arguments.
    * @return the message string.
    */
   public static String formatMessage(String msgid, String[] args)
   {
      StringBuffer sb = new StringBuffer();
      new MessageFormat(getMessage(msgid)).format(args, sb, null);
      return sb.toString();
   }

   /**
    * Disabled constructor.
    */
   private I18n()
   {
   }
}
