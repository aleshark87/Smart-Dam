#ifndef __SERVOMOTOR__
#define __SERVOMOTOR__

#include <ServoTimer2.h>

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
