package com.giago.seba;

import android.os.Handler;

import com.giago.seba.handlers.MainThreadEventHandler;

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
		
		private MainThreadEventHandler eventHandler;
		
		public void onResume() {
			eventHandler = new MainThreadEventHandler(new Handler()) {
				@Override
				public void handleAnsyc(Event event) {
					
				}
			};
			Application.getEventBus().registerHandler(CustomEvent.class, eventHandler);
		}
		
		public void onPause() {
			Application.getEventBus().unregisterHandler(CustomEvent.class, eventHandler);
		}
	}
	
	public static class IntentService {
		
		public EventBus eventBus;
		
		public void onCreate() {
			eventBus = Application.getEventBus();
			eventBus.send(this.getClass(), new CustomEvent(){});
		}
		
	}
	
	public static class CustomEvent implements Event {
		
	}

}
