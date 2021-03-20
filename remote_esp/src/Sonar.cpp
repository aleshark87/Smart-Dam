#include "Sonar.h"

Sonar::Sonar(int triggerPin, int echoPin){
    vs = 331.45 + 0.62*(20);
    this->triggerPin = triggerPin;
    this->echoPin = echoPin;
    pinMode(triggerPin, OUTPUT);
    pinMode(echoPin, INPUT);
}

void Sonar::readValue(){
    /* impulse send */
    digitalWrite(triggerPin,LOW);
    delayMicroseconds(5);
    digitalWrite(triggerPin,HIGH);
    delayMicroseconds(10);
    digitalWrite(triggerPin,LOW);
    
    /* impulse receive */
    float tUS = pulseIn(echoPin, HIGH);
    float t = tUS / 1000.0 / 1000.0 / 2;
    float d = t*vs;
    this->value = d;
}

float Sonar::getValue(){
    readValue();
    return this->value;
}