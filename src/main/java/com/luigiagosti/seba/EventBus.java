/**
 * Copyright 2012 Giago Software Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.luigiagosti.seba;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Instantiate and share an event bus between event consumers and producers. 
 * A good place can be the Application class, but in theory you have multiple
 * eventBus to use for different part of the application.
 * Every eventBus will deliver events sent to listeners registered against 
 * the producer class of handler registered against the class of events.
 * 
 * @see EventListener
 * @see EventHandler
 * @see Event
 */
public class EventBus {
	
	private ConcurrentMap<Class<? extends Event>, EventHandler> handlers;
	private ConcurrentMap<Class<?>, EventListener> listeners;
	private ConcurrentMap<Class<? extends Event>, Event> stickyEvents;
	
	public EventBus() {
	    handlers = new ConcurrentHashMap<Class<? extends Event>, EventHandler>();
	    listeners = new ConcurrentHashMap<Class<?>, EventListener>();
	    stickyEvents = new ConcurrentHashMap<Class<? extends Event>, Event>();
    }

	/**
	 * This methods register an <EventHandler,EventClass> pair so that when an event
	 * is sent on the bus the handler is called to handle the event.
	 * 
	 * @param handler
	 * @param eventClass
	 */
	public void registerHandler(EventHandler handler, Class<? extends Event> eventClass) {
		checkNull(handler, "Handler");
		checkNull(eventClass, "Event");
		handlers.putIfAbsent(eventClass, handler);
		if(stickyEvents.containsKey(eventClass)) {
			handler.handle(stickyEvents.get(eventClass));
		}
	}

	/**
	 * Unregister the handler. It is important to remove all the handlers as soon as they
	 * are not necessary. A good practice is to register onResume and unregister onPause.
	 * 
	 * @param handler
	 * @param eventClass
	 */
	public void unregisterHandler(EventHandler handler, Class<? extends Event> eventClass) {
		checkNull(handler, "Handler");
		checkNull(eventClass, "Event");
		handlers.remove(eventClass, handler);
	}
	
	/**
	 * This methods register an <EventHandler,EventClass> pair so that when an event
	 * is sent on the bus the handler is called to handle the event.
	 * 
	 * @param listener
	 * @param eventSourceClass
	 */
	public void registerListener(EventListener listener, Class<?> eventSourceClass) {
		checkNull(listener, "Listener");
		checkNull(eventSourceClass, "Event source class");
		listeners.putIfAbsent(eventSourceClass, listener);
	}

	/**
	 * Unregister the listener. It is important to remove all the listeners as soon as they
	 * are not necessary. A good practice is to register onResume and unregister onPause.
	 * 
	 * @param listener
	 * @param eventSourceClass
	 */
	public void unregisterListener(EventListener listener, Class<?> eventSourceClass) {
		checkNull(listener, "Listener");
		checkNull(eventSourceClass, "Event source class");
		listeners.remove(eventSourceClass, listener);
	}

	/** 
	 * It sends the event, using the current thread, to the handler registered for this particular
	 * class of events.
	 * It is not checking for listeners as the event source is not defined.
	 * If the event is sticky it is storing it in a map waiting for the first handler to register
	 * against it.
	 * 
	 * @param event
	 */
	public void send(Event event) {
		checkNull(event, "Event");
		sendToHandlers(event);
	}
	
	/** 
	 * It will call onEvent for the listener registered against the eventSourceClass.
	 * It then sends the event, using the current thread, to the handler registered for this particular
	 * class of events.
	 * If the event is sticky it will store it in a map waiting for the first handler to register
	 * against it.
	 * 
	 * @param event
	 * @param eventSourceClass
	 */
	public void send(Event event, Class<?> eventSourceClass) {
		checkNull(event, "Event");
		checkNull(eventSourceClass, "Event source class");
		if(listeners.containsKey(eventSourceClass)) {
			listeners.get(eventSourceClass).onEvent(event);
		}
		sendToHandlers(event);
	}
	
	/**
	 * Similar to send but call the handler on a separate thread from the sender.
	 * 
	 * @param event
	 */
	public void asyncSend(Event event) {
		throw new RuntimeException("Functionality not implemented yet");
	}
	
	/**
	 * Similar to send but call the listener and the handler on a separate thread from the sender.
	 * 
	 * @param event
	 */
	public void asyncSend(Event event, Class<?> eventSourceClass) {
		throw new RuntimeException("Functionality not implemented yet");
	}
	
	private void sendToHandlers(Event event) {
		if(handlers.containsKey(event.getClass())) {
			handlers.get(event.getClass()).handle(event);
			return;
		}
		if(event instanceof Event.Sticky) {
			stickyEvents.putIfAbsent(event.getClass(), event);
		}
	}
	
	private void checkNull(Object obj, String paramName) {
		if(obj == null) {
			throw new IllegalArgumentException(paramName + " can't be null!");
		}
	}

}