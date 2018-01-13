package com.cherry.centralServerSystem;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class SingleChatService {


    public static void startUDPClient(){
        try{
            final String host = "101.132.181.76";
            final int port = 12345;
            Scanner scanner = new Scanner(System.in);
            DatagramSocket socket = new DatagramSocket(12345);
            while(true){
                System.out.println("in the loop");
                String msg = scanner.nextLine();

                socket.setSoTimeout(10000);
                InetAddress address = InetAddress.getByName(host);
                byte []bytes = msg.getBytes();
                DatagramPacket request = new DatagramPacket(
                        bytes, bytes.length, address, port
                );
                socket.send(request);
            }

        }catch (Exception e){
            System.out.println("shit in UDP client");
        }
    }
}
