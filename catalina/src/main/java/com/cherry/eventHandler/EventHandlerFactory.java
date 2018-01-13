package com.cherry.eventHandler;

public class EventHandlerFactory {
    public static EventHandler getEventHandler(EventType eventType){
        EventHandler handler = null;
        try{
            handler = (EventHandler) Class.forName(eventType.getReflectName()).newInstance();
        }catch (Exception e){
            e.getStackTrace();
        }
        return handler;
    }
}
