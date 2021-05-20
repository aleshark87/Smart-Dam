#define S_NORMAL 0
#define S_PREALARM 1
#define S_ALARM 2
#define TRIGGERPIN 4
#define ECHOPIN 5
#define LEDPIN 2
#define DIST1 20
#define DIST2 12
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include <WiFi.h>
#include <Ticker.h>
#include <Sonar.h>
#include <Led.h>

/* wifi network name */
char* ssidName = "FASTWEB-B482E1";
/* WPA2 PSK password */
char* pwd = "***REMOVED***";
/* service IP address */ 
char* address = "http://192.168.1.151:8080";

bool connStatus = false;
bool prevConnStatus = false;
float distance;
volatile int state;
volatile bool determineState = false;
volatile bool msgAlarm = false;
volatile bool msgPreAlarm = false;
volatile bool msgNormal = false;

Sonar *sonar;
Led *led;
Ticker stateDetermination;
Ticker sendMsgAlarm;
Ticker sendMsgNotAlarm;
StaticJsonDocument<128> doc;
const float detPeriod = 0.5;//seconds
const float sendMsgAlarmPeriod = 1.0;
const float sendMsgNotAlarmPeriod = 2.0;

int sendTimes(String address){
  HTTPClient http;    
   http.begin(address + "/api/data");      
   http.addHeader("Content-Type", "application/json");
   String msg;
   doc["type"] = "times";
   doc["prealarm_time"] = sendMsgNotAlarmPeriod;
   doc["alarm_time"] = sendMsgAlarmPeriod;
   serializeJson(doc, msg);
   doc.clear();
   int retCode = http.POST(msg);   
   http.end();    
   return retCode;
}

void setConnStatus(bool set){
  if(set != connStatus){
    connStatus = set;
    if(connStatus){
      sendTimes(address);
    }
  }
}

int sendData(String address){  
   HTTPClient http;    
   http.begin(address + "/api/data");      
   http.addHeader("Content-Type", "application/json");
   String msg;
   doc["type"] = "data";
   doc["state"] = state;
   doc["distance"] = distance;
   serializeJson(doc, msg);
   doc.clear();
   Serial.println(msg);
   int retCode = http.POST(msg);   
   http.end();  
   if(retCode == -1){
     setConnStatus(false);
   }
   else{
     setConnStatus(true);
   }
   return retCode;
}

int sendState(String address){
   HTTPClient http;    
   http.begin(address + "/api/data");      
   http.addHeader("Content-Type", "application/json");
   String msg;
   doc["type"] = "state";
   doc["state"] = state;
   serializeJson(doc, msg);
   doc.clear();
   int retCode = http.POST(msg);   
   http.end();
   if(retCode == -1){
     setConnStatus(false);
   }
   else{
     setConnStatus(true);
   }
   return retCode;
}

void msgNotAlarmINT() {
  if(state == S_PREALARM){
    msgPreAlarm = true;
  } else {
    if(state == S_NORMAL){
      msgNormal = true;
    }
  }
}

void msgAlarmINT(){
  if(state == S_ALARM){
    msgAlarm = true;
  }
}

void stateINT(){
  determineState = true;
}

void sonarState(){
  distance = sonar->getValue();
  if(distance < DIST1 && distance > DIST2){
    state = S_PREALARM;
  }
  else{
    if(distance <= DIST2){
      state = S_ALARM;
    }
    else{
      state = S_NORMAL;
    }
  }
}

void setup() {
  Serial.begin(9600);                            
  WiFi.begin(ssidName, pwd);
  Serial.print("Connecting...");
  WiFi.status();
  while (WiFi.status() != WL_CONNECTED) {  
  } 
  Serial.println("Connected");
  int ret = sendTimes(address);
  if(ret != -1){
    connStatus = true;
  }
  else{
    connStatus = false;
  }
  sonar = new Sonar(TRIGGERPIN, ECHOPIN);
  led = new Led(LEDPIN); 
  distance = sonar->getValue();
  stateDetermination.attach(detPeriod, stateINT);
  sendMsgNotAlarm.attach(sendMsgNotAlarmPeriod, msgNotAlarmINT);
  sendMsgAlarm.attach(sendMsgAlarmPeriod, msgAlarmINT);
}
   
void loop() { 
 if (WiFi.status()== WL_CONNECTED){ 
  if(determineState){ 
    sonarState();
    determineState = false;
  }
	switch(state){
		case S_NORMAL:
    {
      led->off();
      if(msgNormal){
        sendState(address);
        msgNormal = false;
      }
			break;
    }
		case S_PREALARM:
    {
      led->pulse();
      if(msgPreAlarm){
        sendData(address);
        msgPreAlarm = false;
      }
			break;
    }
		case S_ALARM:
    {
      led->on();
      if(msgAlarm){
        sendData(address);
        msgAlarm = false;
      }
			break;
    }
	}
 } else { 
   Serial.println("Error in WiFi connection");   
 }
 
}