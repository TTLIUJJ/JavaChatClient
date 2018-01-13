package com.cherry.eventHandler;

import com.cherry.centralServerSystem.ServerService;
import com.cherry.utils.EntityUtil;
import com.cherry.utils.JedisUtil;

import javax.swing.text.html.parser.Entity;
import java.nio.channels.SelectionKey;
import java.util.Map;

public class AddFriendHandler implements EventHandler {
    private JedisUtil jedisUtil = new JedisUtil();
    private ServerService serverService = new ServerService();
    private static String cmd = "add";
    private boolean authority = true;

    public EventState processEvent(Map<String, String> info) {
        String friendId = info.get("u");
        if(!cmd.equals(serverService.getCommand(info)) || friendId.equals("")){
            return EventState.COMMAND_ERROR;
        }

        String ip = info.get("ip");
        String port = info.get("port");
        if(ip == null || port == null){
            return EventState.ERROR;
        }
        String activeAddressKey = EntityUtil.getActiveAddressKey(ip, port);
        String userId = jedisUtil.lindex(activeAddressKey, 0);

        if(userId.equals(friendId)){
            return EventState.ADD_YOURSELF;
        }

        try{
            if(!jedisUtil.sismember(EntityUtil.USER_SET, friendId)){
               return EventState.ADD_FRIEND_UNREGISTER;
            }
            String myFrinedKey = EntityUtil.getMyFriendKey(userId);
            if(jedisUtil.sismember(myFrinedKey, friendId)){
                return EventState.ADD_FRIEND_EXIST;
            }
            jedisUtil.sadd(myFrinedKey, friendId);
            return EventState.OK;
        }catch (Exception e){
            e.getStackTrace();
        }
        return EventState.ERROR;
    }

    public boolean requireAuthority() {
        return this.authority;
    }
}
