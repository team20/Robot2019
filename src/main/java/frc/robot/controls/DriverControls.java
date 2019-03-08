package frc.robot.controls;

import frc.robot.subsystems.*;

public class DriverControls {
    private static PS4Controller joy;
    private static double speedStraight, speedLeft, speedRight;
    private static boolean climberOverride, climberRetract, climbingMode;

    /*
     * Initializes the driver controller
     */
    static {
        joy = new PS4Controller(0, 2);
        speedStraight = 0;
        speedLeft = 0;
        speedRight = 0;
        climberOverride = false;
        climberRetract = false;
        climbingMode = false;
    }

    /**
     * Runs the driver controls
     */
    public static void driverControls() {

        if (joy.getButtonDRight()) climbingMode = true;
        if (joy.getButtonDLeft()) climbingMode = false;

        if (climbingMode) {
            if (joy.getRightTriggerAxis() > .2) {
                speedStraight = joy.getRightTriggerAxis();
                speedLeft = 0;
                speedRight = 0;
            } else if (joy.getLeftTriggerAxis() < 0.2) {
                speedStraight = 0.0;
            }
            if (joy.getLeftTriggerAxis() > .2) {
                speedStraight = -joy.getLeftTriggerAxis();
                speedLeft = 0;
                speedRight = 0;
            } else if (joy.getRightTriggerAxis() < 0.2) {
                speedStraight = 0.0;
            }

            if (joy.getRightYAxis() > .1 || joy.getRightYAxis() < -.1) {
                Climber.manualClimbFront(-joy.getRightYAxis());
            } else {
                Climber.manualClimbFront(0.0);
            }
            if (joy.getLeftYAxis() > .1 || joy.getLeftYAxis() < -.1) {
                Climber.manualClimbBack(-joy.getLeftYAxis());
            } else {
                Climber.manualClimbBack(0.0);
            }
        } else {
            //Drivetrain Controls
            if (Math.abs(joy.getLeftYAxis()) > 0.1) {
                speedStraight = -joy.getLeftYAxis();
            } else {
                speedStraight = 0.0;
            }

            if (!climberOverride) {
                //line sensor
                if (joy.getTriButton() && LineSensor.isLineSeen()) {
                    // if (!LineSensor.linePid.isEnabled())
                    //     LineSensor.linePid.enable();
                    // LineSensor.calculateLinePosition();
                    // speedRight = -LineSensor.getTurnSpeed();
                    // speedLeft = LineSensor.getTurnSpeed();
                } else {
                    if (LineSensor.linePid.isEnabled())
                        LineSensor.linePid.reset();
                    if (Elevator.aboveStageThreshold()) {
                        if (joy.getSquareButton()) {
                            speedLeft = joy.getLeftTriggerAxis() * 0.25;
                            speedRight = joy.getRightTriggerAxis() * 0.25;
                            Drivetrain.frontLeft.configOpenloopRamp(0.4);
                            Drivetrain.frontRight.configOpenloopRamp(0.4);
                        } else {
                            speedLeft = joy.getLeftTriggerAxis() * 0.4;
                            speedRight = joy.getRightTriggerAxis() * 0.4;
                            Drivetrain.frontLeft.configOpenloopRamp(0.4);
                            Drivetrain.frontRight.configOpenloopRamp(0.4);
                        }
                    } else {
                        if (joy.getSquareButton()) {
                            speedLeft = joy.getLeftTriggerAxis() * 0.6;
                            speedRight = joy.getRightTriggerAxis() * 0.6;
                            Drivetrain.frontLeft.configOpenloopRamp(0.0);
                            Drivetrain.frontRight.configOpenloopRamp(0.0);
                        } else {
                            speedLeft = joy.getLeftTriggerAxis() * 0.75;
                            speedRight = joy.getRightTriggerAxis() * 0.75;
                            Drivetrain.frontLeft.configOpenloopRamp(0.0);
                            Drivetrain.frontRight.configOpenloopRamp(0.0);
                        }
                    }
                }
            }

            //Climber Controls
            //extend
            if (!joy.getXButton()) {
                if (Climber.getBackEncPosition() < 20)
                    Arduino.setDiagnosticPattern(Arduino.Colors.Purple, 1);

                climberOverride = joy.getCircleButton(); //was right bumper
                if (climberOverride) {
                    Climber.manualClimbFront(-joy.getRightTriggerAxis());
                    Climber.manualClimbBack(-joy.getLeftTriggerAxis());
                } else {
                    Climber.stop();
                }
            }
            //retract
            if (Math.abs(joy.getRightYAxis()) > 0.1) {
                Climber.retractClimber(joy.getRightTriggerAxis());
                climberRetract = true;
            } else {
                if (climberRetract) {
                    Climber.stop();
                }
            }
        }

        Drivetrain.drive(speedStraight, speedRight, speedLeft);

        if (joy.getXButton()) {
            Climber.balanceClimb(1.0);
        }

        //Intake Controls
        if (joy.getRightBumperButton()) {
            Intake.spitCargo();
        } else {
            Intake.stopCargoRollers();
        }
        if (joy.getLeftBumperButton()) {
            Elevator.dropHatch();
        } else {
            Elevator.setHatchDrop = false;
        }

        if (joy.getOptionsButton()) {
            Drivetrain.setBrakeMode(true);
        }

        if (joy.getShareButton()) {
            Drivetrain.setBrakeMode(false);
        }
    }

    public static boolean getShareButton() {
        return joy.getShareButton();
    }

    public static boolean isOverridingAuto() {
        return joy.anythingPressed();
    }

    // TODO this button
    public static boolean isStoppingAutoControl() {
        return joy.getTrackpadButton();
    }
}