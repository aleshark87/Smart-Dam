#define S_NORMAL 0
#define S_PREALARM 1
#define S_ALARM 2
#define TRIGGERPIN 4
#define ECHOPIN 5
#define LEDPIN 2
#define DIST1 20
#define DIST2 5
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
char* address = "http://c3fd680ac54c.ngrok.io";

volatile float distance;
volatile int state;

Sonar *sonar;
Led *led;
Ticker stateDetermination;
Ticker sendMsgAlarm;
Ticker sendMsgPreAlarm;
StaticJsonDocument<48> doc;
const float detPeriod = 0.5;//seconds
const float sendMsgAlarmPeriod = 1.0;
const float sendMsgPreAlarmPeriod = 3.0;

int sendData(String address, float value, String place){  
   HTTPClient http;    
   http.begin(address + "/api/data");      
   http.addHeader("Content-Type", "application/json");
   String msg;
   doc["state"] = state;
   doc["distance"] = distance;
   serializeJson(doc, msg);
   int retCode = http.POST(msg);   
   http.end();  
      
    //String payload = http.getString();  
    //Serial.println(payload);      
   return retCode;
}

void msgPreAlarmINT() {
  if(state == S_PREALARM){
    sendData(address, distance, "home");
  }
}

void msgAlarmINT(){
  if(state == S_ALARM){
    sendData(address, distance, "home");
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
  sendMsgPreAlarm.attach(sendMsgPreAlarmPeriod, msgPreAlarmINT);
  sendMsgAlarm.attach(sendMsgAlarmPeriod, msgAlarmINT);
}
   
void loop() { 
 if (WiFi.status()== WL_CONNECTED){   
	switch(state){
		case S_NORMAL:
    {
      led->off();
			Serial.println("Stato normale");
			break;
    }
		case S_PREALARM:
    {
      led->pulse();
			Serial.println("Stato preallarme");

			break;
    }
		case S_ALARM:
    {
      led->on();
			Serial.println("Stato allarme");
      
			break;
    }
	}
 } else { 
   Serial.println("Error in WiFi connection");   
 }
 
}
