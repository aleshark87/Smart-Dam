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

volatile float distance;
volatile int state;

Sonar *sonar;
Led *led;
Ticker stateDetermination;
Ticker sendMsgAlarm;
Ticker sendMsgNotAlarm;
StaticJsonDocument<128> doc;
const float detPeriod = 0.5;//seconds
const float sendMsgAlarmPeriod = 1.5;
const float sendMsgNotAlarmPeriod = 3.0;

int sendData(String address){  
   HTTPClient http;    
   http.begin(address + "/api/data");      
   http.addHeader("Content-Type", "application/json");
   String msg;
   doc["type"] = "data";
   doc["state"] = state;
   doc["distance"] = distance;
   serializeJson(doc, msg);
   int retCode = http.POST(msg);   
   http.end();  
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
   int retCode = http.POST(msg);   
   http.end();       
   return retCode;
}

void msgNotAlarmINT() {
  if(state == S_PREALARM){
    sendData(address);
  } else {
    if(state == S_NORMAL){
      sendState(address);
    }
  }
}

void msgAlarmINT(){
  if(state == S_ALARM){
    sendData(address);
  }
}

void stateINT(){
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
  
  sonar = new Sonar(TRIGGERPIN, ECHOPIN);
  led = new Led(LEDPIN); 
  distance = sonar->getValue();
  stateDetermination.attach(detPeriod, stateINT);
  sendMsgNotAlarm.attach(sendMsgNotAlarmPeriod, msgNotAlarmINT);
  sendMsgAlarm.attach(sendMsgAlarmPeriod, msgAlarmINT);
}
   
void loop() { 
 if (WiFi.status()== WL_CONNECTED){   
	switch(state){
		case S_NORMAL:
    {
      led->off();
			//Serial.println("Stato normale");
			break;
    }
		case S_PREALARM:
    {
      led->pulse();
			//Serial.println("Stato preallarme");

			break;
    }
		case S_ALARM:
    {
      led->on();
			//Serial.println("Stato allarme");
      
			break;
    }
	}
 } else { 
   Serial.println("Error in WiFi connection");   
 }
 
}
