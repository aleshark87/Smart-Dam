#include <ServoMotor.h>


ServoMotor::ServoMotor(int pin){
    this->pin = pin;
    myservo.attach(pin);
}

void ServoMotor::setPosition(int damOpeningPerc){
    myservo.write(getOpeningValue(damOpeningPerc));
}

int ServoMotor::getOpeningValue(int damOpeningPerc){
    return map(damOpeningPerc, 0, 100, STARTPULSE, ENDPULSE);
}

void ServoMotor::setStartValue(){
    myservo.write(1350);
}