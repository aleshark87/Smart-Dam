#ifndef __TIMERLED__
#define __TIMERLED__

#include <async_fsm.h>
#include <TimerOne.h>
#include <Led.h>

#define LED_EVENT 6

class TimerLed : public EventSource {
public:
    void init();
    void checkAndGenerateEvent();
    void setBlinking(bool set);
    bool getBlinkState();
private:
    volatile bool hasToBlink;
    Led* led;
};

class LedEvent : public Event {
public:
    LedEvent() : Event(LED_EVENT, 0) {};
};

extern TimerLed timerLed;

#endif