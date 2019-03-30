package frc.robot.controls;

import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

import static frc.robot.subsystems.Arm.Position.*;
import static frc.robot.subsystems.Elevator.Position.*;

public class OperatorControls {

    private static PS4Controller joy;
    private static boolean elevatorOverridden, armOverridden;

    /*
     * Initializes the operator controller
     */
    static {
        joy = new PS4Controller(1, 3);
        elevatorOverridden = false;
        armOverridden = false;
    }

    /**
     * Runs the operator controls
     */
    public static void operatorControls() {
        // Elevator Controls
        // override
        if (joy.getRightStickButton()) {
            double speed = joy.getRightYAxis();
            Elevator.moveSpeed(-speed / 2);
            elevatorOverridden = true;
        } else {
            if (elevatorOverridden) {
                Elevator.stop();
                elevatorOverridden = false;
            }
        }
        // positions
        if (joy.getRightYAxis() > 0.5) {
            if (joy.getButtonDDown()) {
                Elevator.setPosition(HATCH_LEVEL_ONE);
            } else if (joy.getButtonDLeft()) {
                Elevator.setPosition(HATCH_LEVEL_TWO);
            } else if (joy.getButtonDUp()) {
                Elevator.setPosition(HATCH_LEVEL_THREE);
            } else if (joy.getButtonDRight()) {
                Elevator.setPosition(ELEVATOR_COLLECT_HATCH);
            }
        } else if (joy.getRightYAxis() < -0.5) {
            if (joy.getButtonDDown()) {
                Elevator.setPosition(CARGO_LEVEL_ONE);
                Arm.setPosition(ARM_FLOOR);
            } else if (joy.getButtonDLeft()) {
                Elevator.setPosition(CARGO_LEVEL_TWO);
                Arm.setPosition(ARM_FLOOR);
            } else if (joy.getButtonDUp()) {
                Elevator.setPosition(CARGO_LEVEL_THREE);
                Arm.setPosition(CARGO_SHOOT);
            } else if (joy.getButtonDRight()) {
                Elevator.setPosition(CARGO_SHIP);
                Arm.setPosition(CARGO_SHIP_ANGLE);
            }
        }
        // encoder reset
        if (joy.getShareButton()) {
            Elevator.resetEncoder();
        }

        // Arm Controls
        // override
        if (joy.getLeftStickButton()) {
            double speed = -joy.getLeftYAxis();
            Arm.moveSpeed(speed / 2);
            armOverridden = true;
        } else {
            if (armOverridden) {
                Arm.stop();
                armOverridden = false;
            }
        }
        // positions
        if (joy.getLeftYAxis() < -0.5) {
            Arm.setPosition(CARGO_SHOOT);
        } else if (joy.getLeftYAxis() > 0.5) {
            Arm.setPosition(ARM_FLOOR);
        } else if (Math.abs(joy.getLeftXAxis()) > 0.5) {
            Arm.setPosition(PLACING);
        } else if (joy.getSquareButton()) {
            Arm.setPosition(STARTING_CONFIG);
            Elevator.setPosition(ELEVATOR_FLOOR);
        }
        // encoder reset
        if (joy.getOptionsButton()) {
            Arm.resetEncoder();
        }

        // Intake Controls
        // cargo
        if (joy.getXButton()) {
            Intake.intakeMode();
        }
        if (joy.getTriButton()) {
            Intake.outtakeCargo();
        }
        if (joy.getCircleButton()) {
            Intake.stopCargoRollers();
        }
        if (joy.getRightTriggerAxis() > 0.5) {
            Intake.spitCargo();
        }
        // hatch
        if (joy.getLeftBumperButton()) {
            Elevator.placeHatch();
        } else {
            Elevator.setHatchPlace = false;
        }
        if (joy.getRightBumperButton()) {
            Elevator.setPosition(ELEVATOR_FLOOR);
            Arm.setPosition(PLACING);
        }

        // Combined Subsystem Controls
        if (joy.getLeftTriggerAxis() > 0.5) {
            Elevator.setPosition(ELEVATOR_COLLECT_CARGO);
            Arm.setPosition(ARM_COLLECT_CARGO);
        }

        // Controller Vibrations
        if (Intake.intakeRunning()) {
            joy.setRumble(1.0);
        } else {
            joy.setRumble(0.0);
        }
    }

    public static boolean isOverridingAuto() {
        return joy.anythingPressed();
    }

    // TODO determine this button
    public static boolean isStoppingAutoControl() {
        return joy.getTrackpadButton();
    }
}