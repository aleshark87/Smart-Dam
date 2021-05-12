#ifndef __MSGSERVICE__
#define __MSGSERVICE__

#include "Arduino.h"
#include <ArduinoJson.h>
#include <async_fsm.h>
#include <string.h>

#define S_NORMAL 0
#define S_PREALARM 1
#define S_ALARM 2
#define ALARM_MSG_EVENT 3
#define MANUAL 4
#define NOMANUAL 5
#define DAM_OPEN 6
#define NODAMUPDATE -1

class MsgService : public EventSource{
    
public: 
  virtual void init() = 0;  
  
  virtual void sendMsg(const String msg) = 0;

  virtual Event* eventGenerator(const String msg) = 0;
};



#endif