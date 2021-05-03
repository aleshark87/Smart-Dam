#include <TimerLed.h>

TimerLed timerLed;

void ledISR(void){
    timerLed.checkAndGenerateEvent();
}

void TimerLed::init(){
    hasToBlink = false;
    Timer1.initialize(300000);
    Timer1.attachInterrupt(ledISR);
}

void TimerLed::setBlinking(bool set){
    if(set){ 
        hasToBlink = true;
    }
    else{
        hasToBlink = false;
    }
}

void TimerLed::checkAndGenerateEvent(){
    if(hasToBlink){
        this->generateEvent(new LedEvent());
    }
}

bool TimerLed::getBlinkState(){
    return hasToBlink;
}

