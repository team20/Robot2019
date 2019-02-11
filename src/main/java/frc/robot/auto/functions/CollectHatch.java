package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Intake;

public class CollectHatch extends RobotFunction {

    /**
     * Initializes all needed variables
     */
    public CollectHatch() {

    }

    /**
     * Collects the hatch
     */
    @Override
    public void run() {
        Intake.closeHatch();
    }

    /**
     * @return: is the collector set?
     */
    @Override
    public boolean finished() {
        return true;
    }
}