#ifndef LEDStrip_h
#define LEDStrip_h

#include "Arduino.h"
#include <Adafruit_NeoPixel.h>

class LEDStrip {
  public:
    static void initialize(byte pin, byte numLEDs, byte brightness);
    static void updateDisplay(byte allainceColor, byte pattern, byte diagnosticColor, byte diagnosticPattern);

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
    static unsigned long mainTimeStamp;
    static unsigned long diagnosticTimeStamp;
    //alliance color
    static uint32_t allianceColor;
    //diagnostic color
    static uint32_t diagnosticColor;
    //stores previously called pattern to check if the new one is different
    static byte prevPattern;
    //has the startup pattern run yet?
    static bool robotOn;
    //for blinking diagnostic LEDs
    static bool diagnosticOn;
    
    //one function for each pattern
    //all LEDs off
    static void allOff();
    //green fade in then out
    static void robotReady();
    //chasing up
    static void chasing();
    //green flowing up and down
    static void greenFlowing();
    //elevator height
    static void elevator(byte height);
    //diagnostic LEDs solid on
    static void diagnosticSolid();
    //diagnostic LEDs blinking
    static void diagnosticBlinking();
};

#endif