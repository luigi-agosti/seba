package com.luigiagosti.seba.utils;

import com.luigiagosti.seba.Event;
import com.luigiagosti.seba.EventBus;
import com.luigiagosti.seba.EventHandler;

public class EventConsumer implements Runnable, EventHandler {

    private EventBus bus;
    private int eventConsumedCounter;
    private Class<? extends Event> eventClass;

    public EventConsumer(EventBus bus) {
        this( bus, MyEvent.class );
    }

    public EventConsumer(EventBus bus, Class<? extends Event> eventClass) {
        this.eventClass = eventClass;
        this.bus = bus;
    }

    @Override
    public void run() {
        bus.registerHandler( this, eventClass );
    }

    @Override
    public boolean handle(Event event) {
        eventConsumedCounter++;
        return true;
    }

    public int getEventConsumedCounter() {
        return eventConsumedCounter;
    }
}
