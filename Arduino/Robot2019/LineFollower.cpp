//#include "LineFollower.h"
//
//const byte LineFollower::sensorPin[3] = {A3, A2, A1};
//const int LineFollower::threshold = 75;
//
//int LineFollower::rawLineData[3];
//bool LineFollower::lineDataArray[3];
//byte LineFollower::lineData;
//byte LineFollower::turnValue;
//
//void LineFollower::initialize() {
//  for (byte i = 0; i < sizeof(sensorPin) / sizeof(byte); i ++)
//    pinMode(sensorPin[i], INPUT);
//}
//
//byte LineFollower::getTurnValue() {
//  return turnValue;
//}
//
//void LineFollower::updateLineData() {
//  for (byte i = 0; i < sizeof(sensorPin) / sizeof(byte); i ++) {
//    rawLineData[i] = analogRead(sensorPin[i]);
//    lineDataArray[i] = rawLineData[i] > 75;
//  }
//  lineData = lineDataArray[0] << 2 | lineDataArray[1] << 1 | lineDataArray[2];
//  updateTurnValue();
//  Serial.print(lineData, BIN); Serial.print("\t"); Serial.println(turnValue, DEC);
//}
//
//void LineFollower::updateTurnValue() {
//  switch (lineData) {
//    case B000:
//      turnValue = 0;
//      break;
//    case B100:
//      turnValue = 1;
//      break;
//    case B110:
//      turnValue = 2;
//      break;
//    case B111:
//      turnValue = 3;
//      break;
//    case B011:
//      turnValue = 4;
//      break;
//    case B001:
//      turnValue = 5;
//      break;
//    default:
//      turnValue = 6;
//      break;
//  }
//}
