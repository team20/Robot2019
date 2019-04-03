package frc.robot.auto;

import frc.robot.auto.functions.Align;
import frc.robot.auto.functions.DriveTime;
import frc.robot.auto.functions.MoveArm;
import frc.robot.auto.functions.MoveElevator;
import frc.robot.auto.functions.TeleopControls;
import frc.robot.auto.setup.RobotFunction;
import frc.robot.auto.setup.RocketScript;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

import static frc.robot.subsystems.Arm.Position.CARGO_SHIP_ANGLE;
import static frc.robot.subsystems.Arm.Position.STARTING_CONFIG;
import static frc.robot.subsystems.Elevator.Position.CARGO_SHIP;
import static frc.robot.subsystems.Elevator.Position.ELEVATOR_FLOOR;

public class AutoModes {
    private RocketScript rocketScript;

    public enum Mode {
        CrossLine,
        Align,
        FullyTeleop
    }

    private Mode selectedMode;

    /**
     * Initializes RocketScript
     */
    public AutoModes() {
        rocketScript = new RocketScript();
        selectedMode = null;
    }

    /**
     * Gets selected auto mode
     */
    public Mode getMode() {
        return selectedMode;
    }

    /**
     * Sets selected auto mode
     */
    public void setMode(Mode m) {
        selectedMode = m;
    }

    /**
     * Autonomous: crosses the HAB line
     */
    public void crossLine() {
        rocketScript.addFunction(new DriveTime(0.5, 2.0));
    }

    /**
     * Align to target (hatch on rocket/cargo ship or loading station)
     *
     * @param skip true if turning towards alignment tape should be skipped
     */
    public void align(boolean skip) {
        rocketScript.addFunction(new Align(skip));
    }

    /**
     * No auto - just driver and operator controls the whole time
     */
    public void fullyTeleop() {
        rocketScript.addFunction(new TeleopControls(true));
    }

    /**
     * Drive to cargo ship and place cargo
     * just a demonstration of the making a RobotFunction functionally
     */
    public void frontCargoShip() {
        rocketScript.addFunction(
                RobotFunction.run(() -> Drivetrain.drive(.5))
                        .onStart(Drivetrain::resetEncoders)
                        .doneWhen(() -> Drivetrain.getEncoderPosition() > 2000)
                        .onFinish(Drivetrain::resetEncoders)
                        .build()
        );
        rocketScript.addFunction(new MoveArm(CARGO_SHIP_ANGLE));
        rocketScript.addInParallel(new MoveElevator(CARGO_SHIP));
        rocketScript.addFunction(
                RobotFunction.run(Intake::spitCargo)
                        .doneWhen(Intake::cargoNotPresent)
                        .onFinish(Intake::stopCargoRollers)
                        .build()
        );
        rocketScript.addFunction(new MoveArm(STARTING_CONFIG));
        rocketScript.addInParallel(new MoveElevator(ELEVATOR_FLOOR));
    }

    /**
     * Runs the set autonomous - must be run in autonomous periodic
     */
    public void runAuto() {
        rocketScript.run();
    }
}