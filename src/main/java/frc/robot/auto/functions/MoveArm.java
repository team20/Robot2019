package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Arm;

/**
 * Moves the arm to a desired position in rotations
 */
public class MoveArm extends RobotFunction {
    private double position;

    /**
     * Initializes all needed variables
     */
    public MoveArm(double position) {
        this.position = position;
    }

    public MoveArm(Arm.Position position) {
        this.position = position.value;
    }

    /**
     * Sets the arm to the desired set position
     */
    @Override
    public void init() {
        Arm.setPosition(position);
    }

    @Override
    public void run() {
    }

    /**
     * @return is the arm finished moving?
     */
    @Override
    public boolean isFinished() {
        return Arm.doneMoving();
    }

    /**
     * Stops the arm
     */
    @Override
    public void stop() {
        Arm.stop();
    }
}