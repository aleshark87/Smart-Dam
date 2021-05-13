package controller;

import com.google.gson.Gson;

import io.vertx.core.Vertx;

public class SerialVerticle extends AbstractServiceVerticle implements MsgEventListener{

    private static final String serialPort = "/dev/ttyACM0";
    private static final int baudRate = 9600;
    private CommChannel serialCommChannel;
    private ManualModeListener manualListener;
    private Gson gson;
    
    public void registerListener(ManualModeListener listener) {
        this.manualListener = listener;
    }
    
    public SerialVerticle(MainController controller) {
        super(controller);
        gson = new Gson();
    }
    @Override
    public void start() throws Exception {
        serialCommChannel = new SerialCommChannel(serialPort, baudRate);
        serialCommChannel.registerListener(this);
    }
    
    public void sendMsg(ArduinoPoint data) {
        String jsonString = gson.toJson(data);
        serialCommChannel.sendMsg(jsonString);
    }
    @Override
    public void msgArrived(String msg) {
        
        if(msg.equals("MANUAL") || msg.equals("NOMANUAL") || msg.contains("=")) {
            new Thread(new Runnable() {
                public void run()
                {
                    if(!msg.contains("=")) {
                        boolean set = false;
                        if(msg.equals("MANUAL")) { set = true; }
                        // check if listener is registered.
                        if (manualListener != null) {
                            manualListener.manualMode(set);
                        }
                    }
                    else {
                        if(manualListener != null) {
                            manualListener.manualDamOpening(Integer.parseInt(msg.split("=")[1]));
                        }
                    }
                }
            }).start();
        }
        
    }
    
    

}
