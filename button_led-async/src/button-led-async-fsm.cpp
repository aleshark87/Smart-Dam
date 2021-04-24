#include "async_fsm.h"
#include "Led.h"
#include <ServoMotor.h>
#include <MsgSerialService.h>

#define LED_PIN 13
#define SERVO_PIN 5

class MyAsyncFSM : public AsyncFSM {
  public:
    MyAsyncFSM(Led* led, ServoMotor* servo){
      this->led = led;
      this->servo = servo;
      led->switchOff();
      msgSerialService.registerObserver(this);
    }
  
    void handleEvent(Event* ev) {
      digitalWrite(13, true);
      delay(100);
      digitalWrite(13, false);
      servo->setPosition(ev->getMessage());
    }

  private:
    Led* led;
    ServoMotor* servo;
};

MyAsyncFSM* myAsyncFSM;

void setup() {
  msgSerialService.init();
  Led* led = new Led(LED_PIN);
  ServoMotor* servo = new ServoMotor(SERVO_PIN);
  myAsyncFSM = new MyAsyncFSM(led, servo);
}

void loop() {
  myAsyncFSM->checkEvents();
}
