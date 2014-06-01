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
package com.luigiagosti.seba.utils;

import android.os.Handler;

import com.luigiagosti.seba.Event;
import com.luigiagosti.seba.EventBus;
import com.luigiagosti.seba.EventHandler;
import com.luigiagosti.seba.EventHandler.OnMainThread;

public class SendingEventFromIntentServiceToActivity {

    public static class Application {

        private static EventBus eventBus;

        public void onCreate() {
            eventBus = new EventBus();
        }

        public static final EventBus getEventBus() {
            return eventBus;
        }
    }

    public static class Activity {

        private OnMainThread eventHandler;

        public void onResume() {
            eventHandler = new EventHandler.OnMainThread( new Handler() ) {
                @Override
                public void handleAnsyc(Event event) {

                }
            };
            Application.getEventBus().registerHandler( eventHandler, CustomEvent.class );
        }

        public void onPause() {
            Application.getEventBus().unregisterHandler( eventHandler, CustomEvent.class );
        }
    }

    public static class IntentService {

        public EventBus eventBus;

        public void onCreate() {
            eventBus = Application.getEventBus();
            eventBus.send( new CustomEvent() {
            }, this.getClass() );
        }
    }

    public static class CustomEvent implements Event {

    }
}
