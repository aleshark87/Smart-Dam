package comm;

import java.util.Date;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Testing simple message passing.
 * 
 * To be used with an Arduino connected via Serial Port
 * running modulo-lab-2.2/pingpong.ino
 * 
 * @author aricci
 *
 */
public class MsgReceive {

    public static void main(String[] args) throws Exception {
        CommChannel channel = new SerialCommChannel("/dev/ttyACM0",9600);  
        
        /* attesa necessaria per fare in modo che Arduino completi il reboot */
        System.out.println("Waiting Arduino for rebooting...");     
        Thread.sleep(4000);
        System.out.println("Ready.");       

        
        while (true){
            JsonArray json = new JsonArray();
            JsonObject data = new JsonObject();
            data.put("blasfemy", "porcodio");
            data.put("time", new Date().getTime());
            json.add(data);
            System.out.println("Sending: porco");
            //channel.sendMsg(json.encode());
            channel.sendMsg("porco");
            String msg = channel.receiveMsg();
            System.out.println("Received: "+msg);       
            Thread.sleep(3000);
        }
    }

}