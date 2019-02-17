#ifndef LEDStrip_h
#define LEDStrip_h

#include "Arduino.h"
#include <Adafruit_NeoPixel.h>

class LEDStrip {
  public:
    static void initialize(byte pin, byte numLEDs, byte brightness);
    static void displayPattern(byte pattern, byte diagnosticColor);

  private:
    //LED strip object
    static Adafruit_NeoPixel strip;

    //spacing between pixels for chasing patterns
    static const byte pixelSpacing;
    //constant RGB values for different colors
    static const uint32_t off;
    static const uint32_t red;
    static const uint32_t orange;
    static const uint32_t yellow;
    static const uint32_t green;
    static const uint32_t blue;
    static const uint32_t purple;
    static const uint32_t white;
    
    //global counter to immitate for loops with if statements
    static int counter;
    //for keeping things timed correctly
    static unsigned long timeStamp;
    //stores previously called pattern to check if the new one is different
    static byte prevPattern;
    
    //one function for each pattern
    //all LEDs off
    static void allOff();
    //green fade in then out
    static void robotReady();
    //red chasing up
    static void redChasing();
    //blue chasing up
    static void blueChasing();
    //green flowing up and down
    static void greenFlowing();
    //red elevator height
    static void redElevator(byte height);
    //blue elevator height
    static void blueElevator(byte height);

    //diagnostic colors
    static void setDiagnosticColor(uint32_t color);
};

#endif
