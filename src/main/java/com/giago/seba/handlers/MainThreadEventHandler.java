package com.giago.seba.handlers;

import android.os.Handler;

import com.giago.seba.Event;

public abstract class MainThreadEventHandler implements EventHandler {
    
    private Handler handler;
    
    public MainThreadEventHandler(Handler handler) {
        this.handler = handler;
    }
    
    @Override
    public boolean handle(final Event event) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                handleAnsyc(event);
            }
        });
        return true;
    }
    
    public abstract void handleAnsyc(Event event);
    
}