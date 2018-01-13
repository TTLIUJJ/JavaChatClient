package com.cherry.eventHandler;

import com.cherry.centralServerSystem.ServerService;
import com.cherry.utils.EntityUtil;
import com.cherry.utils.JedisUtil;

import java.util.HashMap;
import java.util.Map;

public class RegisterHandler implements EventHandler{
    private JedisUtil jedisUtil = new JedisUtil();
    private ServerService serverService = new ServerService();
    private static String cmd = "register";
    private boolean authority = false;

    public EventState processEvent(Map<String, String> info){
        String userId = info.get("u");
        String nickname = info.get("n");
        String password = info.get("p");
        if(!cmd.equals(serverService.getCommand(info)) || userId.equals("") || nickname.equals("") || password.equals("")){
            return EventState.COMMAND_ERROR;
        }
        try{
            if(jedisUtil.sismember(EntityUtil.USER_SET, userId)){
                return EventState.USER_IS_EXIST;
            }
            jedisUtil.sadd(EntityUtil.USER_SET, userId);
            Map<String, String> hash = new HashMap<String, String>();
            hash.put("nickname", nickname);
            hash.put("password", password);
            jedisUtil.hmset(EntityUtil.getUserKey(userId), hash);
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
