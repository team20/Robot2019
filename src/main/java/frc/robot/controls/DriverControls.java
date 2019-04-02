package frc.robot.controls;

import frc.robot.subsystems.*;

public class DriverControls extends PS4Controller {
    private static DriverControls singletonInstance = new DriverControls(0, 2);

    private double speedStraight, speedLeft, speedRight;
    private boolean climberOverride, climberRetract, climbingMode;
    //    private NetworkTableEntry cameraSelector;
    private boolean camIsMain;
    private boolean climbingLevelThree = true;

    public static void driverControls() {
        singletonInstance.controls();
    }

    /**
     * Initializes the controller
     *
     * @param portMain   :   main port of the controller
     * @param portRumble : port with external PS4 drivers
     */
    private DriverControls(int portMain, int portRumble) {
        super(portMain, portRumble);
    }

    /**
     * Runs the driver controls
     */
    private void controls() {
        if (getButtonDRight()) climbingMode = true;
        if (getButtonDLeft()) climbingMode = false;

        if (climbingMode) {
            if (getRightTriggerAxis() > .2) {
                speedStraight = getRightTriggerAxis();
                speedLeft = 0;
                speedRight = 0;
            } else if (getLeftTriggerAxis() < 0.2) {
                speedStraight = 0.0;
            }
            if (getLeftTriggerAxis() > .2) {
                speedStraight = -getLeftTriggerAxis();
                speedLeft = 0;
                speedRight = 0;
            } else if (getRightTriggerAxis() < 0.2) {
                speedStraight = 0.0;
            }

            if (getRightYAxis() > .1 || getRightYAxis() < -.1) {
                Climber.front.set(-getRightYAxis());
            } else {
                Climber.front.set(0.0);
            }
            if (getLeftYAxis() > .1 || getLeftYAxis() < -.1) {
//                Climber.back.set(-getLeftYAxis());
                speedStraight = Climber.retractBackLeg(-getLeftYAxis());
            } else {
                Climber.back.set(0.0);
            }

            if (Climber.getBackEncPosition() > Climber.backHab3Height / 2) {
                speedStraight = Math.min(speedStraight, 0.5);
            }
        } else {
            //Drivetrain Controls
            if (Math.abs(getLeftYAxis()) > 0.1) {
                speedStraight = -getLeftYAxis();
            } else {
                speedStraight = 0.0;
            }

            if (!climberOverride) {
                //auto alignment
                if (getTriButton()) {
                    if (LineSensor.isLineSeen()) {
                        //line sensor
                        if (!LineSensor.linePid.isEnabled())
                            LineSensor.linePid.enable();
                        if (!LineSensor.isBroken()) {
                            speedRight -= LineSensor.getTurnSpeed();
                            speedLeft += LineSensor.getTurnSpeed();
                            if (getRumble() != 0)
                                setRumble(0);
                        } else if (getRumble() != 1)
                            setRumble(1);
                    } else {
                        //Pixy camera
                        if (!Arduino.pixyPid.isEnabled())
                            Arduino.pixyPid.enable();
                        if (Arduino.isObjInView()) {
                            speedRight -= Arduino.getTurnSpeed();
                            speedLeft += Arduino.getTurnSpeed();
                            if (getRumble() != 0)
                                setRumble(0);
                        } else if (getRumble() != 1)
                            setRumble(1);
                    }
                } else {
                    if (getRumble() != 0)
                        setRumble(0);
                    if (LineSensor.linePid.isEnabled())
                        LineSensor.linePid.reset();
                    if (Elevator.aboveStageThreshold()) {
                        if (getSquareButton()) {
                            speedLeft = getLeftTriggerAxis() * 0.25;
                            speedRight = getRightTriggerAxis() * 0.25;
                            Drivetrain.frontLeft.configOpenloopRamp(0.55); //shh don't tell victor
                            Drivetrain.frontRight.configOpenloopRamp(0.55); //shh don't tell victor
                        } else {
                            speedLeft = getLeftTriggerAxis() * 0.4;
                            speedRight = getRightTriggerAxis() * 0.4;
                            Drivetrain.frontLeft.configOpenloopRamp(0.55); //shh don't tell victor
                            Drivetrain.frontRight.configOpenloopRamp(0.55); //shh don't tell victor
                        }
                    } else {
                        if (getSquareButton()) {
                            speedLeft = getLeftTriggerAxis() * 0.6;
                            speedRight = getRightTriggerAxis() * 0.6;
                            Drivetrain.frontLeft.configOpenloopRamp(0.15); //shh don't tell victor
                            Drivetrain.frontRight.configOpenloopRamp(0.15); //shh don't tell victor
                        } else {
                            speedLeft = getLeftTriggerAxis() * 0.75;
                            speedRight = getRightTriggerAxis() * 0.75;
                            Drivetrain.frontLeft.configOpenloopRamp(0.15); //shh don't tell victor
                            Drivetrain.frontRight.configOpenloopRamp(0.15); //shh don't tell victor
                        }
                    }
                }
            }

            //Camera Controls
            if (getRightYAxis() < -.2) {
                camIsMain = true;
            } else if (getRightYAxis() > .2) {
                camIsMain = false;
            }

//            cameraSelector.setDouble(camIsMain ? 0 : 1);

            //Climber Controls
            //extend
//            if (!getXButton()) {
//                climberOverride = getCircleButton(); //was right bumper
//                if (climberOverride) {
//                    Climber.front.set(-getRightTriggerAxis());
//                    Climber.back.set(-getLeftTriggerAxis());
//                } else {
//                    Climber.stop();
//                }
//            }

//            //retract
//            if (Math.abs(getRightYAxis()) > 0.1) {
//                Climber.retractBackLeg(getRightTriggerAxis());
//                climberRetract = true;
//            } else {
//                if (climberRetract) {
//                    Climber.stop();
//                }
//            }
        }

        Drivetrain.drive(speedStraight, speedRight, speedLeft);

        // Auto Climb
        if (getXButton()) {
            Climber.climbLevelThree(1.0);
        }

        if (getButtonDDown()) {
            Climber.climbLevelTwo();
        }

        if (getTrackpadButton() && getShareButton()) {
            Climber.setStepNum(0);
        }

        //Intake Controls
        if (getRightBumperButton()) {
            Intake.spitCargo();
        } else {
            Intake.stopCargoRollers();
        }
        if (getLeftBumperButton()) {
            Elevator.dropHatch();
        } else {
            Elevator.setHatchDrop = false;
        }

        if (getOptionsButton()) {
            Drivetrain.setBrakeMode(true);
        }

        if (getShareButton()) {
            Drivetrain.setBrakeMode(false);
        }
    }

    public static boolean isOverridingAuto() {
        return singletonInstance.anythingPressed();
    }

    // TODO this button
    public static boolean isStoppingAutoControl() {
        return singletonInstance.getTrackpadButton();
    }
}