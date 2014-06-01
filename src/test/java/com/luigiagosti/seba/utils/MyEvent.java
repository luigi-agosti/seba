package com.luigiagosti.seba.utils;

import com.luigiagosti.seba.Event;

public class MyEvent implements Event {

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MyEvent [id=" + id + "]";
    }

    public static class MyEvent1 extends MyEvent {

        @Override
        public String toString() {
            return "MyEvent1 [id=" + getId() + "]";
        }
    }

    ;

    public static class MyEvent2 extends MyEvent {

        @Override
        public String toString() {
            return "MyEvent2 [id=" + getId() + "]";
        }
    }

    ;
}
