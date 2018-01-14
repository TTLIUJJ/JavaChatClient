package com.service;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

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

    public String extractWithQueryInfo(String response, Map<String, UserModel> onlineFriends){
        int pos = 0;
        for(int i = 0; i < response.length(); ++i){
            if (response.charAt(i) == '\n'){
                pos = i;
                break;
            }
        }
        String ret = response.substring(0, ++pos);
        String msg = response.substring(pos, response.length());
        String []strings = msg.split(";");
        for(String jsonModel : strings){
            UserModel userModel = JSONObject.parseObject(jsonModel, UserModel.class);
            onlineFriends.put(userModel.getUserId(), userModel);
        }
        return ret;
    }
}
