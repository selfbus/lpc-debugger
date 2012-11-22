package selfbus.debugger.control;

import selfbus.debugger.model.Variable;

/**
 * Interface for listeners that get informed by the {@link DebugController}.
 */
public interface DebugListener
{
   /**
    * The variables have changed. Called when another project is loaded.
    */
   public void variablesChanged();

   /**
    * The value of a single variable has changed.
    * 
    * @param var - the variable
    */
   public void valueChanged(Variable var);

   /**
    * Called before the debug controller starts updating the variables.
    */
   public void beforeUpdate();

   /**
    * Called after the debug controller starts updating the variables.
    */
   public void afterUpdate();

   /**
    * Called when the connection to the debugged device is opened.
    */
   public void connectionOpened();

   /**
    * Called when the connection to the debugged device is closed.
    */
   public void connectionClosed();
}
