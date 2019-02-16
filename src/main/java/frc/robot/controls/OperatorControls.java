package frc.robot.controls;

import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

public class OperatorControls {

    private PS4Controller operatorJoy;

    /**
     * Initializes the operator controller
     */
    public OperatorControls() {
        operatorJoy = new PS4Controller(1, 3);
    }

    /**
     * Runs the operator controls
     */
    public void operatorControls() {
        //Elevator Controls
        //override
        if (operatorJoy.getTrackpadButton()) {
            double speed = operatorJoy.getRightYAxis();
            Elevator.moveSpeed(speed);
        } //positions
        else if (operatorJoy.getLeftYAxis() > 0.1) {
            if (operatorJoy.getButtonDDown()) {
                Elevator.setCargoLevelOne();
            } else if (operatorJoy.getButtonDLeft()) {
                Elevator.setCargoLevelTwo();
            } else if (operatorJoy.getButtonDUp()) {
                Elevator.setCargoLevelThree();
            }
        } else if (operatorJoy.getLeftYAxis() < -0.1) {
            if (operatorJoy.getButtonDDown()) {
                Elevator.setHatchLevelOne();
            } else if (operatorJoy.getButtonDLeft()) {
                Elevator.setHatchLevelTwo();
            } else if (operatorJoy.getButtonDUp()) {
                Elevator.setHatchLevelThree();
            }
        }

        //Arm Controls
        if (operatorJoy.getLeftYAxis() > 0.5) {
            Arm.setCargoShootPosition();
        } else if (operatorJoy.getLeftYAxis() < -0.5) {
            Arm.setFloorPosition();
        } else if (Math.abs(operatorJoy.getLeftXAxis()) > 0.5) {
            Arm.setPlacePosition();
        }
        if (operatorJoy.getSquareButton()) {
            Arm.setStartingConfigPosition();
        }

        //Intake Controls
        //cargo
        if (operatorJoy.getXButton()) {
            Intake.intakeMode();
        }
        if (operatorJoy.getTriButton()) {
            Intake.outtakeCargo();
        }
        if (operatorJoy.getCircleButton()) {
            Intake.stopCargoRollers();
        }
        if (operatorJoy.getRightTriggerAxis() > 0.5) {
            Intake.spitCargo();
        }
        //hatch
        if (operatorJoy.getLeftBumperButton()) {
            Intake.openHatch();
        }
        if (operatorJoy.getRightBumperButton()) {
            Intake.closeHatch();
        }

        //Combined Subsystem Controls
        if (operatorJoy.getLeftTriggerAxis() > 0.5) {
            Elevator.setFloor();
            Arm.setFloorPosition();
        }
    }
}