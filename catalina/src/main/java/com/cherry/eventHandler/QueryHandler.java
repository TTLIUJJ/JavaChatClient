package com.cherry.eventHandler;

import com.alibaba.fastjson.JSONObject;
import com.cherry.centralServerSystem.ServerService;
import com.cherry.utils.EntityUtil;
import com.cherry.utils.JedisUtil;
import com.cherry.utils.UserModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryHandler implements EventHandler {
    private JedisUtil jedisUtil = new JedisUtil();
    private ServerService serverService = new ServerService();
    private static String cmd = "query";
    private boolean authority = true;

    public EventState processEvent(Map<String, String> info) {
        //指令 query -a 或者 query -l
        //info信息: cmd-query (a-null 或者 l-null)
        if(!cmd.equals(info.get("cmd"))){
            return EventState.COMMAND_ERROR;
        }
        try{
            String userId = serverService.getUserId(info);
            if(userId == null){
                // query操作是经过验证的, 在redis中应该是ACTIVE_ADDRESS:xxx.xx.xx.x:port
                return EventState.ERROR;
            }
            if (info.containsKey("a")){
                return qeuryAllFriends(userId);
            }
            else if(info.containsKey("l")){
                return queryAliveFriends(userId);
            }
            else{
                return EventState.COMMAND_ERROR;
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        return EventState.ERROR;
    }

    public boolean requireAuthority() {
        return this.authority;
    }

    private EventState queryAliveFriends(String userId){
        List<UserModel> userList = serverService.getAliveFriendListInfo(userId);
        EventState.RESPONSE_OK.setMsg("OK\r\n" + JSONObject.toJSONString(userList));
        return EventState.RESPONSE_OK;
    }

    private EventState qeuryAllFriends(String userId){
        String myFriendsKey = EntityUtil.getMyFriendKey(userId);
        Set<String> friendSet = jedisUtil.smembers(myFriendsKey);
        EventState.RESPONSE_OK.setMsg("All Friends Id " + friendSet.toString() + "\r\n");
        return EventState.RESPONSE_OK;
    }

}
