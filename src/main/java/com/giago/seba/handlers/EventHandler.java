package com.giago.seba.handlers;

import com.giago.seba.Event;

/**
 * Default EventHandler is using the thread that
 * generated the event to handle the event.
 */
public interface EventHandler {

	boolean handle(Event event);

}
