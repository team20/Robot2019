package frc.robot.controls;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

public class DriverControls {
    private static PS4Controller driverJoy;
    private static double speedStraight, speedLeft, speedRight;
    private static boolean climberOverride, climberRetract;

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
            if (Elevator.aboveStageThreshold()) {
                if (driverJoy.getCircleButton()) {
                    speedLeft = driverJoy.getLeftTriggerAxis() * 0.25;
                    speedRight = driverJoy.getRightTriggerAxis() * 0.25;
                } else {
                    speedLeft = driverJoy.getLeftTriggerAxis() * 0.4;
                    speedRight = driverJoy.getRightTriggerAxis() * 0.4;
                }
            } else {
                if (driverJoy.getCircleButton()) {
                    speedLeft = driverJoy.getLeftTriggerAxis() * 0.33;
                    speedRight = driverJoy.getRightTriggerAxis() * 0.33;
                } else {
                    speedLeft = driverJoy.getLeftTriggerAxis() * 0.65;
                    speedRight = driverJoy.getRightTriggerAxis() * 0.65;
                }
            }
        }
        Drivetrain.drive(speedStraight, speedRight, speedLeft);

        //Intake Controls
        if (driverJoy.getRightBumperButton()) {
            Intake.spitCargo();
        }
        if (driverJoy.getLeftBumperButton()) {
            Intake.closeHatch();
        }

        //Climber Controls
        //extend
        if (driverJoy.getXButton()) {
            Climber.balanceClimb(0.4);
        } else {
            climberOverride = driverJoy.getSquareButton();
            if (climberOverride) {
                Climber.manualClimbFront(driverJoy.getLeftTriggerAxis());
                Climber.manualClimbBack(driverJoy.getRightTriggerAxis());
            }
        }
        //retract
        if (Math.abs(driverJoy.getRightYAxis()) > 0.1) {
            Climber.retractClimber(driverJoy.getRightTriggerAxis());
            climberRetract = true;
        } else {
            if (climberRetract) {
                Climber.stop();
            }
        }
    }

    // TODO determine these controls
    public static boolean isOverridingAuto() {
        return driverJoy.getTrackpadButton();
    }

    public static boolean isStoppingAutoControl() {
        return driverJoy.getTrackpadButton();
    }
}