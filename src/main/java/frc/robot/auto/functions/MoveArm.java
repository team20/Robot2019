package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Arm;

import java.util.InputMismatchException;

/**
 * Moves the arm to a desired position in rotations
 * <p>{@code values[0]} is the position in rotations</p>
 */
public class MoveArm extends RobotFunction<Double> {

    private double position;

    /**
     * Initializes all needed variables
     */
    public MoveArm() {
        position = 0;
    }

    /**
     * Stores the desired position of the arm
     */
    @Override
    public void collectInputs(Double... values) {
        if (values.length != 1) throw new InputMismatchException("MoveArm requires ONE input");
        position = values[0];
    }

    /**
     * Sets the arm to the desired set position
     */
    @Override
    public void run() {
        Arm.setPosition(position);
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