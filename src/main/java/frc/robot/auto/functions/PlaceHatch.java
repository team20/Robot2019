package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Intake;

public class PlaceHatch extends RobotFunction<Void> {

    /**
     * Initializes all needed variables
     */
    public PlaceHatch() {

    }

    @Override
    public void collectInputs(Void... values) {
    }

    /**
     * Places the hatch
     */
    @Override
    public void run() {
        Intake.openHatch();
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