#include <ServoMotor.h>
//max opening is 180, lowest is 0


ServoMotor::ServoMotor(int pin){
    this->pin = pin;
    myservo.attach(pin);
}

void ServoMotor::setPosition(int damOpeningPerc){
    myservo.write(getOpeningValue(damOpeningPerc));
}

int ServoMotor::getOpeningValue(int damOpeningPerc){
    return map(damOpeningPerc, 0, 100, 80, 170);
}