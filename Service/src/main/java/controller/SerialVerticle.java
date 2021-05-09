package controller;

import com.google.gson.Gson;

public class SerialVerticle extends AbstractServiceVerticle implements MsgEventListener{

    private static final String serialPort = "/dev/ttyACM0";
    private static final int baudRate = 9600;
    private CommChannel serialCommChannel;
    private ManualModeListener manualListener;
    private Gson gson;
    
    private int testingCounter;
    
    public void registerListener(ManualModeListener listener) {
        this.manualListener = listener;
    }
    
    public SerialVerticle(MainController controller) {
        super(controller);
        gson = new Gson();
        testingCounter = 0;
    }
    @Override
    public void start() throws Exception {
        serialCommChannel = new SerialCommChannel(serialPort, baudRate);
        serialCommChannel.registerListener(this);
    }
    
    public void sendMsg(ArduinoPoint data) {
        String jsonString = gson.toJson(data);
        //System.out.println("Sending: " + jsonString);
        serialCommChannel.sendMsg(jsonString);
    }
    @Override
    public void msgArrived(String msg) {
        testingCounter++;
        System.out.println(msg + " " + testingCounter);
        if(msg.equals("MANUAL") || msg.equals("NOMANUAL") || msg.contains("open=")) {
            
            new Thread(new Runnable() {
                public void run()
                {
                    if(!msg.contains("open=")) {
                        boolean set = false;
                        if(msg.equals("MANUAL")) { set = true; }
                        // check if listener is registered.
                        if (manualListener != null) {
                            manualListener.manualMode(set);
                        }
                    }
                }
            }).start();
        }
        
    }
    

}
