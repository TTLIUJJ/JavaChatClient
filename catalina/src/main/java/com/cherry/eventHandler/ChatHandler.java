package com.cherry.eventHandler;

import com.cherry.centralServerSystem.ServerService;
import com.cherry.utils.JedisUtil;
import com.cherry.utils.UserModel;

import java.util.Map;

public class ChatHandler implements EventHandler{
    private JedisUtil jedisUtil = new JedisUtil();
    private ServerService serverService = new ServerService();
    private static String cmd = "chat";
    private boolean authority = true;

    public EventState processEvent(Map<String, String> info) {
        String arg = info.get("l");
        if(!cmd.equals(serverService.getCommand(info)) || arg.equals("")){
            return EventState.COMMAND_ERROR;
        }
        try{
            return chatWithFriend(info);
        }catch (Exception e){
            System.out.println("...");
        }
        return EventState.ERROR;
    }

    public boolean requireAuthority() {
        return authority;
    }



    private EventState chatWithFriend(Map<String, String> info){
        //单人聊天系统
        //服务器返回 'OK\r\n' 给主动聊天者, 主动聊天者创建一个UDP服务器,
        //                              使用和TCP一样的端口号
        //                  发送给被动聊天者：主动聊天者的ip:port,

        UserModel user = serverService.parseUserByInfo(info);
        if(user == null){
            return EventState.ERROR;
        }
        sendHostInfoToClent(user);
        return EventState.OK;
    }

    private void createChatRoom(){

    }

    private void addInChatRoom(){

    }

    private void chatInTheRoom(){

    }

    private String sendHostInfoToClent(UserModel user){
        System.out.println("command client startup a udp socket");
        System.out.println(user.getIp() + ":"+user.getPort() + ", " + user.getNickname() + ", " + user.getUserId());
        return null;
    }
}


