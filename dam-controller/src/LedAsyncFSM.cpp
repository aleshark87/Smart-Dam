#include <LedAsyncFSM.h>

LedAsyncFSM::LedAsyncFSM(Led* led){
    this->led = led;
    timerLed.registerObserver(this);
}

void LedAsyncFSM::handleEvent(Event* ev){
    if(led->getState()){
        led->switchOff();
    }
    else{
        led->switchOn();
    }
}


