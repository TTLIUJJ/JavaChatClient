package com.cherry.eventHandler;

import java.util.Map;


public interface EventHandler {
    EventState processEvent(Map<String, String> info);
    boolean requireAuthority();
//    String responseMessage(EventState code);
//    Map<String, Object> extractMessage(char []chars);
}
