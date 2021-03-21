#include <Led.h>

/* ONLY FOR ESP32 */
// setting PWM properties
const int freq = 5000;
const int ledChannel = 0;
const int resolution = 8;

Led::Led(const int pin){
    this->pin = pin;
    pinMode(pin, OUTPUT);
    /* ONLY FOR ESP32 */
    // configure LED PWM functionalitites
    ledcSetup(ledChannel, freq, resolution);
  
    // attach the channel to the GPIO to be controlled
    ledcAttachPin(pin, ledChannel);
    
}

void Led::on(){
    /* ESP8266 */
    //analogWrite(pin, HIGH);
    /* ESP32 */
    ledcWrite(ledChannel, 255);
}

void Led::off(){
    /* ESP8266 */
    //analogWrite(pin, LOW);
    /* ESP32 */
    ledcWrite(ledChannel, LOW);
}

void Led::pulse(){
  // increase the LED brightness
  for(int dutyCycle = 0; dutyCycle <= 255; dutyCycle+=20){   
    /* ESP32 */
    ledcWrite(ledChannel, dutyCycle);
    /* ESP8266 */
    //analogWrite(pin, dutyCycle);
    delay(10);
  }
  // decrease the LED brightness
  for(int dutyCycle = 255; dutyCycle >= 0; dutyCycle-=20){
    /* ESP32 */
    ledcWrite(ledChannel, dutyCycle);
    /* ESP8266 */
    //analogWrite(pin, dutyCycle);
    delay(10);
  }
}