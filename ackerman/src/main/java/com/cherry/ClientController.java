package com.cherry;

import com.service.CentralService;

public class ClientController {
    private static CentralService centralService = new CentralService();

    static class JaynaThread implements Runnable{
        public void run(){
            centralService.correspondWithJayna();
        }
    }

    static class JingGegeThread implements Runnable{
        public void run(){
            centralService.startupUDPServer();
        }
    }

    public void start(){
        try {

            Thread tcp = new Thread(new JaynaThread());
            Thread udp = new Thread(new JingGegeThread());
            tcp.start();
            udp.start();

            tcp.join();
            udp.join();

        }catch (InterruptedException e){
            System.out.println("Interruption exception");
        }
    }

    public static void main(String []args){
        ClientController clientController = new ClientController();
        clientController.start();
    }


}
