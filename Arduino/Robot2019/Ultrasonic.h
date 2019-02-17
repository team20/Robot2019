#ifndef Ultrasonic_h
#define Ultrasonic_h

#include "Arduino.h"

class Ultrasonic {
  public:
    static void initialize();
    static void updateDistance();
    static byte getDistance();

  private:
    //pin that trigger signal will be sent on
    static const byte trigPin;
    //pin that response will be read from
    static const byte echoPin;
    
    //time between trigger and echo
    static int duration;
    //distance in centimeters
    static byte distance;
};

#endif
