#include "LEDStrip.h"

Adafruit_NeoPixel LEDStrip::strip;

const byte LEDStrip::pixelSpacing = 5;
const uint32_t LEDStrip::off = strip.Color(0, 0, 0);
const uint32_t LEDStrip::red = strip.Color(255, 0, 0);
const uint32_t LEDStrip::orange = strip.Color(255, 63, 0);
const uint32_t LEDStrip::yellow = strip.Color(255, 127, 0);
const uint32_t LEDStrip::green = strip.Color(0, 255, 0);
const uint32_t LEDStrip::blue = strip.Color(0, 0, 255);
const uint32_t LEDStrip::purple = strip.Color(0, 255, 255);
const uint32_t LEDStrip::white = strip.Color(255, 255, 255);

int LEDStrip::counter;
unsigned long LEDStrip::timeStamp;
byte LEDStrip::prevPattern;

void LEDStrip::initialize(byte pin, byte numLEDs, byte brightness) {
  strip = Adafruit_NeoPixel(numLEDs, pin, NEO_GRB + NEO_KHZ800);
  strip.begin();
  strip.setBrightness(brightness);
  strip.show();
  counter = 0;
  timeStamp = millis();
}

void LEDStrip::displayPattern(byte pattern, byte diagnosticColor) {
  if (pattern != prevPattern) {
    allOff();
    counter = 0;
    timeStamp = millis();
  }
  //selects correct pattern to display
  switch (pattern) {
    case 0:
      allOff();
      break;
    case 1:
      robotReady();
      break;
    case 2:
      redChasing();
      break;
    case 3:
      blueChasing();
      break;
    case 4:
      greenFlowing();
      break;
    default:
      if (pattern >= 5 && pattern <= 25)
        redElevator(pattern - 5);
      else if (pattern >= 26 && pattern <= 46)
        blueElevator(pattern - 26);
      break;
  }
  switch (diagnosticColor) {
    case 1:
      setDiagnosticColor(red);
      break;
    case 2:
      setDiagnosticColor(orange);
      break;
    case 3:
      setDiagnosticColor(yellow);
      break;
    case 4:
      setDiagnosticColor(green);
      break;
    case 5:
      setDiagnosticColor(blue);
      break;
    case 6:
      setDiagnosticColor(purple);
      break;
  }
  //send data to LED strip
  strip.show();
  //store value of previous pattern for future comparison
  prevPattern = pattern;
}

void LEDStrip::allOff() {
  for (byte i = 0; i < strip.numPixels(); i ++)
    strip.setPixelColor(i, off);
}

void LEDStrip::robotReady() {
  if (millis() - timeStamp >= 10) {
    timeStamp = millis();
    if (counter < 511) {
      for (byte i = 0; i < strip.numPixels(); i ++) {
        if (counter < 256)
          strip.setPixelColor(i, strip.Color(0, counter, 0));
        else
          strip.setPixelColor(i, strip.Color(0, 511 - counter, 0));
      }
      counter ++;
    }
  }
}

//void LEDStrip::redLowGear() {
//  if (millis() - timeStamp >= 80) {
//    timeStamp = millis();
//    if (!(counter < pixelSpacing))
//      counter = 0;
//    for (int i = strip.numPixels() - counter; i >= -1; i -= pixelSpacing) {
//      strip.setPixelColor(i, red);
//      strip.setPixelColor(i + 1, off);
//    }
//    counter ++;
//  }
//}

void LEDStrip::redChasing() {
  if (millis() - timeStamp >= 80) {
    timeStamp = millis();
    if (!(counter < pixelSpacing))
      counter = 0;
    for (byte j = counter; j <= strip.numPixels(); j += pixelSpacing) {
      strip.setPixelColor(j, red);
      strip.setPixelColor(j - 1, off);
    }
    counter ++;
  }
}

//void LEDStrip::blueLowGear() {
//  if (millis() - timeStamp >= 80) {
//    timeStamp = millis();
//    if (!(counter < pixelSpacing))
//      counter = 0;
//    for (int i = strip.numPixels() - counter; i >= -1; i -= pixelSpacing) {
//      strip.setPixelColor(i, blue);
//      strip.setPixelColor(i + 1, off);
//    }
//    counter ++;
//  }
//}

void LEDStrip::blueChasing() {
  if (millis() - timeStamp >= 80) {
    timeStamp = millis();
    if (!(counter < pixelSpacing))
      counter = 0;
    for (byte j = counter; j <= strip.numPixels(); j += pixelSpacing) {
      strip.setPixelColor(j, blue);
      strip.setPixelColor(j - 1, off);
    }
    counter ++;
  }
}

void LEDStrip::greenFlowing() {
  //TODO: make this
}

void LEDStrip::redElevator(byte height) {
  for (byte i = 0; i < strip.numPixels(); i ++) {
    if (i < height)
      strip.setPixelColor(i, red);
    else
      strip.setPixelColor(i, off);
  }
}

void LEDStrip::blueElevator(byte height) {
  for (byte i = 0; i < strip.numPixels(); i ++) {
    if (i < height)
      strip.setPixelColor(i, blue);
    else
      strip.setPixelColor(i, off);
  }
}

void LEDStrip::setDiagnosticColor(uint32_t color) {
  for (byte i = 14; i < 20; i ++)
    strip.setPixelColor(i, color);
}
