package com.cherry.eventHandler;

public enum EventType {
    REGISTER("RegisterHandler", 1),
    LOGIN("LoginHandler", 2),
    ADD("AddFriendHandler", 3),
    QUERY("QueryHandler", 4),
    CHAT("ChatHandler", 5);

    private String eventHandler;
    private int index;

    private EventType(String eventHandler, int index){
        this.eventHandler = eventHandler;
        this.index = index;
    }

    @Override
    public String toString(){
        return "eventType: " + this.eventHandler + "(" + this.index + ")";
    }

    public String getReflectName(){
        return "com.cherry.eventHandler." + this.eventHandler;
    }
}
