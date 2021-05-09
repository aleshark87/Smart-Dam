#include <MsgBtService.h>

String contentBt;
MsgBtService msgBtService;
SoftwareSerial serialChannel(2,3);
StaticJsonDocument<128> doc1;

void MsgBtService::init(){
    serialChannel.begin(9600);
    contentBt.reserve(256);
    contentBt = "";
}

void MsgBtService::sendMsg(const String msg){
    serialChannel.println(msg);
}

void MsgBtService::sendUpdate(const int state, const int damOpening, const float distance){
   String msg;
   doc1["state"] = state;
   doc1["damOpening"] = damOpening;
   doc1["distance"] = distance;
   serializeJson(doc1, msg);
   doc1.clear();
   msgBtService.sendMsg(msg);
}

void MsgBtService::btSerialEvent(){
    while (serialChannel.available()) {
        noInterrupts();
        char ch = (char) serialChannel.read(); 
        interrupts();
        if(ch == '\n'){
            msgBtService.generateEvent(this->eventGenerator(contentBt));
            contentBt = "";
        } 
        else {
            contentBt += ch;
        }
    }
}

Event* MsgBtService::eventGenerator(const String msg){
    
    if(msg.compareTo("MANUAL") == 0){
        return new BtMsgEvent(MANUAL);
    }
    else{
        return new DamOpenMsgEvent(msg.toInt());
    }
}