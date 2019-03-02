package frc.robot.controls;

import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.utils.PrettyPrint;

import static frc.robot.subsystems.Arm.Position.*;
import static frc.robot.subsystems.Elevator.Position.*;

public class OperatorControls {

    private static PS4Controller operatorJoy;
    private static boolean elevatorOverridden, armOverridden;

    /*
     * Initializes the operator controller
     */
    static {
        operatorJoy = new PS4Controller(1, 3);
        elevatorOverridden = false;
        armOverridden = false;
    }

    /**
     * Runs the operator controls
     */
    public static void operatorControls() {
        PrettyPrint.put("Elevator", Elevator.getPosition());
        PrettyPrint.put("Arm", Arm.getPosition());
        // Elevator Controls
        // override
        if (operatorJoy.getRightStickButton()) {
            double speed = operatorJoy.getRightYAxis();
            Elevator.moveSpeed(-speed);
            elevatorOverridden = true;
        } else {
            if (elevatorOverridden) {
                Elevator.stop();
                elevatorOverridden = false;
            }
        }
        // positions
        if (operatorJoy.getRightYAxis() < 0.1) {
            if (operatorJoy.getButtonDDown()) {
                Elevator.setPosition(CARGO_LEVEL_ONE);
            } else if (operatorJoy.getButtonDLeft()) {
                Elevator.setPosition(CARGO_LEVEL_TWO);
            } else if (operatorJoy.getButtonDUp()) {
                Elevator.setPosition(CARGO_LEVEL_THREE);
            } else if (operatorJoy.getButtonDRight()) {
                Elevator.setPosition(CARGO_SHIP);
            }
        } else if (operatorJoy.getRightYAxis() > -0.1) {
            if (operatorJoy.getButtonDDown()) {
                Elevator.setPosition(HATCH_LEVEL_ONE);
            } else if (operatorJoy.getButtonDLeft()) {
                Elevator.setPosition(HATCH_LEVEL_TWO);
            } else if (operatorJoy.getButtonDUp()) {
                Elevator.setPosition(HATCH_LEVEL_THREE);
            } else if (operatorJoy.getButtonDRight()){
                Elevator.setPosition(ELEVATOR_COLLECT_HATCH);
//                Arm.setPosition(DROP_AND_COLLECT_HATCH);  //enable for top hatch mechanism
            }
        }
        // encoder reset
        if (operatorJoy.getShareButton()) {
            Elevator.resetEncoder();
        }

        // Arm Controls
        // override
        if (operatorJoy.getLeftStickButton()) {
            double speed = -operatorJoy.getLeftYAxis();
            Arm.moveSpeed(speed);
            armOverridden = true;
        } else {
            if (armOverridden) {
                Arm.stop();
                armOverridden = false;
            }
        }
        // positions
        if (operatorJoy.getLeftYAxis() < -0.5) {
            Arm.setPosition(CARGO_SHOOT);
        } else if (operatorJoy.getLeftYAxis() > 0.5) {
            Arm.setPosition(ARM_FLOOR);
        } else if (Math.abs(operatorJoy.getLeftXAxis()) > 0.5) {
            Arm.setPosition(PLACING);
        } else if (operatorJoy.getSquareButton()) {
            Arm.setPosition(STARTING_CONFIG);
            Elevator.setPosition(ELEVATOR_FLOOR);
        }
        // encoder reset
        if (operatorJoy.getOptionsButton()) {
            Arm.resetEncoder();
        }

        // Intake Controls
        // cargo
        if (operatorJoy.getXButton()) {
            Intake.intakeMode(); // TODO enable sensor - not plugged in for initial testing
            // Intake.collectCargo();
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
        // hatch
        if (operatorJoy.getLeftBumperButton()) {
//            Intake.openHatch();
//            Arm.setPosition(DROP_AND_COLLECT_HATCH);
            Elevator.dropHatch();
        }   
        if (operatorJoy.getRightBumperButton()) {
//            Intake.closeHatch();
            Elevator.setPosition(ELEVATOR_FLOOR);
//            Arm.setPosition(ARM_FLOOR);
        }

        // Combined Subsystem Controls
        if (operatorJoy.getLeftTriggerAxis() > 0.5) {
            Elevator.setPosition(ELEVATOR_COLLECT_CARGO);
            Arm.setPosition(ARM_COLLECT_CARGO);
        }

        // Controller Vibrations
        if (Intake.intakeRunning()) {
            operatorJoy.setRumble(1.0);
        } else {
            operatorJoy.setRumble(0.0);
        }
    }

    public static boolean isOverridingAuto() {
        return operatorJoy.getTriButton() || operatorJoy.getSquareButton() || operatorJoy.getCircleButton()
                || operatorJoy.getXButton() ||

                operatorJoy.getPSButton() || operatorJoy.getShareButton() || operatorJoy.getOptionsButton() ||

                operatorJoy.getButtonDDown() || operatorJoy.getButtonDLeft() || operatorJoy.getButtonDRight()
                || operatorJoy.getButtonDUp() ||

                operatorJoy.getLeftBumperButton() || Math.abs(operatorJoy.getLeftTriggerAxis()) > .1 ||

                operatorJoy.getRightBumperButton() || Math.abs(operatorJoy.getRightTriggerAxis()) > .1 ||

                operatorJoy.getLeftStickButton() || Math.abs(operatorJoy.getLeftXAxis()) > .1
                || Math.abs(operatorJoy.getLeftYAxis()) > .1 ||

                operatorJoy.getRightStickButton() || Math.abs(operatorJoy.getRightXAxis()) > .1
                || Math.abs(operatorJoy.getRightYAxis()) > .1;
    }

    // TODO determine this button
    public static boolean isStoppingAutoControl() {
        return operatorJoy.getTrackpadButton();
    }
}