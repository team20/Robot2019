#ifndef I2C_h
#define I2C_h

#include "Arduino.h"
#include <Wire.h>

class I2C {
  public:
    static void initialize(byte address);
    static void receiveEvent();
    static void requestEvent();
    static byte getPattern();
    static byte getDiagnosticColor();
    static byte getPixyCamState();
    static bool getLineFollowerState();
    static bool getUltrasonicState();
    static void setWriteData(bool objInView, byte angle, byte distance);

  private:
    //data received from RoboRio
    static byte readData[4];
    //data to send to RoboRio
    static byte writeData[3];
};

#endif
