package frc.robot.controls;

import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

import static frc.robot.subsystems.Arm.Position.ARM_FLOOR;
import static frc.robot.subsystems.Arm.Position.CARGO_SHOOT;
import static frc.robot.subsystems.Arm.Position.PLACING;
import static frc.robot.subsystems.Arm.Position.STARTING_CONFIG;
import static frc.robot.subsystems.Elevator.Position.CARGO_LEVEL_ONE;
import static frc.robot.subsystems.Elevator.Position.CARGO_LEVEL_THREE;
import static frc.robot.subsystems.Elevator.Position.CARGO_LEVEL_TWO;
import static frc.robot.subsystems.Elevator.Position.ELEVATOR_FLOOR;
import static frc.robot.subsystems.Elevator.Position.HATCH_LEVEL_ONE;
import static frc.robot.subsystems.Elevator.Position.HATCH_LEVEL_THREE;
import static frc.robot.subsystems.Elevator.Position.HATCH_LEVEL_TWO;

public class OperatorControls {

    private static PS4Controller operatorJoy;
    private static boolean elevatorOverriden, armOverriden;

    /*
     * Initializes the operator controller
     */
    static {
        operatorJoy = new PS4Controller(1, 3);
        elevatorOverriden = false;
        armOverriden = false;
    }

    /**
     * Runs the operator controls
     */
    public static void operatorControls() {
        //Elevator Controls
        //override
        if (operatorJoy.getRightStickButton()) {
            double speed = operatorJoy.getRightYAxis();
            Elevator.moveSpeed(speed);
            elevatorOverriden = true;
        } //positions
        else if (operatorJoy.getLeftYAxis() > 0.1) {
            if (operatorJoy.getButtonDDown()) {
                Elevator.setPosition(CARGO_LEVEL_ONE);
            } else if (operatorJoy.getButtonDLeft()) {
                Elevator.setPosition(CARGO_LEVEL_TWO);
            } else if (operatorJoy.getButtonDUp()) {
                Elevator.setPosition(CARGO_LEVEL_THREE);
            }
        } else if (operatorJoy.getLeftYAxis() < -0.1) {
            if (operatorJoy.getButtonDDown()) {
                Elevator.setPosition(HATCH_LEVEL_ONE);
            } else if (operatorJoy.getButtonDLeft()) {
                Elevator.setPosition(HATCH_LEVEL_TWO);
            } else if (operatorJoy.getButtonDUp()) {
                Elevator.setPosition(HATCH_LEVEL_THREE);
            }
        }
        //encoder reset
        if (operatorJoy.getShareButton()) {
            Elevator.resetEncoder();
        }

        //Arm Controls
        //override
        if (operatorJoy.getLeftStickButton()) {
            double speed = operatorJoy.getLeftYAxis();
            Arm.moveSpeed(speed);
            armOverriden = true;
        } //positions
        else if (operatorJoy.getLeftYAxis() > 0.5) {
            Arm.setPosition(CARGO_SHOOT);
        } else if (operatorJoy.getLeftYAxis() < -0.5) {
            Arm.setPosition(ARM_FLOOR);
        } else if (Math.abs(operatorJoy.getLeftXAxis()) > 0.5) {
            Arm.setPosition(PLACING);
        } else if (operatorJoy.getSquareButton()) {
            Arm.setPosition(STARTING_CONFIG);
        }
        //encoder reset
        if (operatorJoy.getOptionsButton()) {
            Arm.resetEncoder();
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
            Elevator.setPosition(ELEVATOR_FLOOR);
            Arm.setPosition(ARM_FLOOR);
        }
        if (armOverriden) {
            Arm.stop();
            armOverriden = false;
        }
        if (elevatorOverriden) {
            Elevator.stop();
            elevatorOverriden = false;
        }
    }
}