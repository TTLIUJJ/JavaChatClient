package com.cherry.eventHandler;

import com.cherry.centralServerSystem.ServerService;
import com.cherry.utils.EntityUtil;
import com.cherry.utils.JedisUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements EventHandler {
    private JedisUtil jedisUtil = new JedisUtil();
    private ServerService serverService = new ServerService();
    private static String cmd = "login";
    private boolean authority = false;

    public EventState processEvent(Map<String, String> info){

        String userId = info.get("u");
        String password = info.get("p");
        if(!cmd.equals(info.get("cmd")) || userId.equals("") || password.equals("")){
            return EventState.COMMAND_ERROR;
        }
        try{
            if(!jedisUtil.sismember(EntityUtil.USER_SET, userId)){
                return EventState.LOGIN_USER_NOT_EXIST;
            }

            String userKey = EntityUtil.getUserKey(userId);
            if(!password.equals(jedisUtil.hmget(userKey, "password"))){
                return EventState.LOGIN_PASSWORD_ERROR;
            }

            String ip = info.get("ip");
            String port = info.get("port");
            if(ip == null || port == null){
                return EventState.ERROR;
            }
            Map<String, String> address = new HashMap<String, String>();
            address.put("ip", ip);
            address.put("port", port);
            // 这两行代码要保证事务
            // 需要保证添加ip:port对应的userId, 和用户已经登陆
            jedisUtil.hmset(userKey, address);
            jedisUtil.sadd(EntityUtil.ALIVE_SET, userId);

            //active_address
            //判断ip:port是否被激活, 不允许同意重复登陆
            String activeKey = EntityUtil.getActiveAddressKey(ip, port);
            if(jedisUtil.exists(activeKey)){
                return EventState.LOGIN_ADDRESS_REPEAT;
            }
            else {
                jedisUtil.rpush(activeKey, userId);
            }
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
