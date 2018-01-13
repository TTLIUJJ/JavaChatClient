package com.cherry.centralServerSystem;

import com.cherry.eventHandler.EventState;
import com.cherry.utils.EntityUtil;
import com.cherry.utils.JedisUtil;
import com.cherry.utils.UserModel;

import java.util.Map;

//信息格式
//注册：register -u 123456 -n ackerman -p cherry123
//登录：login -u 123456 -p cherry123
//添加：add   -u 777666     添加好友
//查询：query -a     所有好友
//           -l     在线好友
//聊天：chat  -p     单聊
//           -g     群聊
//退出：quit
//菜单：man
public class ServerService {
    private JedisUtil jedisUtil = new JedisUtil();

    public EventState extractMessage(char[] chars, Map<String, String> map){
        int i = parseCmd(chars, 0, map);
        try{
            while(i < chars.length){
                i = skipWhiteSpace(chars, i);
                i = parseArgs(chars, i, map);
            }

        }catch (Exception e){
            e.getMessage();
        }

        return EventState.OK;
    }

    private int parseCmd(char []chars, int i, Map<String, String> info){
        StringBuilder sb = new StringBuilder();
        for( ; i < chars.length; ++i){
            if(chars[i] == ' '){
                info.put("cmd", sb.toString());
                return ++i;
            }
            sb.append(chars[i]);
        }
        return chars.length;
    }

    private int parseArgs(char []chars, int i, Map<String, String> info){
        StringBuilder sb = new StringBuilder();
        String arg = "";
        for( ; i < chars.length; ++i){
            if(chars[i] == ' '){
                ++i;
                break;
            }
            else if(chars[i] == '-'){
                if(++i == chars.length){
                    return chars.length;
                }
                arg = String.valueOf(chars[i]);
                i = i + 1;  // -p  跳过 'p'和p之后的' '
            }
            else{
                sb.append(chars[i]);
            }
        }
        info.put(arg, sb.toString());
        return i;
    }

    private int skipWhiteSpace(char []chars, int i){
        while(i != chars.length &&
                (chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\n' || chars[i] == '\r')){

            ++i;
        }
        return i;
    }

    public String getCommand(Map<String, String> info){
        return info.get("cmd");
    }


    //info是一个map类型, 其中包含ip, port的键
    //从一个激活的active_address:xx.xxx.xx.x:xx 中提取userId
    public String getUserId(Map<String, String> info){
        try{
            String ip = info.get("ip");
            String port = info.get("port");
            String activeAddressKey = EntityUtil.getActiveAddressKey(ip, port);
            String userId = jedisUtil.lindex(activeAddressKey, 0);
            return userId;
        }catch (Exception e){

        }
        return null;
    }
    public UserModel parseUserByInfo(Map<String, String> info){
        UserModel user = new UserModel();
        try {
            String ip = info.get("ip");
            String port = info.get("port");
            String activeAddressKey = EntityUtil.getActiveAddressKey(ip, port);
            String userId = jedisUtil.lindex(activeAddressKey, 0);
            String userKey = EntityUtil.getUserKey(userId);
            String nickname = jedisUtil.hmget(userKey, "nickname");

            user.setIp(ip).setPort(port).setUserId(userId).setNickname(nickname);
            return user;
        }catch (Exception e){
            System.out.println("...");
        }
        return null;
    }
}