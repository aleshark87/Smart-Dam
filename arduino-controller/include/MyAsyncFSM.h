#ifndef __MYASYNCFSM__
#define __MYASYNCFSM__
#include "async_fsm.h"
#include "ServoMotor.h"
#include "Led.h"

class MyAsyncFSM : public AsyncFSM {
  public:
    MyAsyncFSM(Led* led, ServoMotor* servo);  
    void handleEvent(Event* ev);

  private:
    bool manualMode;
    int damOpening;
    Led* led;
    ServoMotor* servo;
    void sendBtUpdate(const int eventType, const int damOpening, const float distance);
};
    
#endif