package com.luigiagosti.seba;

import com.luigiagosti.seba.utils.ConcurrencyBaseEventBusTest;
import com.luigiagosti.seba.utils.EventConsumer;
import com.luigiagosti.seba.utils.EventProducer;
import com.luigiagosti.seba.utils.MyEvent;

import org.junit.Test;

public class ConcurrencyEventBusTest extends ConcurrencyBaseEventBusTest {

    private EventProducer eventProducer;
    private EventConsumer eventConsumer;
    private EventProducer eventProducer1;
    private EventConsumer eventConsumer1;
    private EventProducer eventProducer2;
    private EventConsumer eventConsumer2;

    @Test
    public void shouldSupportDifferentThreadSendAndReceive() {
        EventBus bus = new EventBus();
        eventConsumer = new EventConsumer( bus );
        eventProducer = new EventProducer( bus, 10 );
        start( eventConsumer );
        start( eventProducer );

        waitForFinish( eventProducer );
    }

    @Test
    public void shouldSupportMultipleThreadForSendersAndHanlders() {
        EventBus bus = new EventBus();
        eventConsumer = new EventConsumer( bus );
        eventConsumer1 = new EventConsumer( bus, MyEvent.MyEvent1.class );
        eventConsumer2 = new EventConsumer( bus, MyEvent.MyEvent2.class );
        eventProducer = new EventProducer( bus, LARGE_NUMBER_OF_EVENT );
        eventProducer1 = new EventProducer( bus, LARGE_NUMBER_OF_EVENT, MyEvent.MyEvent1.class );
        eventProducer2 = new EventProducer( bus, LARGE_NUMBER_OF_EVENT, MyEvent.MyEvent2.class );
        start( eventConsumer, eventConsumer1, eventConsumer2 );
        start( eventProducer, eventProducer1, eventProducer2 );

        waitForFinish( eventProducer, eventProducer1, eventProducer2 );
        checkCosumers( eventConsumer, eventConsumer1, eventConsumer2 );
    }
}
