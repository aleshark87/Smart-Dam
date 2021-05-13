#ifndef __SONAR__
#define __SONAR__
#include <Arduino.h>

class Sonar {
public:
    Sonar(int triggerPin, int echoPin);
    float getValue();
private:
    float vs;
    void readValue();
    int triggerPin;
    int echoPin;
    float value;
};

#endif