#ifndef __SERVOMOTOR__
#define __SERVOMOTOR__

#include <Servo.h>
#include <Arduino.h>

class ServoMotor {
public:
    ServoMotor(int pin);
    void setPosition(int damOpeningPerc);
private:
    int pin;
    Servo myservo;
    int getOpeningValue(int damOpeningPerc);
};

#endif
