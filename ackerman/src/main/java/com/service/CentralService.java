package com.service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

public class CentralService{
    private static final String TCP_HOST = "101.132.181.76";
    private static final int TCP_PORT = 8080;
    private static final int UDP_PORT = 12345;

    private ServiceUtil serviceUtil = new ServiceUtil();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    //用户交流通信各式
    //单聊 send -p 234567  hello world!!!
    //群聊 send -g 11223   hello buddy
    // .
    //此条命令会 创建或者复用已存在的udp端口
    public void correspondWithJayna(){
        SocketChannel channel = null;
        try{
            InetSocketAddress socketAddress = new  InetSocketAddress(TCP_HOST, TCP_PORT);
            channel = SocketChannel.open(socketAddress);
            Socket socket = channel.socket();
            Writer writer = new OutputStreamWriter(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            ByteBuffer buffer = ByteBuffer.allocate(256);
            String line;
            while(true){
                line = scanner.nextLine();
                String command = serviceUtil.extractCommand(line);
                if(command.equals("exit")){
                    break;
                }
                if(command.equals("send")){
                    chatWithFriendService(line);
                    continue;
                }
                //往Jayna中央服务器请求数据
                writer.write(line);
                writer.flush();

                channel.read(buffer);
                String response = new String(buffer.array(), 0, buffer.position(), "UTF-8");
                System.out.println(response);
                buffer.clear();
            }
            System.out.println("close client with exit");
        }catch (IOException ioe){
            System.out.println("correspondWithJayna IOException");
        }finally {
            if(channel != null){
                try {
                    channel.socket().close();
                }catch (IOException e){
                    e.getStackTrace();
                }
            }
        }
    }



    //该UDPServer只用于接受消息
    //因为每个用户的udpServer使用约定而同的端口,

    //注意发送对于每个用户自己而言, udp的收发端口是不一样的.
    public void startupUDPServer() {
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(UDP_PORT);

            while (true) {
                byte []bytes = new byte[256];
                DatagramPacket request = new DatagramPacket(bytes, 0, 256);
                socket.receive(request);
                //阻塞UDP接受端
                //直到有好友发来一条消息
                String msg = new String(bytes, 0, bytes.length, "UTF-8");
                System.out.print("receive msg from[" + request.getSocketAddress() + "]: ");
                System.out.println(msg);
            }
        }catch (IOException e){
            System.out.println("IOException in startupUDPServer");
        }finally {
            if(socket != null){
                socket.close();
            }
        }
    }

    private void chatWithFriendService(String request){
        Map<String, Object> requestInfo = serviceUtil.parseChatMessage(request);
        if(requestInfo == null){
            System.out.println("unknown system error");
            return;
        }

        String exception = (String)requestInfo.get("exception");
        if(exception != null){
            System.out.println("request error: " + exception);
            return;
        }

        char mode = (Character) requestInfo.get("mode");
        switch (mode){
            case 'p':
                System.out.println("send message to friend");
                System.out.println(requestInfo.toString());
                break;
            case 'g':
                break;
            default:
                System.out.println("command error");
        }
    }


}
