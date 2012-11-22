package selfbus.debugger.actions;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

/**
 * Factory for {@link BasicAction actions}.
 */
public class ActionFactory
{
   private static final ActionFactory INSTANCE = new ActionFactory();

   private final Map<String, BasicAction> actions = new HashMap<String, BasicAction>();
   private String[] packageNames;

   /**
    * Create a action factory.
    * 
    * @see #getInstance()
    */
   public ActionFactory()
   {
      setPackageNames(new String[] { getClass().getCanonicalName().replaceAll("\\.[^\\.]+$", "") });
   }

   /**
    * @return The action factory instance.
    */
   public static ActionFactory getInstance()
   {
      return INSTANCE;
   }

   /**
    * Set the package names to try when searching for actions.
    * 
    * @param packageNames - the package names
    */
   public void setPackageNames(String[] packageNames)
   {
      this.packageNames = packageNames;
   }

   /**
    * Get an action object. The actions are singleton objects.
    * 
    * @param id - the id of the action.
    * @return The action object.
    * 
    * @throws ActionCreationException if the creation of the action object fails.
    */
   public BasicAction getAction(String id)
   {
      Validate.notNull(this.packageNames, "the action package names must be set", new Object[0]);

      synchronized (this.actions)
      {
         BasicAction action = (BasicAction) this.actions.get(id);
         if (action == null)
         {
            StringBuilder failedMsg = new StringBuilder();
            String className = null;
            Class<?> clazz = null;

            for (String packageName : this.packageNames)
            {
               className = packageName + '.' + id.substring(0, 1).toUpperCase() + id.substring(1);
               try
               {
                  clazz = getClass().getClassLoader().loadClass(className);
               }
               catch (ClassNotFoundException e)
               {
                  failedMsg.append(" ").append(className);
               }
            }

            if (clazz == null)
            {
               throw new ActionCreationException("No class found for action " + id + ". Tried " + failedMsg.toString());
            }

            try
            {
               action = (BasicAction) clazz.newInstance();
            }
            catch (InstantiationException e)
            {
               throw new ActionCreationException("Could not instantiate class " + clazz);
            }
            catch (IllegalAccessException e)
            {
               throw new ActionCreationException("Could not instantiate class " + clazz + ": illegal access");
            }

            this.actions.put(id, action);
         }

         return action;
      }
   }
}
