#include <MyAsyncFSM.h>
#include <MsgSerialService.h>
#include <MsgBtService.h>

MyAsyncFSM::MyAsyncFSM(Led* led, ServoMotor* servo){
      this->led = led;
      this->servo = servo;
      led->switchOff();
      manualMode = false;
      damOpening = 0;
      this->servo->setStartValue();
      msgSerialService.registerObserver(this);
      msgBtService.registerObserver(this);
}
  
void MyAsyncFSM::handleEvent(Event* ev) {
    int eventType = ev->getType();
    if(eventType == MANUAL && manualMode == false){
        led->switchOn();
        manualMode = true;
    }
    else {
        if(eventType == MANUAL && manualMode == true){
            led->switchOff();
            manualMode = false;
            servo->setPosition(damOpening);
        }
    }
    
    if(manualMode == false){
        damOpening = ev->getMessage();
        servo->setPosition(damOpening);
        sendBtUpdate(eventType, damOpening, msgSerialService.getDistance());
    }
    else{
        if(eventType != MANUAL){
            sendBtUpdate(eventType, -1, msgSerialService.getDistance());
        }
        if(eventType == DAM_OPEN){
            servo->setPosition(ev->getMessage());
        }
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
