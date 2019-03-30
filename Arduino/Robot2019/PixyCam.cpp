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

void PixyCam::initialize() {
  pixy.init();
  pixy.changeProg("color");
  pixy.setCameraBrightness(15);
  reset();
  prevState = -1;
  objInView = false;
  xValue = 0;
}

void PixyCam::refresh(byte state) {
  switch (state) {
    case 0:
      if (state != prevState)
        reset();
      break;
    case 1:
      //get object data
      pixy.ccc.getBlocks(2);
      //if object(s) detected...
      if (pixy.ccc.numBlocks) {
        if (!objInView)
          objInView = true;
        updateServos();
        updateXValue();
      } else if (objInView) {
        objInView = false;
        reset();
      }
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

void PixyCam::updateServos() {
  //find average coordinates of number of objects specified
  totalX = 0;
  totalY = 0;
  for (byte i = 0; i < 2; i ++) {
    totalX += pixy.ccc.blocks[i].m_x;
    totalY += pixy.ccc.blocks[i].m_y;
  }
  avgX = totalX / 2;
  avgY = totalY / 2;
  //calculate error in angle based off of average coordinates of objects
  panError = pixy.frameWidth / 2 - avgX;
  tiltError = avgY - pixy.frameHeight / 2;
  //update PID loops
  pan.update(panError);
  tilt.update(tiltError);
  //set servos
  panValue = pan.m_command;
  tiltValue = tilt.m_command;
  pixy.setServos(panValue, tiltValue);
}

void PixyCam::updateXValue() {
  //maps from servo value to servo angle (precision of 2 degrees)
  xValue = map(panValue, 0, 1000, 75, 0);
}
