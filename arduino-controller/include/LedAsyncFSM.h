#ifndef __LEDASYNCFSM__
#define __LEDASYNCFSM__

#include <async_fsm.h>
#include <Led.h>
#include <TimerLed.h>

class LedAsyncFSM : public AsyncFSM {
public:
    LedAsyncFSM(Led* led);
    void handleEvent(Event* ev);
private:
    Led* led;
};

#endif