//#include "Ultrasonic.h"
//
//const byte Ultrasonic::trigPin = 5;
//const byte Ultrasonic::echoPin = 6;
//
//int Ultrasonic::duration;
//byte Ultrasonic::distance;
//
//void Ultrasonic::initialize() {
//  pinMode(trigPin, OUTPUT);
//  pinMode(echoPin, INPUT);
//  distance = 0;
//}
//
//void Ultrasonic::updateDistance() {
//  //trigger sensor to send out a pulse
//  digitalWrite(trigPin, LOW);
//  delayMicroseconds(2);
//  digitalWrite(trigPin, HIGH);
//  delayMicroseconds(10);
//  digitalWrite(trigPin, LOW);
//  //get time it took for wave to come back
//  duration = pulseIn(echoPin, HIGH);
//  //convert to inches
//  distance = (duration / 2) / 29.1 / 2.54;
//  if (distance > 60)
//    distance = 60;
//}
//
//byte Ultrasonic::getDistance() {
//  return distance;
//}
