package com.cherry.centralServerSystem;

import com.cherry.eventHandler.EventHandler;
import com.cherry.eventHandler.EventState;
import com.cherry.utils.EntityUtil;
import com.cherry.utils.JedisUtil;

import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class JaynaController {
     private ServerService serverService;
     private EventHandler handler;
     private Map<String, String> info;
     private SelectionKey key;
     private JedisUtil jedisUtil;

     public JaynaController(EventHandler handler, SelectionKey key){
         this.handler = handler;
         this.key = key;
         serverService = new ServerService();
         info = new HashMap<String, String>();
         jedisUtil = new JedisUtil();
     }

     public String fireEvent(char []chars){
         EventState state;

         if((state = serverService.extractMessage(chars, info)) == EventState.OK
                 || state == EventState.RESPONSE_OK){
             extractAddress();
             //判断是否需要登陆权限
             if(handler.requireAuthority()){
                 String ip = info.get("ip");
                 String port = info.get("port");
                 String activeAddressKey = EntityUtil.getActiveAddressKey(ip, port);
                 if(!jedisUtil.exists(activeAddressKey)){
                     return EventState.LOGIN_AUTHORITY.getMsg();
                 }
             }
             state = handler.processEvent(info);
         }

         return state.getMsg();
     }

     public void extractAddress(){
         try {
             SocketChannel channel = (SocketChannel) key.channel();
             Socket socket = channel.socket();
             String str = socket.getRemoteSocketAddress().toString();

             String[] strings =  str.split(":");
             String ip = strings[0].substring(1);
             String port = strings[1];
             info.put("ip", ip);
             info.put("port", port);
         }catch (Exception e){
             e.getStackTrace();
         }
     }

     public static void removeInfo(SelectionKey key){
         SocketChannel channel = (SocketChannel) key.channel();
         Socket socket = channel.socket();
         String str = socket.getRemoteSocketAddress().toString();

         String[] strings =  str.split(":");
         String ip = strings[0].substring(1);
         String port = strings[1];


         JedisUtil jedisUtil = new JedisUtil();
         //删除在线地址
         String activeKey = EntityUtil.getActiveAddressKey(ip, port);
         String userId = jedisUtil.lpop(activeKey);
         jedisUtil.del(activeKey);
         //删除在线Id
         jedisUtil.srem(EntityUtil.ALIVE_SET, userId);
     }


}
