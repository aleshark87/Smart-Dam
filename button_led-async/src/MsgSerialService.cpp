#include "Arduino.h"
#include <MsgSerialService.h>

String content;
StaticJsonDocument <128>doc;
MsgSerialService msgSerialService;

void MsgSerialService::init(){
  Serial.begin(9600);
  content.reserve(256);
  content = "";
}

void MsgSerialService::sendMsg(const String msg){
  Serial.println(msg);  
}

Event* MsgSerialService::eventGenerator(const String msg){
  int size = msg.length();
  char msgChar[size + 1];
  strcpy(msgChar, msg.c_str());
  // Deserialize the JSON document
  DeserializationError error = deserializeJson(doc, msgChar);
  if (error) { Serial.println(error.f_str()); }

  const int state = doc["state"];
  const int damOpening = doc["damOpening"];
  switch(state){
    case S_ALARM:
      Event *ev = new AlarmEvent(damOpening);
      return ev;
    break;
  }
  return NULL;
}

void serialEvent() {
  /* reading the content */
  while (Serial.available()) {
    noInterrupts();
    char ch = (char) Serial.read();
    interrupts();
    if (ch == '\n'){
      msgSerialService.generateEvent(msgSerialService.eventGenerator(content));
      content = "";
    } else {
      content += ch;      
    }
  }
}
