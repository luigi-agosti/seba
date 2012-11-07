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

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.luigiagosti.seba.Event;
import com.luigiagosti.seba.EventBus;
import com.luigiagosti.seba.EventHandler;
import com.luigiagosti.seba.EventListener;

public class EventBusTest {

	private class EventSource {}	
	private class AnotherEventSource {}
	
	private EventBus eventBus = new EventBus();
	private Event event;
	private Event.Sticky stickyEvent;
	private EventHandler handler;
	private EventHandler anotherHandler;
	private EventListener listener;
	
	@Before
	public void beforeEachTest() {
		event = mock(Event.class);
		stickyEvent = mock(Event.Sticky.class);
		handler = mock(EventHandler.class);
		anotherHandler = mock(EventHandler.class);
		listener = mock(EventListener.class);
	}
	
	@Test
	public void shouldRegisterHandlerForEvent() {
		eventBus.registerHandler(handler, event.getClass());
		eventBus.send(event, EventSource.class);
		
		verify(handler, times(1)).handle(event);
	}
	
	@Test
	public void shouldUnregisterHandlerForEvent() {
		eventBus.registerHandler(handler, event.getClass());
		eventBus.unregisterHandler(handler, event.getClass());
		eventBus.send(event);
		
		verify(handler, never()).handle(event);
	}
	
	@Test
	public void shouldKeepOnlyFirstRegisteredHandler() {
		eventBus.registerHandler(handler, event.getClass());
		eventBus.registerHandler(anotherHandler, event.getClass());
		eventBus.send(event);
		
		verify(anotherHandler, never()).handle(event);
		verify(handler, times(1)).handle(event);
	}
	
	@Test
	public void shouldAddListenerForEventSource() {
		eventBus.registerListener(listener, EventSource.class);
		eventBus.send(event, EventSource.class);
		
		verify(listener, times(1)).onEvent(event);
	}
	
	@Test
	public void shouldAddSameListenerTwoTimesForEventSourceAndStillReceiveOneCall() {
		eventBus.registerListener(listener, EventSource.class);
		eventBus.registerListener(listener, EventSource.class);
		eventBus.send(event, EventSource.class);
		
		verify(listener, times(1)).onEvent(event);
	}
	
	@Test
	public void shouldSendTheEventOnlyToThe() {
		eventBus.registerListener(listener, EventSource.class);
		eventBus.send(event, AnotherEventSource.class);
		
		verify(listener, never()).onEvent(event);
	}
	
	@Test
	public void shouldRemoveListenerForEventSource() {
		eventBus.registerListener(listener, EventSource.class);
		eventBus.unregisterListener(listener, EventSource.class);
		
		verify(listener, never()).onEvent(event);
	}
	
	@Test
	public void shouldNotConsiderListenersIfEventsAreSentWithoutSource() {
		eventBus.registerListener(listener, EventSource.class);
		eventBus.send(event);
		
		verify(listener, never()).onEvent(event);
	}
	
	@Test
	public void shouldSendStickyEventEvenIfHandlerIsRegisteredAfterEvent() {
		eventBus.send(stickyEvent);
		eventBus.registerHandler(handler, stickyEvent.getClass());
		
		verify(handler, times(1)).handle(stickyEvent);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullEventsOnSendMethod() {
		eventBus.send(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullEventOnSendMethodWithSource() {
		eventBus.send(null, EventSource.class);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullEventSource() {
		eventBus.send(event, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullHanldersOnRegister() {
		eventBus.registerHandler(null, event.getClass());
	}
	 
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullHanldersOnUnregister() {
		eventBus.unregisterHandler(null, event.getClass());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullEventClassOnRegister() {
		eventBus.registerHandler(handler, null);
	}
	 
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullEventClassOnUnregister() {
		eventBus.unregisterHandler(handler, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullListenerOnRegister() {
		eventBus.registerListener(null, event.getClass());
	}
	 
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullListenersOnUnregister() {
		eventBus.unregisterListener(null, event.getClass());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullEventClassOnRegisterListener() {
		eventBus.registerListener(listener, null);
	}
	 
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotExpectNullEventClassOnUnregisterListener() {
		eventBus.unregisterListener(listener, null);
	}
}
