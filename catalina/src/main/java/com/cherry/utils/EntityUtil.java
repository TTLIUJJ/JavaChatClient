package com.cherry.utils;

public class EntityUtil {
    public static final String USER_SET = "USER_SET";
    public static final String ALIVE_SET = "ALIVE_SET";

    public static String getUserKey(String qq){
        return "user:" + qq;
    }


    //在线的地址记录使用列表, 记录的东西如下：
    // userId, [time] msg1, [time] msg2, [time] msg3
    public static String getActiveAddressKey(String ip, String port){
        return "ACTIVE_ADDRESS:" + ip + ":" + port;
    }

    public static String getMyFriendKey(String userId){
        return "MY_FRIENDS_" + userId;
    }
}
