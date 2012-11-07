package com.luigiagosti.seba.utils;

import com.luigiagosti.seba.Event;
import com.luigiagosti.seba.EventBus;

public class EventProducer implements Runnable {

	private int eventNumber;
	protected EventBus bus;
	private int count;
	private Class<?> eventClass;
	
	public EventProducer(EventBus bus, int eventNumber) {
		this(bus, eventNumber, MyEvent.class);
	}
	
	public EventProducer(EventBus bus, int eventNumber, Class<?> eventClass) {
		this.bus = bus;
		this.eventNumber = eventNumber;
		this.eventClass = eventClass;
	}

	@Override
	public void run() {
		while (count < eventNumber) {
			sendEvent(bus, produce()); 
		}
	}

	protected void sendEvent(EventBus bus, Event event) {
		bus.send(event);
	}

	private MyEvent produce() {
		MyEvent e = null;
		try {
			e = (MyEvent)eventClass.newInstance();
			e.setId(count++);
		} catch (Exception e1) {
			throw new RuntimeException();
		}
		return e;
	}

	public Boolean finished() {
		return count >= eventNumber;
	}
	
}
