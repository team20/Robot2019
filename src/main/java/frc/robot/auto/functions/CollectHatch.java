package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Elevator;

import static frc.robot.subsystems.Elevator.Position.ELEVATOR_COLLECT_HATCH;

/**
 * Closes hatch grabbing mechanism
 */
public class CollectHatch extends RobotFunction {

    /**
     * Initializes all needed variables
     */
    public CollectHatch() {
    }

    @Override
    public void init() {
        Elevator.setPosition(ELEVATOR_COLLECT_HATCH);
    }

    /**
     * Collects the hatch
     */
    @Override
    public void run() {
    }

    @Override
    public void stop() {
    }

    /**
     * @return is the collector set?
     */
    @Override
    public boolean isFinished() {
        return true;
    }

}