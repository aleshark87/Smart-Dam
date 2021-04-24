#ifndef __MSGSERVICESE__
#define __MSGSERVICESE__

#include <MsgService.h>

class MsgSerialService : public MsgService {
public:
    void init();
    void sendMsg(const String msg);
    Event* eventGenerator(const String msg);
};

class AlarmEvent: public Event {
public:
    AlarmEvent(int message) : Event(S_ALARM, message){}
};

extern MsgSerialService msgSerialService;


#endif