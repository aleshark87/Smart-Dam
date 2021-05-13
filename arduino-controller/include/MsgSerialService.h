#ifndef __MSGSERVICESE__
#define __MSGSERVICESE__

#include <MsgService.h>

/*
    Service for SerialCommunication
*/

class MsgSerialService : public MsgService {
public:
    float getDistance();
    void init();
    void sendMsg(const String msg);
    Event* eventGenerator(const String msg);
};

class AlarmEvent: public Event {
public:
    AlarmEvent(int message) : Event(S_ALARM, message){}
};

class NormalEvent: public Event {
public:
    NormalEvent() : Event(S_NORMAL, 0){}
};

class PreAlarmEvent: public Event {
public:
    PreAlarmEvent(int message, float distance) : Event(S_PREALARM, message){
        this->distance = distance;
    }
    float getDistance() {
        return this->distance;
    }
private:
    float distance;
};

extern MsgSerialService msgSerialService;


#endif