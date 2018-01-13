package com.cherry.eventHandler;

public enum EventState {
    OK("OK\r\n", 0),
    COMMAND_ERROR("command error: check the manual\r\n", 1),
    ERROR("unknown error\r\n", 2),
    USER_IS_EXIST("register_error: userId exist\r\n", 3),
    LOGIN_USER_NOT_EXIST("login_error: userId is not exist\r\n", 4),
    LOGIN_PASSWORD_ERROR("login_error: password error\r\n", 5),
    LOGIN_ADDRESS_REPEAT("login_error: ip-port repeat\r\n", 6),
    LOGIN_AUTHORITY("login_authority: please login first\r\n", 7),
    ADD_FRIEND_UNREGISTER("add_error: userId not exist\r\n", 8),
    ADD_FRIEND_EXIST("add_error: friend exist\r\n", 9),
    ADD_YOURSELF("add_erro: emmmmm... can't add yourself\r\n", 10),

    RESPONSE_OK("", 34);

//    QUERY_ERROR("错误的请求", 3);

    private String msg;
    private int code;

    private EventState(String msg, int code){
        this.msg = msg;
        this.code = code;
    }

    public String getMsg(){
        return msg;
    }

    public EventState setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
