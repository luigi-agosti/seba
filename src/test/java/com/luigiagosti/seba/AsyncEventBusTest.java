package com.luigiagosti.seba;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.luigiagosti.seba.utils.AsyncEventProducer;
import com.luigiagosti.seba.utils.ConcurrencyBaseEventBusTest;
import com.luigiagosti.seba.utils.EventConsumer;
import com.luigiagosti.seba.utils.EventProducer;
import com.luigiagosti.seba.utils.MyEvent.MyEvent1;
import com.luigiagosti.seba.utils.MyEvent.MyEvent2;

public class AsyncEventBusTest extends ConcurrencyBaseEventBusTest {
	
	private EventProducer eventProducer;
	private EventConsumer eventConsumer;
	private EventProducer eventProducer1;
	private EventConsumer eventConsumer1;
	private EventProducer eventProducer2;
	private EventConsumer eventConsumer2;
	
	@Ignore @Test
	public void shouldReceiveEventSentAsynchronously() {
		EventBus bus = new EventBus();
		eventConsumer = new EventConsumer(bus);
		eventProducer = new AsyncEventProducer(bus, 10);
		start(eventConsumer);
		start(eventProducer);
		
		waitForFinish(eventProducer);
		assertEquals(10, eventConsumer.getEventConsumedCounter());
	}
	
	@Ignore @Test
	public void shouldReceiveEventFromDifferentThread() {
		EventBus bus = new EventBus();
		eventConsumer = new EventConsumer(bus);
		eventConsumer1 = new EventConsumer(bus, MyEvent1.class);
		eventConsumer2 = new EventConsumer(bus, MyEvent2.class);
		eventProducer = new AsyncEventProducer(bus, 10);
		eventProducer1 = new AsyncEventProducer(bus, 10, MyEvent1.class);
		eventProducer2 = new AsyncEventProducer(bus, 10, MyEvent2.class);
		start(eventConsumer);
		start(eventConsumer1);
		start(eventConsumer2);
		start(eventProducer);
		start(eventProducer1);
		start(eventProducer2);
		
		waitForFinish(eventProducer, eventProducer1, eventProducer2);
		checkCosumers(eventConsumer, eventConsumer1, eventConsumer2);
	}

}
