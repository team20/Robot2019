package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Intake;

public class CollectHatch extends RobotFunction<Void> {

    /**
     * Initializes all needed variables
     */
    public CollectHatch() {

    }

    @Override
    public void collectInputs(Void... values) {
    }

    /**
     * Collects the hatch
     */
    @Override
    public void run() {
        Intake.closeHatch();
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