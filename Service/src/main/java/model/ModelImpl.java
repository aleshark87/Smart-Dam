package model;

import java.util.LinkedList;
import java.util.List;

import controller.ArduinoPoint;
import controller.DataPoint;
import controller.MainController;
import controller.ManualModeListener;

public class ModelImpl implements Model, ManualModeListener{

    private double alarmPeriodUpdate = 0.0;
    private double notAlarmPeriodUpdate = 0.0;
    private final MainController mainController;
    private STATE state;
    private boolean manualMode;
    private float distance;
    private int damOpening;
    private final float deltaD = 2.0f;
    private final float alarmDistance = 12.0f;
    
    public ModelImpl(MainController controller) {
        mainController = controller;
        damOpening = 0;
        manualMode = false;
        this.mainController.getSerialVerticle().registerListener(this);
    }
    
    private void computeOpening() {
        /* Calcola apertura diga  */
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
        if(state == STATE.ALARM || state == STATE.PRE_ALARM) {
            distance = data.getDistance();
            if(!manualMode) {
                computeOpening();
                
                //this.mainController.getConnection().insertData(data);
            }
            else {
                
            }
            notifySerialAlarm();
          //this.mainController.getConnection().insertData(data);
        }
        else {
            notifySerialNormal();
        }
    }
    
    private void notifySerialAlarm() {
        if(state == STATE.ALARM) {
            this.mainController.getSerialVerticle().sendMsg(new ArduinoPoint(stateToIntState(), damOpening, distance));
        }
        else {
            this.mainController.getSerialVerticle().sendMsg(new ArduinoPoint(stateToIntState(), 0, distance));
        }
    }
    
    private void notifySerialNormal() {
        this.mainController.getSerialVerticle().sendMsg(new ArduinoPoint(stateToIntState(), 0, 0.0f));
    }
    
    private int stateToIntState() {
        if(state == STATE.NORMAL) {
            return 0;
        }
        else {
            if(state == STATE.PRE_ALARM) {
                return 1;
            }
            else {
                return 2;
            }
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

    @Override
    public void manualMode(boolean set) {
        //System.out.println();
        this.manualMode = set;
    }

    @Override
    public boolean getManualMode() {
        
        return manualMode;
    }

    @Override
    public void setUpdateTimesInternet(double alarmTime, double notAlarmTime) {
        this.alarmPeriodUpdate = alarmTime;
        this.notAlarmPeriodUpdate = notAlarmTime;
        //System.out.println(alarmPeriodUpdate);
    }
    
    private boolean areUpdateTimesSetted() {
        return !(alarmPeriodUpdate == 0.0 && notAlarmPeriodUpdate == 0.0);
    }

    @Override
    public List<Double> getTimes() {
        if(areUpdateTimesSetted()) {
            List<Double> timesList = new LinkedList<Double>();
            timesList.add(alarmPeriodUpdate); timesList.add(notAlarmPeriodUpdate);
            return timesList;
        }
        else {
            return null;
        }
    }

    @Override
    public void manualDamOpening(int damOpening) {
        this.damOpening = damOpening;
    }
}
