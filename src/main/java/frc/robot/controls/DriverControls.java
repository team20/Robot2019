package frc.robot.controls;

import edu.wpi.first.wpilibj.PIDController;
import frc.robot.subsystems.*;

import static frc.robot.subsystems.Arm.Position.STARTING_CONFIG;

public class DriverControls {
    public static PS4Controller driverJoy;
    private static double speedStraight, speedLeft, speedRight;
    private static boolean climberOverride, climberRetract;

    private static final PIDController linePid;

    /*
     * Initializes the driver controller
     */
    static {
        driverJoy = new PS4Controller(0, 2);
        speedStraight = 0;
        speedLeft = 0;
        speedRight = 0;
        climberOverride = false;
        climberRetract = false;

        linePid = new PIDController(0.002, 0, 0, LineSensor.pidSource, LineSensor.pidOutput);
        linePid.setSetpoint(350);
    }

    /**
     * Runs the driver controls
     */
    public static void driverControls() {

        //Drivetrain Controls
        if (Math.abs(driverJoy.getLeftYAxis()) > 0.1) {
            speedStraight = -driverJoy.getLeftYAxis();
        } else {
            speedStraight = 0.0;
        }

        if (!climberOverride) {
            if (!driverJoy.getTriButton()) {
                linePid.reset();
                if (Elevator.aboveStageThreshold()) {
                    if (driverJoy.getSquareButton()) {
                        speedLeft = driverJoy.getLeftTriggerAxis() * 0.25;
                        speedRight = driverJoy.getRightTriggerAxis() * 0.25;
                        Drivetrain.frontLeft.configOpenloopRamp(0.4);
                        Drivetrain.frontRight.configOpenloopRamp(0.4);
                    } else {
                        speedLeft = driverJoy.getLeftTriggerAxis() * 0.4;
                        speedRight = driverJoy.getRightTriggerAxis() * 0.4;
                        Drivetrain.frontLeft.configOpenloopRamp(0.4);
                        Drivetrain.frontRight.configOpenloopRamp(0.4);
                    }
                } else {
                    if (driverJoy.getSquareButton()) {
                        speedLeft = driverJoy.getLeftTriggerAxis() * 0.6;
                        speedRight = driverJoy.getRightTriggerAxis() * 0.6;
                        Drivetrain.frontLeft.configOpenloopRamp(0.0);
                        Drivetrain.frontRight.configOpenloopRamp(0.0);
                    } else {
                        speedLeft = driverJoy.getLeftTriggerAxis() * 0.75;
                        speedRight = driverJoy.getRightTriggerAxis() * 0.75;
                        Drivetrain.frontLeft.configOpenloopRamp(0.0);
                        Drivetrain.frontRight.configOpenloopRamp(0.0);
                    }
                }
            } else {
                if (LineSensor.isLineSeen()) {
                    linePid.enable();
                    speedRight = -LineSensor.getTurnSpeed();
                    speedLeft = LineSensor.getTurnSpeed();
                }
            }
        }
        Drivetrain.drive(speedStraight, speedRight, speedLeft);

        //Intake Controls
        if (driverJoy.getRightBumperButton()) {
            Intake.spitCargo();
        } else {
            Intake.stopCargoRollers();
        }
        if (driverJoy.getLeftBumperButton()) {
//            Intake.closeHatch();
            Elevator.dropHatch();
        } else {
            Elevator.setHatchDrop = false;
        }

        //Climber Controls
        //extend
        if (driverJoy.getXButton()) {
            if (!driverJoy.getButtonDUp()) Climber.stop();
            Climber.balanceClimb(0.8);
            if (driverJoy.getButtonDUp()) {
                Climber.manualClimbBoth(.3);
            }
            Arm.setPosition(STARTING_CONFIG);
        } else {
            climberOverride = driverJoy.getCircleButton(); //was right bumper
            if (climberOverride) {
                Climber.manualClimbFront(-driverJoy.getRightTriggerAxis());
                Climber.manualClimbBack(-driverJoy.getLeftTriggerAxis());
            } else {
                Climber.stop();
            }
        }
        //retract
        if (Math.abs(driverJoy.getRightYAxis()) > 0.1/* && !climberOverride*/) {
            Climber.retractClimber(driverJoy.getRightTriggerAxis());
            climberRetract = true;
        } else {
            if (climberRetract) {
                Climber.stop();
            }
        }
    }

    public static boolean getShareButton() {
        return driverJoy.getShareButton();
    }

    public static boolean isOverridingAuto() {
        return driverJoy.getTriButton() ||
                driverJoy.getSquareButton() ||
                driverJoy.getCircleButton() ||
                driverJoy.getXButton() ||

                driverJoy.getPSButton() ||
                driverJoy.getShareButton() ||
                driverJoy.getOptionsButton() ||

                driverJoy.getButtonDDown() ||
                driverJoy.getButtonDLeft() ||
                driverJoy.getButtonDRight() ||
                driverJoy.getButtonDUp() ||

                driverJoy.getLeftBumperButton() ||
                Math.abs(driverJoy.getLeftTriggerAxis()) > .1 ||

                driverJoy.getRightBumperButton() ||
                Math.abs(driverJoy.getRightTriggerAxis()) > .1 ||

                driverJoy.getLeftStickButton() ||
                Math.abs(driverJoy.getLeftXAxis()) > .1 ||
                Math.abs(driverJoy.getLeftYAxis()) > .1 ||

                driverJoy.getRightStickButton() ||
                Math.abs(driverJoy.getRightXAxis()) > .1 ||
                Math.abs(driverJoy.getRightYAxis()) > .1;
    }

    // TODO this button
    public static boolean isStoppingAutoControl() {
        return driverJoy.getTrackpadButton();
    }
}