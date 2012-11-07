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

/**
 * Listeners are used to watch a particular source of events.
 * All the events sent with that particular class are sent over 
 * to the listener registered for it by the eventBus.
 * At the moment the library is not providing any other type 
 * of listeners although it is in theory very easy to implement 
 * a listener working on the main thread.
 */
public interface EventListener {

	void onEvent(Event event);

}
