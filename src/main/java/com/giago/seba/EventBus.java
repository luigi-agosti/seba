package com.giago.seba;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.giago.seba.handlers.EventHandler;
import com.giago.seba.listeners.EventListener;

public class EventBus {
	
	private ConcurrentMap<Class<? extends Event>, EventHandler> handlers;
	private ConcurrentMap<Class<?>, Set<EventListener>> listeners;
	
	public EventBus() {
	    handlers = new ConcurrentHashMap<Class<? extends Event>, EventHandler>();
	    listeners = new ConcurrentHashMap<Class<?>, Set<EventListener>>();
    }

	public void registerHandler(Class<? extends Event> eventClass, EventHandler handler) {
		handlers.put(eventClass, handler);
	}

	public void unregisterHandler(Class<? extends Event> eventClass, EventHandler handler) {
		handlers.remove(eventClass, handler);
	}

	public void send(Class<?> eventSourceClass, Event event) {
		if(listeners.containsKey(eventSourceClass)) {
			Set<EventListener> ls = listeners.get(eventSourceClass);
			for(EventListener l : ls) {
				l.onEvent(event);
			}
		}
		if(handlers.containsKey(event.getClass())) {
			EventHandler handler = handlers.get(event.getClass());
			handler.handle(event);
			return;
		}
	}

	public void addListener(Class<?> eventSourceClass, EventListener listener) {
		if(listeners.containsKey(eventSourceClass)) {
			Set<EventListener> ls = listeners.get(eventSourceClass);
			ls.add(listener);
			return;
		}
		Set<EventListener> ls = new HashSet<EventListener>();
		ls.add(listener);
		listeners.put(eventSourceClass, ls);
	}

	public void removeListener(Class<?> eventSourceClass, EventListener listener) {
		// TODO Auto-generated method stub
		
	}

}