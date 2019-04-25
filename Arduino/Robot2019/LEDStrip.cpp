#include "LEDStrip.h"

Adafruit_NeoPixel LEDStrip::strip;

const byte LEDStrip::pixelSpacing = 5;
//order for RGB values is RBG
const uint32_t LEDStrip::off = strip.Color(0, 0, 0);
const uint32_t LEDStrip::red = strip.Color(255, 0, 0);
const uint32_t LEDStrip::orange = strip.Color(255, 0, 31);
const uint32_t LEDStrip::yellow = strip.Color(255, 0, 127);
const uint32_t LEDStrip::green = strip.Color(0, 0, 255);
const uint32_t LEDStrip::blue = strip.Color(0, 255, 0);
const uint32_t LEDStrip::purple = strip.Color(127, 255, 0);
const uint32_t LEDStrip::white = strip.Color(255, 255, 255);

int LEDStrip::counter;
unsigned long LEDStrip::mainTimeStamp;
unsigned long LEDStrip::diagnosticTimeStamp;
uint32_t LEDStrip::allianceColor;
uint32_t LEDStrip::diagnosticColor;
byte LEDStrip::prevPattern;
bool LEDStrip::robotOn;
bool LEDStrip::diagnosticOn;

void LEDStrip::initialize(byte pin, byte numLEDs, byte brightness) {
  strip = Adafruit_NeoPixel(numLEDs, pin, NEO_GRB + NEO_KHZ800);
  strip.begin();
  strip.setBrightness(brightness);
  strip.show();
  counter = 0;
  mainTimeStamp = millis();
  diagnosticTimeStamp = millis();
  robotOn = false;
}

void LEDStrip::updateDisplay(byte ac, byte pattern, byte dc, byte diagnosticPattern) {
  //reset pixels if pattern changed
  if (pattern != prevPattern) {
    allOff();
    counter = 0;
    mainTimeStamp = millis();
  }
  //sets alliance color
  switch (ac) {
    case 0:
      allianceColor = red;
      break;
    case 1:
      allianceColor = blue;
      break;
    case 2:
      allianceColor = green;
      break;
  }
  //sets pattern
  switch (pattern) {
    case 0:
      allOff();
      break;
    case 1:
      robotReady();
      break;
    case 2:
      chasing();
      break;
    case 3:
      greenFlowing();
      break;
    default:
      if (pattern >= 4 && pattern <= 19)
        elevator(pattern - 4);
      break;
  }
  //sets diagnostic color
  switch (dc) {
    case 0:
      diagnosticColor = red;
      break;
    case 1:
      diagnosticColor = orange;
      break;
    case 2:
      diagnosticColor = yellow;
      break;
    case 3:
      diagnosticColor = green;
      break;
    case 4:
      diagnosticColor = blue;
      break;
    case 5:
      diagnosticColor = purple;
      break;
  }
  //sets diagnostic pattern
  switch (diagnosticPattern) {
    case 1:
      diagnosticSolid();
      break;
    case 2:
      diagnosticBlinking();
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

// Reverted to HVR version
void LEDStrip::robotReady() {
  if (!robotOn) {
    for (int i = 0; i < 256; i ++) {
      for (byte j = 0; j < strip.numPixels(); j ++)
        strip.setPixelColor(j, strip.Color(0, 0, i));
      strip.show();
      delay(10);
    }
    for (int i = 255; i >= 0; i --) {
      for (byte j = 0; j < strip.numPixels(); j ++)
        strip.setPixelColor(j, strip.Color(0, 0, i));
      strip.show();
      delay(10);
    }
    robotOn = true;
  }
}

void LEDStrip::chasing() {
  if (millis() - mainTimeStamp >= 80) {
    mainTimeStamp = millis();
    if (!(counter < pixelSpacing))
      counter = 0;
    for (byte j = counter; j <= strip.numPixels(); j += pixelSpacing) {
      strip.setPixelColor(j, allianceColor);
      strip.setPixelColor(j - 1, off);
    }
    counter ++;
  }
}

//void LEDStrip::chasingDown(uint32_t color) {
//  if (millis() - timeStamp >= 80) {
//    timeStamp = millis();
//    if (!(counter < pixelSpacing))
//      counter = 0;
//    for (int i = strip.numPixels() - counter; i >= -1; i -= pixelSpacing) {
//      strip.setPixelColor(i, color);
//      strip.setPixelColor(i + 1, off);
//    }
//    counter ++;
//  }
//}

// Reverted to HVR
void LEDStrip::greenFlowing() {
  if (millis() - mainTimeStamp >= 15) {
    mainTimeStamp = millis();
    if (counter < strip.numPixels() * 2 + 1) {
      if (counter < strip.numPixels())
        strip.setPixelColor(counter, green);
      else
        strip.setPixelColor(strip.numPixels() - (counter - strip.numPixels()), off);
      counter ++;
    } else
      counter = 0;
  }
}

void LEDStrip::elevator(byte height) {
  for (byte i = 0; i < strip.numPixels(); i ++) {
    if (i < height)
      strip.setPixelColor(i, allianceColor);
    else
      strip.setPixelColor(i, off);
  }
}

void LEDStrip::diagnosticSolid() {
  for (byte i = 11; i < 15; i ++)
    strip.setPixelColor(i, diagnosticColor);
}

void LEDStrip::diagnosticBlinking() {
  if (millis() - diagnosticTimeStamp >= 250) {
    diagnosticTimeStamp = millis();
    diagnosticOn = !diagnosticOn;
  }
  for (byte i = 11; i < 15; i ++)
    strip.setPixelColor(i, diagnosticOn ? diagnosticColor : off);
}
