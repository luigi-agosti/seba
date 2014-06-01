package com.luigiagosti.seba;

import com.luigiagosti.seba.utils.AsyncEventProducer;
import com.luigiagosti.seba.utils.ConcurrencyBaseEventBusTest;
import com.luigiagosti.seba.utils.EventConsumer;
import com.luigiagosti.seba.utils.EventProducer;
import com.luigiagosti.seba.utils.MyEvent.MyEvent1;
import com.luigiagosti.seba.utils.MyEvent.MyEvent2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AsyncEventBusTest extends ConcurrencyBaseEventBusTest {

    private EventProducer eventProducer;
    private EventConsumer eventConsumer;
    private EventProducer eventProducer1;
    private EventConsumer eventConsumer1;
    private EventProducer eventProducer2;
    private EventConsumer eventConsumer2;

    @Test
    public void shouldReceiveEventSentAsynchronously() {
        EventBus bus = new EventBus();
        eventConsumer = new EventConsumer( bus );
        eventProducer = new AsyncEventProducer( bus, 10 );
        start( eventConsumer );
        start( eventProducer );

        waitForFinish( eventProducer );
        assertEquals( 10, eventConsumer.getEventConsumedCounter() );
    }

    @Test
    public void shouldReceiveEventFromDifferentThread() {
        EventBus bus = new EventBus();
        eventConsumer = new EventConsumer( bus );
        eventConsumer1 = new EventConsumer( bus, MyEvent1.class );
        eventConsumer2 = new EventConsumer( bus, MyEvent2.class );
        eventProducer = new AsyncEventProducer( bus, LARGE_NUMBER_OF_EVENT );
        eventProducer1 = new AsyncEventProducer( bus, LARGE_NUMBER_OF_EVENT, MyEvent1.class );
        eventProducer2 = new AsyncEventProducer( bus, LARGE_NUMBER_OF_EVENT, MyEvent2.class );
        start( eventConsumer );
        start( eventConsumer1 );
        start( eventConsumer2 );
        start( eventProducer );
        start( eventProducer1 );
        start( eventProducer2 );

        waitForFinish( eventProducer, eventProducer1, eventProducer2 );
        waitForConsumers( eventConsumer, eventConsumer1, eventConsumer2 );
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotExpectNullEvent() {
        EventBus bus = new EventBus();
        bus.asyncSend( null );
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotExpectNullEventOnAsyncSendWithSource() {
        EventBus bus = new EventBus();
        bus.asyncSend( null, EventProducer.class );
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotExpectNullEventClassOnUnregisterListener() {
        EventBus bus = new EventBus();
        bus.asyncSend( new Event() {
        }, null );
    }
}
