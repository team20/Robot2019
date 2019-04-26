package frc.robot.controls;

import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

import static frc.robot.subsystems.Arm.Position.*;
import static frc.robot.subsystems.Elevator.Position.*;

public class OperatorControls extends PS4Controller {
    public static boolean elevatorOverridden, armOverridden, intakeing;

    private static OperatorControls singletonInstance = new OperatorControls(1, 3);

    /**
     * Initializes the controller
     *
     * @param portMain   :   main port of the controller
     * @param portRumble : port with external PS4 drivers
     */
    private OperatorControls(int portMain, int portRumble) {
        super(portMain, portRumble);
    }

    public static void operatorControls() {
        singletonInstance.controls();
    }

    /**
     * Runs the operator controls
     */
    private void controls() {
        // Elevator Controls
        // override
        if (getRightStickButton()) {
            double speed = getRightYAxis();
            Elevator.moveSpeed(-speed);//divide by 2 normal override
            elevatorOverridden = true;
        } else {
            if (elevatorOverridden) {
                Elevator.stop(); // was changed if bad change it back
                elevatorOverridden = false;
            }
        }
        // positions
        if (getRightYAxis() > 0.5) {
            if (getButtonDDown()) {
                Elevator.setPosition(HATCH_LEVEL_ONE);
            } else if (getButtonDLeft()) {
                Elevator.setPosition(HATCH_LEVEL_TWO);
            } else if (getButtonDUp()) {
                Elevator.setPosition(HATCH_LEVEL_THREE);
            } else if (getButtonDRight()) {
                Elevator.setPosition(ELEVATOR_COLLECT_HATCH);
            }
        } else if (getRightYAxis() < -0.5) {
            if (getButtonDDown()) {
                Elevator.setPosition(CARGO_LEVEL_ONE);
                Arm.setPosition(ARM_FLOOR);
            } else if (getButtonDLeft()) {
                Elevator.setPosition(CARGO_LEVEL_TWO);
                Arm.setPosition(ARM_FLOOR);
            } else if (getButtonDUp()) {
                Elevator.setPosition(CARGO_LEVEL_THREE);
                Arm.setPosition(CARGO_SHOOT);
            } else if (getButtonDRight()) {
                Elevator.setPosition(CARGO_SHIP);
                Arm.setPosition(CARGO_SHIP_ANGLE);
            }
        }
        // encoder reset
        if (getShareButton()) {
            Elevator.resetEncoder();
        }

        // Arm Controls
        // override
        if (getLeftStickButton()) {
            double speed = -getLeftYAxis();
            Arm.moveSpeed(speed / 2);
            armOverridden = true;
        } else {
            if (armOverridden) {
                Arm.stop();
                armOverridden = false;
            }
        }
        // positions
        if (!armOverridden) {
            if (getLeftYAxis() < -0.5) {
                Arm.setPosition(CARGO_SHOOT);
            } else if (getLeftYAxis() > 0.5) {
                Arm.setPosition(ARM_FLOOR);
            } else if (Math.abs(getLeftXAxis()) > 0.5) {
                Arm.setPosition(PLACING);
            } else if (getSquareButton()) {
                Arm.setPosition(STARTING_CONFIG);
                Elevator.setPosition(ELEVATOR_FLOOR);
            }
        }
        // encoder reset
        if (getOptionsButton()) {
            Arm.resetEncoder();
        }

        // Intake Controls
        // cargo
        if (getXButton()) {
            Intake.intakeMode();
            intakeing = true;
        } else if (getCircleButton()) {
            Intake.collectHatch();
            intakeing = true;
        } else if (getTriButton()) {
            Intake.outtakeCargo();
            intakeing = true;
        } else if (getRightTriggerAxis() > 0.5) {
            Intake.spitCargo();
            intakeing = true;
        } else {
            intakeing = false;
        }
            /*else {
            Intake.stopCargoRollers();
        }
*/
        // hatch
//        if (getLeftBumperButton()) {
//            Elevator.placeHatch();
//        } else {
//            Elevator.setHatchPlace = false;
//        }
        if (getRightBumperButton()) {
            Elevator.setPosition(ELEVATOR_FLOOR);
            Arm.setPosition(PLACING);
        }

        // Combined Subsystem Controls
        if (getLeftTriggerAxis() > 0.5) {
            Elevator.setPosition(ELEVATOR_COLLECT_CARGO);
            Arm.setPosition(ARM_COLLECT_CARGO);
        }

        // Controller Vibrations
        if (Intake.intakeRunning()) {
            setRumble(1.0);
        } else {
            setRumble(0.0);
        }
    }

    public static boolean isOverridingAuto() {
        return singletonInstance.anythingPressed();
    }

    public static boolean isStoppingAutoControl() {
        return singletonInstance.getTrackpadButton();
    }
}