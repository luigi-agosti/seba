package com.giago.seba.listeners;

import com.giago.seba.Event;

/**
 * Default EventHandler is using the thread that
 * generated the event to handle the event.
 */
public interface EventListener {

	void onEvent(Event event);

}
