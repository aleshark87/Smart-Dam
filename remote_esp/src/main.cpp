/* ESP32 libraries */
#include <HTTPClient.h>
#include <WiFi.h>
/* ESP8266 libraries */
//#include <ESP8266HTTPClient.h>
//#include <ESP8266WiFi.h>
#include <Sonar.h>
#define NORMAL 0
#define PREALARM 1
#define ALARM 2
#define TRIGGER_PIN 4
#define ECHO_PIN 5

/* wifi network name */
char* ssidName = "FASTWEB-B482E1";
/* WPA2 PSK password */
char* pwd = "***REMOVED***";
/* service IP address */ 
char* address = "http://164c5a96b01f.ngrok.io(ngrok link)";

volatile int state;
Sonar *sonar;


int sendData(String address, float value, String place){  
   HTTPClient http;    
   http.begin(address + "/api/data");      
   http.addHeader("Content-Type", "application/json");     
   String msg = 
    String("{ \"value\": ") + String(value) + 
    ", \"place\": \"" + place +"\" }";
   int retCode = http.POST(msg);   
   http.end();  
      
    //String payload = http.getString();  
    //Serial.println(payload);      
   return retCode;
}

void setup() { 
  pinMode(TRIGGER_PIN, OUTPUT); 
  pinMode(ECHO_PIN, INPUT);  
  Serial.begin(9600);                            
  WiFi.begin(ssidName, pwd);
  Serial.print("Connecting...");
  WiFi.status();
  while (WiFi.status() != WL_CONNECTED) {  
  } 
  Serial.println("Connected");
  //fa fatta prima lettura del dato del sonar per inizializzare state
  state = NORMAL;
  sonar = new Sonar(TRIGGER_PIN, ECHO_PIN);
}
   
void loop() { 
 if (WiFi.status()== WL_CONNECTED){   
	switch(state){
		case NORMAL:
			Serial.println("Stato normale");
			delay(500);
			state = PREALARM;
			break;
		case PREALARM:
			Serial.println("Stato preallarme");
			Serial.println(sonar->getValue());
			delay(1000);
			state = ALARM;
			break;
		case ALARM:
			Serial.println("Stato allarme");
			delay(2000);
			state = NORMAL;
			break;
	}
   /*Serial.print("sending "+String(distance)+"...");    
   int code = sendData(address, value, "home");*/

   // result check 
   /*if (code == 200){
     Serial.println("ok");   
   } else {
     Serial.println("error");
   }*/
 } else { 
   Serial.println("Error in WiFi connection");   
 }
 
}
