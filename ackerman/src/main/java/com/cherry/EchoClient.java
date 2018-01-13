package com.cherry;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class EchoClient {
    public static String msg = "che";

    public static byte []bytes = new byte[]{(byte) 1, (byte) 2, (byte)3};
    public static char []chars = new char[]{'1', '2', 'a'};
    public static void startClient(){
        Socket socket = null;
        try{
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
            SocketChannel socketChannel = SocketChannel.open(address);
//            socketChannel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.get(bytes);
            socketChannel.write(buffer);
            System.out.println(buffer.toString());

//
//            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
//            writer.write(msg);
            TimeUnit.SECONDS.sleep(2);

            System.out.println("after write");
            TimeUnit.SECONDS.sleep(3);

            InputStreamReader reader = new InputStreamReader(socket.getInputStream());
            int c;
            while((c = reader.read()) != -1){
                System.out.print((char)c);
            }

        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(socket != null){
                try {
                    socket.close();
                }catch (IOException ee){
                    ee.getStackTrace();
                }
            }
        }
    }

    public static void start2(){
        SocketChannel channel = null;
        try{
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
            channel = SocketChannel.open(address);

            Socket socket = channel.socket();

            Writer writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write("register -u 520520 -n cherry -p 666666");
            writer.flush();

            Reader reader = new InputStreamReader(socket.getInputStream());
            int c;
            while((c = reader.read()) != -1){
                System.out.print((char)c);
            }

        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if(channel != null){
                try {
                    channel.close();
                }catch (Exception ee){
                    ee.getStackTrace();
                }
            }

        }
    }

    public static void startTCPClient(){
        SocketChannel channel = null;
        try{
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
            channel = SocketChannel.open(address);
            Socket socket = channel.socket();
            Writer writer = new OutputStreamWriter(socket.getOutputStream());
//            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            Scanner scanner = new Scanner(System.in);
            String line;
            while(true){
                line = scanner.nextLine();
                if(line.equals("exit")){
                    break;
                }
                writer.write(line);
                writer.flush();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                channel.read(byteBuffer);
                char []myChars = getChars(byteBuffer.array());
                for(char c: myChars){
                    System.out.print(c);
                }
                byteBuffer.clear();
            }

        }catch (Exception e){
            e.getStackTrace();
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

    public static void startUDPClient(){
        try{
            DatagramSocket socket = new DatagramSocket(8080);
            socket.setSoTimeout(10000);
            InetAddress host = InetAddress.getByName("127.0.0.1");
            DatagramPacket request = new DatagramPacket(new byte[1], 1, host, 8080);
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
            socket.send(request);
            socket.receive(response);
            String result = new String(response.getData(), 0, response.getLength(), "UTF-8");
            System.out.println(result +", " );

        }catch (IOException e){
            System.out.print("shit");

            e.getStackTrace();
        }
    }

    public static void startUDPCServerWithChannel(){
        final int port = 12345;
        try{
            // 此udp服务器只用于接受udp消息, 使用与TCP一样的端口
            DatagramSocket socket = new DatagramSocket(port);

            while(true){
                //接受消息
                System.out.println("in the server loop");
                byte [] bytes = new byte[1024];
                DatagramPacket request = new DatagramPacket(bytes, 1024);
                socket.receive(request);
                System.out.println("already receive msg");

                String str = new String(bytes, 0, bytes.length);
                System.out.println("receive MSG from: " + request.getSocketAddress());
                System.out.println(str);

            }

        }catch (Exception e){
            System.out.println("emmm.....");
        }finally {
//            if(channel != null){
//                try{
//                    channel.close();
//                }catch (IOException e){
//                    System.out.println("channel close exception");
//                }
//            }
        }
    }

    static class TCPThread implements Runnable{
        public void run(){
            try{
                System.out.println("TCP Thread running");
                startTCPClient();
            }catch (Exception e){
                System.out.println("TCP Thread exception");
            }finally {
                System.out.println("TCP Thread canceled");
            }
        }
    }

    static class UDPThread implements Runnable{
        public void run(){
            try{
                System.out.println("UDP Thread running");
                startUDPCServerWithChannel();
            }catch (Exception e){
                System.out.println("UDP Thread exception");
            }finally {
                System.out.println("UDP Thread canceled");
            }
        }
    }


    public static void main(String []args) throws  InterruptedException{
//        startClient();
//        start2();
//        startTCPClient();
//        startUDPClient();

//        Thread tcp = new Thread(new TCPThread());
        Thread udp = new Thread(new UDPThread());
//        tcp.start();
        udp.start();

//        tcp.join();
        udp.join();
    }


    public static String parseByteBuffer(ByteBuffer buffer){
        try{
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer);

            return charBuffer.toString();
        }catch (Exception e){
            e.getStackTrace();
        }
        return "";
    }

    public static void parseString(String str, ByteBuffer buffer){
        try{
            ByteBuffer t = ByteBuffer.wrap(str.getBytes());
            buffer.put(t);
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public static void testBuffer(){
        ByteBuffer buffer = ByteBuffer.allocate(64);
        String str = "jayna";
        parseString(str, buffer);
        byte []bytes = buffer.array();
        for(byte b: bytes){
            if(b == 0){
                break;
            }
            System.out.print((char)b);
        }
    }

    private  static byte[] getBytes(char []chars){
        Charset charset = Charset.forName("UTF-8");
        CharBuffer charBuffer = CharBuffer.allocate(chars.length);
        charBuffer.put(chars);
        charBuffer.flip();
        ByteBuffer byteBuffer = charset.encode(charBuffer);
        return byteBuffer.array();
    }


    private static char[] getChars(byte []bytes){
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
}
