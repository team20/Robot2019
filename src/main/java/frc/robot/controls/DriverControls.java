package frc.robot.controls;

import frc.robot.subsystems.*;

public class DriverControls extends PS4Controller {

    public static DriverControls singletonInstance = new DriverControls(0, 2);

    private double speedStraight, speedLeft, speedRight;
    private static boolean climberOverride, climberRetract, climbingMode;
    private boolean climbingLevelThree = true;

    static {

        climbingMode = false;
    }


    //private boolean testBool = cameraSwitch.getBoolean(true);//default true for front camera

    private boolean prevRightBumper = false;

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
        speedStraight = 0;
        speedLeft = 0;
        speedRight = 0;
        boolean rightBumper = getRightBumperButton();
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
                speedStraight = Climber.retractBackLeg(getLeftYAxis());
//                Climber.back.set(-getLeftYAxis());
            } else {
                Climber.back.set(0.0);
            }

            if (Climber.getBackEncPosition() > Climber.backHab3Height / 2) {
                speedStraight = Math.min(speedStraight, 0.5);
            }

            if (rightBumper && !prevRightBumper) {
                Climber.stepNumL3++;
                Climber.stepNumL2++;
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
                if (getTriButton() && LineSensor.isLineSeen()) {
                    //line sensor
                    if (!LineSensor.linePid.isEnabled())
                        LineSensor.linePid.enable();
                    if (!LineSensor.isBroken()) {
                        speedRight = -LineSensor.getTurnSpeed();
                        speedLeft = LineSensor.getTurnSpeed();
                        if (getRumble() != 0)
                            setRumble(0);
                    } else if (getRumble() != 1)
                        setRumble(1);
                    // else {
                    //     //Pixy camera
                    //     if (!Arduino.pixyPid.isEnabled())
                    //         Arduino.pixyPid.enable();
                    //     if (Arduino.isObjInView()) {
                    //         speedRight -= Arduino.getTurnSpeed();
                    //         speedLeft += Arduino.getTurnSpeed();
                    //         if (getRumble() != 0)
                    //             setRumble(0);
                    //     } else if (getRumble() != 1)
                    //         setRumble(1);
                    // }
                } else {
                    if (getRumble() != 0)
                        setRumble(0);
                    if (LineSensor.linePid.isEnabled()) {
                        LineSensor.linePid.reset();
                        LineSensor.setTurnSpeed(0);
                    }
                    if (Elevator.aboveStageThreshold()) {
                        if (getSquareButton()) {
                            speedLeft = getLeftTriggerAxis() * 0.4;
                            speedRight = getRightTriggerAxis() * 0.4;
                            Drivetrain.frontLeft.configOpenloopRamp(0.45);
                            Drivetrain.frontRight.configOpenloopRamp(0.45);
                        } else {
                            speedLeft = getLeftTriggerAxis() * 0.5;
                            speedRight = getRightTriggerAxis() * 0.5;
                            Drivetrain.frontLeft.configOpenloopRamp(0.45);
                            Drivetrain.frontRight.configOpenloopRamp(0.45);
                        }
                    } else {
                        if (getSquareButton()) {
                            speedLeft = getLeftTriggerAxis() * 0.6;
                            speedRight = getRightTriggerAxis() * 0.6;
                            Drivetrain.frontLeft.configOpenloopRamp(0.1);
                            Drivetrain.frontRight.configOpenloopRamp(0.1);
                        } else {
                            speedLeft = getLeftTriggerAxis() * 0.75;
                            speedRight = getRightTriggerAxis() * 0.75;
                            Drivetrain.frontLeft.configOpenloopRamp(0.1);
                            Drivetrain.frontRight.configOpenloopRamp(0.1);
                        }
                    }
                }
            }


//            PrettyPrint.put("Cam Bool",cameraSwitch.getBoolean(true));


            if (!getButtonDDown() && !getXButton()) {
                Climber.stop();
            }

            //Intake Controls
            if (getRightBumperButton()) {
                Intake.spitCargo();
            } else {
                Intake.stopCargoRollers();
            }
        }

        Drivetrain.drive(speedStraight, speedRight, speedLeft);

        // Auto Climb
        if (getXButton()) {
            Climber.climbLevelThree();
            climbingMode = true;
        }

        if (getButtonDDown()) {
            Climber.climbLevelTwo();
            climbingMode = true;
        }

        if (getTrackpadButton() && getShareButton()) {
            Climber.stepNumL3 = 0;
            Climber.stepNumL2 = 0;
        }

        if (getOptionsButton()) {
            Drivetrain.setBrakeMode(true);
        }

        if (getShareButton()) {
            Drivetrain.setBrakeMode(false);
        }
        prevRightBumper = rightBumper;
    }

    public static boolean isOverridingAuto() {
        return singletonInstance.anythingPressed();
    }

    public static boolean isStoppingAutoControl() {
        return singletonInstance.getTrackpadButton();
    }
}