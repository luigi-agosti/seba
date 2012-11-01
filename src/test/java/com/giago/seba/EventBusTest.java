package com.giago.seba;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.giago.seba.Event;
import com.giago.seba.EventBus;
import com.giago.seba.handlers.EventHandler;
import com.giago.seba.listeners.EventListener;

public class EventBusTest {

	private class EventSource {}	
	private class AnotherEventSource {}
	
	private EventBus eventBus = new EventBus();
	private Event event;
	private EventHandler handler;
	private EventHandler anotherHandler;
	private EventListener listener;
	
	@Before
	public void beforeEachTest() {
		event = mock(Event.class);
		handler = mock(EventHandler.class);
		anotherHandler = mock(EventHandler.class);
		listener = mock(EventListener.class);
	}
	
	@Test
	public void shouldRegisterHandlerForEvent() {
		eventBus.registerHandler(event.getClass(), handler);
		eventBus.send(EventSource.class, event);
		
		verify(handler, times(1)).handle(event);
	}
	
	@Test
	public void shouldUnregisterHandlerForEvent() {
		eventBus.registerHandler(event.getClass(), handler);
		eventBus.unregisterHandler(event.getClass(), handler);
		eventBus.send(EventSource.class, event);
		
		verify(handler, never()).handle(event);
	}
	
	@Test
	public void shouldKeepOnlyLastRegisteredHandler() {
		eventBus.registerHandler(event.getClass(), handler);
		eventBus.registerHandler(event.getClass(), anotherHandler);
		eventBus.send(EventSource.class, event);
		
		verify(handler, never()).handle(event);
		verify(anotherHandler, times(1)).handle(event);
	}
	
	@Test
	public void shouldAddListenerForEventSource() {
		eventBus.addListener(EventSource.class, listener);
		eventBus.send(EventSource.class, event);
		
		verify(listener, times(1)).onEvent(event);
	}
	
	@Test
	public void shouldAddSameListenerTwoTimesForEventSourceAndStillReceiveOneCall() {
		eventBus.addListener(EventSource.class, listener);
		eventBus.addListener(EventSource.class, listener);
		eventBus.send(EventSource.class, event);
		
		verify(listener, times(1)).onEvent(event);
	}
	
	@Test
	public void shouldSendTheEventOnlyToThe() {
		eventBus.addListener(EventSource.class, listener);
		eventBus.send(AnotherEventSource.class, event);
		
		verify(listener, never()).onEvent(event);
	}
	
	@Test
	public void shouldRemoveListenerForEventSource() {
		eventBus.addListener(EventSource.class, listener);
		eventBus.removeListener(EventSource.class, listener);
		
		verify(listener, never()).onEvent(event);
	}
	
}
