#include <MyAsyncFSM.h>
#include <MsgSerialService.h>
#include <MsgBtService.h>
#include <TimerLed.h>

MyAsyncFSM::MyAsyncFSM(Led* led, ServoMotor* servo){
      this->led = led;
      this->servo = servo;
      led->switchOff();
      manualMode = false;
      prevDamOpening = 0;
      switchingManualMode = false;
      damOpening = 0;
      this->servo->setStartValue();
      msgSerialService.registerObserver(this);
      msgBtService.registerObserver(this);
}
  
void MyAsyncFSM::handleEvent(Event* ev) {
    if(ev->getType() == MANUAL && manualMode == false){
        msgSerialService.sendMsg("MANUAL");
        manualMode = true;
    }
    else {
        if(ev->getType() == NOMANUAL && manualMode == true){
            msgSerialService.sendMsg("NOMANUAL");
            switchingManualMode = true;
            manualMode = false;
        }
    }
    sendMsgMoveDamComputeLed(ev);
    if(switchingManualMode) { switchingManualMode = false; }

}

void MyAsyncFSM::sendMsgMoveDamComputeLed(Event *ev){
    int eventType = ev->getType();
    computeStateSetLed(eventType);
    if(manualMode == false && switchingManualMode == false){
        damOpening = ev->getMessage();
        servo->setPosition(damOpening);
        sendBtUpdate(eventType, damOpening, msgSerialService.getDistance());
    }
    else{
        if(eventType != MANUAL && eventType != NOMANUAL){
            sendBtUpdate(eventType, -1, msgSerialService.getDistance());
        }
        if(eventType == DAM_OPEN){
            int number = ev->getMessage();
            String open(ev->getMessage());
            msgSerialService.sendMsg("open=" + open);
            servo->setPosition(number);
        }
    }
}

void MyAsyncFSM::computeStateSetLed(int eventType){
    if(eventType == S_NORMAL || eventType == S_PREALARM){
        if(led->getState()){
            led->switchOff();
        }
        if(timerLed.getBlinkState()){
            timerLed.setBlinking(false);
        }
    }
    if(eventType == S_ALARM){
        if(!timerLed.getBlinkState() && manualMode == false){
            timerLed.setBlinking(true);
        }
    }
    if(eventType == MANUAL && manualMode == true){
        led->switchOn();
        timerLed.setBlinking(false);
    }
    if(eventType == NOMANUAL && manualMode == false){
        led->switchOff();
    }
}

void MyAsyncFSM::sendBtUpdate(const int eventType, const int damOpening, const float distance) {
switch(eventType) {
case S_NORMAL:
    if(damOpening != NODAMUPDATE){
    msgBtService.sendUpdate(S_NORMAL, 0, 0.0);
    }
    else{
    msgBtService.sendUpdate(S_NORMAL, NODAMUPDATE, 0.0);
    }
    break;
case S_PREALARM:
    if(damOpening != NODAMUPDATE){
    msgBtService.sendUpdate(S_PREALARM, 0, distance);
    }
    else{
    msgBtService.sendUpdate(S_PREALARM, NODAMUPDATE, distance);
    }
    break;
case S_ALARM:
    if(damOpening != NODAMUPDATE){
    msgBtService.sendUpdate(S_ALARM, damOpening, distance);
    }
    else{
    msgBtService.sendUpdate(S_ALARM, NODAMUPDATE, distance);
    }
    break;
}
}
