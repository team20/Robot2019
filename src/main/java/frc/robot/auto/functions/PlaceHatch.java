package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Elevator;

/**
 * Opens the hatch grabbing mechanism
 */
public class PlaceHatch extends RobotFunction {
    /**
     * Initializes all needed variables
     */
    public PlaceHatch() {
    }

    @Override
    public void init() {
        Elevator.dropHatch();
    }

    /**
     * Places the hatch
     */
    @Override
    public void run() {
    }

    @Override
    public void stop() {
    }

    /**
     * @return is the Placer set?
     */
    @Override
    public boolean isFinished() {
        return true;
    }
}