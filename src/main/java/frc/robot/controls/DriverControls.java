package frc.robot.controls;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

public class DriverControls {
    private static PS4Controller driverJoy;
    private static double speedStraight, speedLeft, speedRight;
    private static boolean climberOverride;

    /*
     * Initializes the driver controller
     */
    static {
        driverJoy = new PS4Controller(0, 2);
        speedStraight = 0;
        speedLeft = 0;
        speedRight = 0;
        climberOverride = false;
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
        if (Elevator.aboveStageThreshold()) {
            if (driverJoy.getSquareButton()) {
                speedLeft = driverJoy.getLeftTriggerAxis() * 0.25;
                speedRight = driverJoy.getRightTriggerAxis() * 0.25;
            } else {
                speedLeft = driverJoy.getLeftTriggerAxis() * 0.4;
                speedRight = driverJoy.getRightTriggerAxis() * 0.4;
            }
        } else {
            if (driverJoy.getSquareButton()) {
                speedLeft = driverJoy.getLeftTriggerAxis() * 0.33;
                speedRight = driverJoy.getRightTriggerAxis() * 0.33;
            } else {
                speedLeft = driverJoy.getLeftTriggerAxis() * 0.65;
                speedRight = driverJoy.getRightTriggerAxis() * 0.65;
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
        if (driverJoy.getXButton()) {
            Climber.balanceClimb(0.75);
        } else if(driverJoy.getCircleButton() || driverJoy.getTriButton()){
            if(driverJoy.getCircleButton()){
                Climber.retractFront(0.5);
            }
            if(driverJoy.getTriButton()){
                Climber.retractBack(0.5);
            }
        } else if(driverJoy.getTrackpadButton()){
            climberOverride = true;
            if(driverJoy.getButtonDUp())
                Climber.manualClimbFront(0.5);
            if(driverJoy.getButtonDDown()){
                Climber.manualClimbBack(0.5);
            }
        }
        if(climberOverride){
            Climber.manualClimbFront(0.0);
            Climber.manualClimbBack(0.0);
            climberOverride = false;
        }
    }
}