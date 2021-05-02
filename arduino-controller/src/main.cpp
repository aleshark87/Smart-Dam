#include <MyAsyncFSM.h>
#include <MsgSerialService.h>
#include <MsgBtService.h>

#define LED_PIN 6
#define SERVO_PIN 5

AsyncFSM* myAsyncFSM;

void setup() {
  msgSerialService.init();
  msgBtService.init();
  Led* led = new Led(LED_PIN);
  ServoMotor* servo = new ServoMotor(SERVO_PIN);
  myAsyncFSM = new MyAsyncFSM(led, servo);
}

void loop() {
  myAsyncFSM->checkEvents();
  msgBtService.btSerialEvent();
}
