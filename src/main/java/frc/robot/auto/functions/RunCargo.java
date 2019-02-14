package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Intake;

public class RunCargo extends RobotFunction<Void> {

    /**
     * Initializes all needed variables
     */
    public RunCargo() {

    }

    /**
     * Stores the desired speed of the cargo collector
     */
    @Override
    public void collectInputs(Void...values) {
    }

    /**
     * Sets the cargo collector to the desired set speed
     */
    @Override
    public void run() {
        Intake.collectCargo();
    }

    @Override
    public void stop() {
        Intake.stopCargoRollers();
    }

    /**
     * @return is the collector set?
     */
    @Override
    public boolean isFinished() {
        return true;
    }
}