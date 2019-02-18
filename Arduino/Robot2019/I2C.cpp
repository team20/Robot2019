#include "I2C.h"

byte I2C::readData[4];
byte I2C::writeData[3];

void I2C::initialize(byte address) {
  Wire.begin(address);
  Wire.onReceive(receiveEvent);
  Wire.onRequest(requestEvent);
  for (byte i = 0; i < sizeof(readData) / sizeof(byte); i ++)
    readData[i] = 0;
  for (byte i = 0; i < sizeof(writeData) / sizeof(byte); i ++)
    writeData[i] = 0;
}

void I2C::receiveEvent() {
  //for every byte received...
  for (byte i = 0; Wire.available() > 0 && i < sizeof(readData) / sizeof(byte); i ++)
    //store it in readData[]
    readData[i] = Wire.read();
}

void I2C::requestEvent() {
  //send all data in writeData[]
  Wire.write(writeData, sizeof(writeData) / sizeof(byte));
}

byte I2C::getPattern() {
  return readData[0];
}

byte I2C::getDiagnosticColor() {
  return readData[1];
}

byte I2C::getPixyCamState() {
  return readData[2];
}

bool I2C::getUltrasonicState() {
  return readData[3] == 1;
}

void I2C::setWriteData(bool objInView, byte xValue, byte distance) {
  writeData[0] = objInView ? 1 : 0;
  writeData[1] = xValue;
  writeData[2] = distance;
}
