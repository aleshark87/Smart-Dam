/* ESP32 libraries */
#include <HTTPClient.h>
#include <WiFi.h>
/* ESP8266 libraries */
//#include <ESP8266HTTPClient.h>
//#include <ESP8266WiFi.h>

/* wifi network name */
char* ssidName = "Your SSID";
/* WPA2 PSK password */
char* pwd = "YourPassword";
/* service IP address */ 
char* address = "http://164c5a96b01f.ngrok.io(ngrok link)";

const int trigPin = 4; 
const int echoPin = 5;
float duration,distance;

void setup() { 
  pinMode(trigPin, OUTPUT); 
  pinMode(echoPin, INPUT);  
  Serial.begin(9600);                            
  WiFi.begin(ssidName, pwd);
  Serial.print("Connecting...");
  WiFi.status();
  while (WiFi.status() != WL_CONNECTED) {  
    //delay(500);
    //Serial.print(".");
  } 
  Serial.println("Connected");
}

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
   
void loop() { 
 digitalWrite(trigPin, LOW); 
 delayMicroseconds(2); 
 digitalWrite(trigPin, HIGH); 
 delayMicroseconds(10); 
 digitalWrite(trigPin, LOW); 
 duration = pulseIn(echoPin, HIGH); 
 distance = (duration*.0343)/2; 
 if (WiFi.status()== WL_CONNECTED){   

   // read sensor 
   float value = 100;
   
   // send data 
   Serial.print("sending "+String(distance)+"...");    
   int code = sendData(address, value, "home");

   // log result 
   if (code == 200){
     Serial.println("ok");   
   } else {
     Serial.println("error");
   }
 } else { 
   Serial.println("Error in WiFi connection");   
 }
 
 delay(5);  
 
}
