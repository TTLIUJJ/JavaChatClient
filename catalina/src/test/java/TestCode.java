import com.cherry.centralServerSystem.ServerService;
import com.cherry.eventHandler.EventHandler;
import com.cherry.eventHandler.EventHandlerFactory;
import com.cherry.eventHandler.EventType;
import com.cherry.eventHandler.RegisterHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TestCode {
    private static RegisterHandler handler = new RegisterHandler();
    private static ServerService serverService = new ServerService();

    public static void testFactory(){
        EventHandler eventHandler = EventHandlerFactory.getEventHandler(EventType.REGISTER);
        if(eventHandler == null){
            System.out.println("shit");
        }
        else{
//            System.out.println(eventHandler.say());
        }
    }

    public static void testRegisterHandler(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("qq", "123456");
        map.put("nickname", "ackerman");
        map.put("password", "shit");
        RegisterHandler handler = new RegisterHandler();
        handler.processEvent(map);
    }

    public static void testExtractMessage(){
        char []chars = "fireEvent -u 123456 -n jayna -p 5588006".toCharArray();
        Map<String, String> map = new HashMap<String, String>();
        serverService.extractMessage(chars, map);
        for(Map.Entry<String, String> entry: map.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println(serverService.getCommand(map));
    }

    public static void testScanner(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            String line = scanner.nextLine();
            if(line.equals("exit")){
                break;
            }
            System.out.println(">>> "+line);
        }
    }

    public static void main(String []args){
//        testFactory();
//        testRegisterHandler();
//        testExtractMessage();
        testScanner();
    }
}
