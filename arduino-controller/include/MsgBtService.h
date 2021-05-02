#ifndef __MSGBTSERVICE__
#define __MSGBTSERVICE__

#include <SoftwareSerial.h>
#include <MsgService.h>

class MsgBtService: public MsgService {
public:
    void init();
    void sendMsg(const String msg);
    void sendUpdate(const int state, const int damOpening, const float distance);
    void btSerialEvent();
    Event* eventGenerator(const String msg);
};

class BtMsgEvent : public Event {
public:
    BtMsgEvent(int message) : Event(MANUAL, message){}
};

class DamOpenMsgEvent : public Event {
public:
    DamOpenMsgEvent(int message) : Event(DAM_OPEN, message){
    }
};

extern MsgBtService msgBtService;

#endif