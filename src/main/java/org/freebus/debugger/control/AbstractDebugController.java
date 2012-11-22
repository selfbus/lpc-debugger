package org.freebus.debugger.control;

import java.util.concurrent.CopyOnWriteArraySet;
import org.freebus.debugger.model.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for debug controllers.
 */
public abstract class AbstractDebugController
{
   static final Logger LOGGER = LoggerFactory.getLogger(AbstractDebugController.class);

   private final CopyOnWriteArraySet<DebugListener> listeners = new CopyOnWriteArraySet<DebugListener>();

   /**
    * Add a listener.
    *
    * @param listener - the listener to add
    */
   public void addListener(DebugListener listener)
   {
      this.listeners.add(listener);
   }

   /**
    * Remove a listener.
    *
    * @param listener - the listener to remove
    */
   public void removeListener(DebugListener listener)
   {
      this.listeners.remove(listener);
   }

   public void fireBeforeUpdate()
   {
      LOGGER.trace("fire before update");

      for (DebugListener listener : this.listeners)
         listener.beforeUpdate();
   }

   public void fireAfterUpdate()
   {
      LOGGER.trace("fire after update");

      for (DebugListener listener : this.listeners)
         listener.afterUpdate();
   }

   public void fireValueChanged(Variable var)
   {
      LOGGER.trace("fire value changed of {}", var.getName());

      for (DebugListener listener : this.listeners)
         listener.valueChanged(var);
   }

   public void fireVariablesChanged()
   {
      LOGGER.trace("fire variables changed");

      for (DebugListener listener : this.listeners)
         listener.variablesChanged();
   }

   public void fireConnectionOpened()
   {
      LOGGER.trace("fire connection opened");

      for (DebugListener listener : this.listeners)
         listener.connectionOpened();
   }

   public void fireConnectionClosed()
   {
      LOGGER.trace("fire connection closed");

      for (DebugListener listener : this.listeners)
         listener.connectionClosed();
   }
}
