package com.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceUtil {

    public String extractCommand(String request){
        try {
            request = request.trim();
            String []strings = request.split(" ");
            return strings[0];
        }catch (Exception e){
            System.out.println("extractCommand exception");
        }
        return null;
    }

    //单聊 send -p 234567  hello world!
    //群聊 send -g 11223   hello buddy
    public Map<String, Object> parseChatMessage(String request){
        Map<String, Object> info = new HashMap<String, Object>();
        try{
            if(request.length() < 8){
                return null;
            }
            //send -p
            info.put("mode", request.charAt(6));
            StringBuilder sb = new StringBuilder();
            int i = 8;
            for(; i < request.length(); ++i){
                char c = request.charAt(i);
                if(c == ' '){
                    break;
                }
                sb.append(c);
            }
            info.put("friend", sb.toString());
            //没有输入消息
            //i+1 跳过message的前一个空格
            if(i++ == request.length()){
                info.put("exception", "please enter some message");
                return info;
            }
            String msg = request.substring(i);
            info.put("msg", msg);
            return info;
        }catch (Exception e){
            System.out.println("parseChatMessage Exception");
            info.put("exception", "system exception");
        }
        return null;
    }
}
