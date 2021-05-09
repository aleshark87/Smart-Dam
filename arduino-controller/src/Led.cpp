#include "Led.h"
#include "Arduino.h"

Led::Led(int pin){
  this->pin = pin;
  state = false;
  pinMode(pin,OUTPUT);
}

void Led::switchOn(){
  state = true;
  digitalWrite(pin,HIGH);
}

void Led::switchOff(){
  state = false;
  digitalWrite(pin,LOW);
};

bool Led::getState(){
  return state;
}
