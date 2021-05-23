#ifndef __MYASYNCFSM__
#define __MYASYNCFSM__
#include "async_fsm.h"
#include "ServoMotor.h"
#include "Led.h"

/*
  AsyncFSM for all except for led blinking
*/

class MyAsyncFSM : public AsyncFSM {
  public:
    MyAsyncFSM(Led* led, ServoMotor* servo);  
    void handleEvent(Event* ev);
  private:
    bool switchingManualMode;
    bool manualMode;
    int prevDamOpening;
    int damOpening;
    Led* led;
    ServoMotor* servo;
    void sendBtUpdate(const int eventType, const int damOpening, const float distance);
    void computeStateSetLed(int eventType);
    void sendMsgMoveDamComputeLed(Event* ev);
};
    
#endif