package com.cherry.centralServerSystem;

import com.cherry.eventHandler.EventHandler;
import com.cherry.eventHandler.EventHandlerFactory;
import com.cherry.eventHandler.EventType;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

public class JaynaServer {
    private int port = 8080;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private SelectionKey serverKey;


    public JaynaServer(){
        try{
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverKey = serverSocketChannel.register(
                    selector, SelectionKey.OP_ACCEPT
            );
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public void start(){
        try {
            while (true) {
                int n = selector.select();
                if (n == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isAcceptable()){
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel client = channel.accept();
                        if(client == null){
                            continue;
                        }
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    }
                    if(key.isValid() && key.isReadable()){
                        readMessage(key);
                    }
                    if(key.isValid() && key.isWritable()){
                        writeMessage(key);
                    }
                }
            }
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    private void readMessage(SelectionKey key){
        try {
            SocketChannel client = (SocketChannel) key.channel();
            System.out.println();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int flag = client.read(buffer);
            char []chars = getChars(buffer.array());

            /*
            若客户端强制关闭，服务器会报“java.io.IOException: 远程主机强迫关闭了一个现有的连接。”，
            并且服务器会在报错后停止运行，错误的意思就是客户端关闭了，
            但是服务器还在从这个套接字通道读取数据，便抛出IOException，
            导致这种情况出现的原因就是:客户端异常关闭后，服务器的选择器会获取到与客户端套接字对应的套接字通道SelectionKey，
            并且这个key的兴趣是OP_READ，执行从这个通道读取数据时，客户端已套接字已关闭，
            所以会出现“java.io.IOException: 远程主机强迫关闭了一个现有的连接”的错误。
            解决这种问题也很简单，就是服务器在读取数据时，若发生异常，则取消当前key并关闭通道
             */

            if(flag == -1){
                System.out.println("close: " + client.socket());
                client.socket().close();    //客户端已经强制关闭, 服务端显示执行关闭
                //从数据库移除保存在alive_set中的user, 以及user:12345中的地址
                JaynaController.removeInfo(key);
                return;
            }

            try{
                char c = chars[0];
                EventHandler handler = null;
                JaynaController controller;
                switch (c){
                    case 'r':
                        handler = EventHandlerFactory.getEventHandler(EventType.REGISTER);
                        break;
                    case 'l':
                        handler = EventHandlerFactory.getEventHandler(EventType.LOGIN);
                        break;
                    case 'a':
                        handler = EventHandlerFactory.getEventHandler(EventType.ADD);
                        break;
                    case 'q':
                        handler = EventHandlerFactory.getEventHandler(EventType.QUERY);
                        break;
                    case 'c':
                        handler = EventHandlerFactory.getEventHandler(EventType.CHAT);
                        break;
                }
                controller = new JaynaController(handler, key);
                String response = controller.fireEvent(chars);

                client.register(selector, SelectionKey.OP_WRITE, response);
                key.attach(response);
            }catch (ArrayIndexOutOfBoundsException a){
                System.out.println("no message");
            }

        }catch (Exception e){
            e.getStackTrace();
        }
    }

    private void writeMessage(SelectionKey key){
        try{
            SocketChannel client = (SocketChannel) key.channel();
            String response = (String)key.attachment();
            client.write(ByteBuffer.wrap(response.getBytes()));
            response = "";
            client.register(selector, SelectionKey.OP_READ, response);
            key.attach(response);
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    private byte[] getBytes(char []chars){
        Charset charset = Charset.forName("UTF-8");
        CharBuffer charBuffer = CharBuffer.allocate(chars.length);
        charBuffer.put(chars);
        charBuffer.flip();
        ByteBuffer byteBuffer = charset.encode(charBuffer);
        return byteBuffer.array();
    }

    private char[] getChars(byte []bytes){
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        CharBuffer charBuffer = charset.decode(byteBuffer);
        ArrayList<Character> arrayList = new ArrayList<Character>();
        char [] chars = charBuffer.array();
        for(char c: chars){
            if(c == '\0'){
                break;
            }
            arrayList.add(c);
        }
        char []ret = new char[arrayList.size()];
        for(int i = 0; i < arrayList.size(); ++i){
            ret[i] = arrayList.get(i);
        }
        return ret;
    }


    public static void main(String []args){
        JaynaServer jaynaServer = new JaynaServer();
        jaynaServer.start();

//        SingleChatService.startUDPClient();
    }
}
