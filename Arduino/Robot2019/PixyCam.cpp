#include "PixyCam.h"

Pixy2 PixyCam::pixy;
PIDLoop PixyCam::pan = PIDLoop(400, 0, 400, true);
PIDLoop PixyCam::tilt = PIDLoop(500, 0, 500, true);

byte PixyCam::prevState;
int PixyCam::totalX;
int PixyCam::totalY;
int PixyCam::avgX;
int PixyCam::avgY;
int PixyCam::panError;
int PixyCam::tiltError;
int PixyCam::panValue;
int PixyCam::tiltValue;
bool PixyCam::objInView;
byte PixyCam::xValue;
//int PixyCam::angleOffset;

void PixyCam::initialize() {
  pixy.init();
  pixy.changeProg("color");
  reset();
  prevState = -1;
  objInView = false;
  xValue = 0;
//  angleOffset = 0;
}

void PixyCam::refresh(byte state) {
  switch (state) {
    case 0:
      if (state != prevState)
        reset();
      break;
    case 1:
      if (state != prevState) {
        pixy.changeProg("color");
        pixy.setServos(panValue, tiltValue);
        pixy.setCameraBrightness(15);
//        angleOffset = 0;
      }
      //get object data
      pixy.ccc.getBlocks(2);
      //if object(s) detected...
      if (pixy.ccc.numBlocks) {
        if (!objInView)
          objInView = true;
        updateServos(true, 2);
        updateXValue(state);
      } else if (objInView) {
        objInView = false;
        reset();
      }
      break;
    case 2:
      if (state != prevState) {
        pixy.changeProg("line");
        pixy.setServos(panValue, 750);
        pixy.setCameraBrightness(50);
      }
      //get line data
      pixy.line.getAllFeatures(LINE_VECTOR, false);
      //if line detected...
      if (pixy.line.numVectors) {
        if (!objInView)
          objInView = true;
//        updateServos(false);
//        updateAngleOffset();
        updateXValue(state);
      } else if (objInView)
        objInView = false;
      break;
  }
  prevState = state;
}

bool PixyCam::getObjInView() {
  return objInView;
}

byte PixyCam::getXValue() {
  return xValue;
}

void PixyCam::reset() {
  //reset PID loops and servos
  pan.reset();
  tilt.reset();
  panValue = pan.m_command;
  tiltValue = tilt.m_command;
  pixy.setServos(panValue, tiltValue);
}

void PixyCam::updateServos(bool obj, byte numObjects = 0) {
  //find average coordinates of number of objects specified
  totalX = 0;
  totalY = 0;
  //if in ccc mode...
  if (obj) {
    for (byte i = 0; i < numObjects; i ++) {
      totalX += pixy.ccc.blocks[i].m_x;
      totalY += pixy.ccc.blocks[i].m_y;
    }
    avgX = totalX / numObjects;
    avgY = totalY / numObjects;
    //calculate error in angle based off of average coordinates of objects
    panError = pixy.frameWidth / 2 - avgX;
    tiltError = avgY - pixy.frameHeight / 2;
    //update PID loops
    pan.update(panError);
    tilt.update(tiltError);
    //set servos
    panValue = pan.m_command;
    tiltValue = tilt.m_command;
  } else {
    //calculate error in angle based off of coordinates of floor tape vector
    panError = map(pixy.frameWidth, 0, 78, 0, 315) / 2 - map(pixy.line.vectors[0].m_x0, 0, 78, 0, 315);
    //update PID loops
    pan.update(panError);
    //set servos
    panValue = pan.m_command;
    Serial.println(panValue);
    tiltValue = 750;
  }
  pixy.setServos(panValue, tiltValue);
}

void PixyCam::updateXValue(byte state) {
  switch (state) {
    case 1:
      //maps from servo value to servo angle (precision of 2 degrees)
      xValue = map(panValue, 0, 1000, 75, 0);    // + angleOffset / 2;
      break;
    case 2:
      xValue = pixy.line.vectors[0].m_x0;
      break;
  }
}

//void PixyCam::updateAngleOffset() {
//  //uses perceived angle of floor tape to offset angle of robot so the line sensors get to it in time
//  angleOffset = atan((double)(pixy.line.vectors[0].m_x0 - pixy.line.vectors[0].m_x1) / (double)(pixy.line.vectors[0].m_y0 - pixy.line.vectors[0].m_y1)) * (180 / PI);
//}

//void PixyCam::distanceTest() {
//  double distanceValue = pow(51 - pixy.line.vectors[0].m_y0, 0.897) + 6;
//  Serial.print("distance: "); Serial.println(distanceValue);
//}