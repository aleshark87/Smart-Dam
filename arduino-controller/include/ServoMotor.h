#ifndef __SERVOMOTOR__
#define __SERVOMOTOR__
#define STARTPULSE 1350
#define ENDPULSE 2250

#include <ServoTimer2.h>

/*
    Primitive for Servo
*/

class ServoMotor {
public:
    ServoMotor(int pin);
    void setPosition(int damOpeningPerc);
    void setStartValue();
private:
    int pin;
    ServoTimer2 myservo;
    int getOpeningValue(int damOpeningPerc);
};

#endif
