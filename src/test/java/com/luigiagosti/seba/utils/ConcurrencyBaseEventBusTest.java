package com.luigiagosti.seba.utils;

import com.jayway.awaitility.Duration;

import org.junit.Assert;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.with;
import static org.junit.Assert.assertEquals;

public class ConcurrencyBaseEventBusTest {

    protected static final int LARGE_NUMBER_OF_EVENT = 1000;

    protected void checkCosumers(EventConsumer... eventConsumers) {
        for (EventConsumer ec : eventConsumers) {
            assertEquals( LARGE_NUMBER_OF_EVENT, ec.getEventConsumedCounter() );
        }
    }

    protected void waitForConsumers(final EventConsumer... eventConsumers) {
        try {
            with().pollInterval( Duration.ONE_HUNDRED_MILLISECONDS ).and()
                    .with().pollDelay( 20, TimeUnit.MILLISECONDS ).await( "event producer finished" )
                    .atMost( 2, TimeUnit.SECONDS ).until( new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    for (EventConsumer ec : eventConsumers) {
                        if (ec.getEventConsumedCounter() != LARGE_NUMBER_OF_EVENT) {
                            return false;
                        }
                    }
                    return true;
                }
            } );
        } catch (Exception re) {
            re.printStackTrace();
            Assert.fail( re.getMessage() );
        }
    }

    protected void waitForFinish(final EventProducer... eventProducers) {
        try {
            with().pollInterval( Duration.ONE_HUNDRED_MILLISECONDS ).and()
                    .with().pollDelay( 20, TimeUnit.MILLISECONDS ).await( "event producer finished" )
                    .atMost( 2, TimeUnit.SECONDS ).until( new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    for (EventProducer ep : eventProducers) {
                        if (!ep.finished()) {
                            return false;
                        }
                    }
                    return true;
                }
            } );
        } catch (Exception re) {
            re.printStackTrace();
            Assert.fail( re.getMessage() );
        }
    }

    protected void start(Runnable... rs) {
        for (Runnable r : rs) {
            new Thread( r ).start();
        }
    }
}
