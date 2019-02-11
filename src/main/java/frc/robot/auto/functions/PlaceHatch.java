package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Intake;

public class PlaceHatch extends RobotFunction {

    /**
     * Initializes all needed variables
     */
    public PlaceHatch() {

    }

    /**
     * Places the hatch
     */
    @Override
    public void run() {
        Intake.openHatch();
    }

    /**
     * @return: is the Placeor set?
     */
    @Override
    public boolean finished() {
        return true;
    }
}