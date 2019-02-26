#ifndef PixyCam_h
#define PixyCam_h

#include "Arduino.h"
#include <Pixy2.h>
#include <PIDLoop.h>

class PixyCam {
  public:
    static void initialize();
    static void refresh(byte state);
    static bool getObjInView();
    static byte getXValue();
  
  private:
    //Pixy2 object
    static Pixy2 pixy;
    //pid loops
    static PIDLoop pan;
    static PIDLoop tilt;
    //previous state of Pixy2
    static byte prevState;
    //variables for calculating average coordinates of detected objects
    static int totalX;
    static int totalY;
    static int avgX;
    static int avgY;
    //error of x and y of object from center of view
    static int panError;
    static int tiltError;
    //values sent to servos
    static int panValue;
    static int tiltValue;
    //true if something is detected by camera
    static bool objInView;
    //x-value of point for the robot to turn towards
    static byte xValue;
//    //angle offset necessary to get line sensor to floor tape in time
//    static int angleOffset;

    static void reset();
    static void updateServos(bool obj, byte numObjects = 0);
    static void updateXValue(byte state);
//    static void updateAngleOffset();
//    static void distanceTest();
};

#endif