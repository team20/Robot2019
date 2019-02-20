package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Elevator;

import java.util.InputMismatchException;

/**
 * Moves the elevator to a position
 */
public class MoveElevator extends RobotFunction<Integer> {
    private int position;

    /**
     * Initializes all needed variables
     */
    public MoveElevator() {
        position = 0;
    }

    /**
     * Stores the desired position of the elevator
     */
    @Override
    public void collectInputs(Integer... values) {
        if (values.length != 1) throw new InputMismatchException("MoveElevator requires ONE input");

        position = values[0];
    }

    /**
     * Sets the elevator to the desired set position
     */
    @Override
    public void run() {
        Elevator.setPosition(position);
    }

    /**
     * @return is the elevator isFinished moving?
     */
    @Override
    public boolean isFinished() {
        return !Elevator.isMoving();
    }

    /**
     * Stops the elevator
     */
    @Override
    public void stop() {
        Elevator.stop();
    }
}