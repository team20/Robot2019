package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Elevator;

/**
 * Moves the elevator to a position
 * <p>{@code values[0]} is the position of the elevator in ticks</p>
 */
public class MoveElevator extends RobotFunction {
    private double position;

    /**
     * Initializes all needed variables
     */
    public MoveElevator(int position) {
        this.position = position;
    }

    public MoveElevator(Elevator.Position position) {
        this.position = position.value;
    }

    @Override
    public void init() {
        Elevator.setPosition(position);
    }

    /**
     * Sets the elevator to the desired set position
     */
    @Override
    public void run() {
    }

    /**
     * @return is the elevator isFinished moving?
     */
    @Override
    public boolean isFinished() {
        return Elevator.atSetPosition();
    }

    /**
     * Stops the elevator
     */
    @Override
    public void stop() {
    }
}