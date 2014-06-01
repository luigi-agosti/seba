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

import android.os.Handler;

/**
 * Default EventHandler is using the thread that generated the event to handle the event.
 */
public interface EventHandler {

    boolean handle(Event event);

    /**
     * This abstract implementation of EventHandler it is useful in scenarios where an activity need to receive an event
     * and change some ui component but the event was sent on background thread (like an IntentService).
     */
    abstract class OnMainThread implements EventHandler {

        private Handler handler;

        public OnMainThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public boolean handle(final Event event) {
            handler.post( new Runnable() {
                @Override
                public void run() {
                    handleAnsyc( event );
                }
            } );
            return true;
        }

        public abstract void handleAnsyc(Event event);
    }
}
