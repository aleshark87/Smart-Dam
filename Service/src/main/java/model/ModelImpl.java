package model;

import controller.ArduinoPoint;
import controller.DataPoint;
import controller.MainController;

public class ModelImpl implements Model{

    private final MainController mainController;
    private STATE state;
    private float distance;
    private int damOpening;
    private final float deltaD = 2.0f;
    private final float alarmDistance = 12.0f;
    
    public ModelImpl(MainController controller) {
        mainController = controller;
        damOpening = 0;
    }
    
    private void computeOpening() {
        /* Calcola l'apertura della diga quando siamo in ALLARME */
        float tmp1Distance = alarmDistance;
        float tmp2Distance = alarmDistance - deltaD;
        //12,10
        if((distance <= tmp1Distance) && (distance > tmp2Distance)) {
            damOpening = 20;
        }
        tmp1Distance = tmp2Distance;
        tmp2Distance = tmp2Distance - deltaD;
        //10,8
        if((distance <= tmp1Distance) && (distance > tmp2Distance)) {
            damOpening = 40;
        }
        tmp1Distance = tmp2Distance;
        tmp2Distance = tmp2Distance - deltaD;
        //8,6
        if((distance <= tmp1Distance) && (distance > tmp2Distance)) {
            damOpening = 60;
        }
        tmp1Distance = tmp2Distance;
        tmp2Distance = tmp2Distance - deltaD;
        //6,4
        if((distance <= tmp1Distance) && (distance > tmp2Distance)) {
            damOpening = 80;
        }
        tmp1Distance = tmp2Distance;
        //4
        if(distance <= tmp1Distance) {
            damOpening = 100;
        }
    }
    
    public void handleNewData(final DataPoint data) {
        /* Qua arriveranno i dati */
        state = data.getState();
        distance = data.getDistance();
        if(state == STATE.ALARM) {
            computeOpening();
            notifySerial();
        }
        if(state != STATE.NORMAL) {
            
        }
    }
    
    private void notifySerial() {
        this.mainController.getSerialVerticle().sendMsg(new ArduinoPoint(stateToIntState(), damOpening));
    }
    
    private int stateToIntState() {
        if(state == STATE.PRE_ALARM) {
            return 1;
        }
        else {
            return 2;
        }
    }
    
    @Override
    public STATE getState() {
        return state;
    }

    @Override
    public float getDistance() {
        return distance;
    }

    @Override
    public int getDamOpening() {
        return damOpening;
    }

}
