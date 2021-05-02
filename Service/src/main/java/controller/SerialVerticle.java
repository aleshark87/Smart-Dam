package controller;

import java.util.Date;

import com.google.gson.Gson;

public class SerialVerticle extends AbstractServiceVerticle{

    private static final String serialPort = "/dev/ttyACM0";
    private static final int baudRate = 9600;
    private CommChannel serialCommChannel;
    private Gson gson;
    
    public SerialVerticle(MainController controller) {
        super(controller);
        gson = new Gson();
    }
    @Override
    public void start() throws Exception {
        serialCommChannel = new SerialCommChannel(serialPort, baudRate);
        
    }
    
    public void sendMsg(ArduinoPoint data) {
        String jsonString = gson.toJson(data);
        System.out.println("Sending: " + jsonString);
        serialCommChannel.sendMsg(jsonString);
    }

}
