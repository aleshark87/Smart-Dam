#include <MyAsyncFSM.h>
#include <LedAsyncFSM.h>
#include <MsgSerialService.h>
#include <MsgBtService.h>
#include <TimerLed.h>

#define LED_PIN 6
#define SERVO_PIN 5

AsyncFSM* myAsyncFSM;
AsyncFSM* ledAsyncFSM;

void setup() {
  pinMode(12, OUTPUT);
  msgSerialService.init();
  timerLed.init();
  msgBtService.init();
  Led* led = new Led(LED_PIN);
  ServoMotor* servo = new ServoMotor(SERVO_PIN);
  myAsyncFSM = new MyAsyncFSM(led, servo);
  ledAsyncFSM = new LedAsyncFSM(led);
}

void loop() {
  myAsyncFSM->checkEvents();
  ledAsyncFSM->checkEvents();
  msgBtService.btSerialEvent();
}
