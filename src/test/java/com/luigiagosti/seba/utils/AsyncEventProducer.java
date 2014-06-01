package com.luigiagosti.seba.utils;

import com.luigiagosti.seba.Event;
import com.luigiagosti.seba.EventBus;

public class AsyncEventProducer extends EventProducer {

    public AsyncEventProducer(EventBus bus, int eventNumber) {
        super( bus, eventNumber );
    }

    public AsyncEventProducer(EventBus bus, int eventNumber, Class<?> eventClass) {
        super( bus, eventNumber, eventClass );
    }

    @Override
    protected void sendEvent(EventBus bus, Event event) {
        bus.asyncSend( event );
    }
}
