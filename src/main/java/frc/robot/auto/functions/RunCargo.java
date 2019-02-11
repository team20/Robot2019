package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Intake;

public class RunCargo extends RobotFunction {

    private double speed;

    /**
     * Initializes all needed variables
     */
    public RunCargo() {
        speed = 0;
    }

    /**
     * Stores the desired speed of the cargo collector
     */
    @Override
    public void collectInputs(double s) {
        speed = s;
    }

    /**
     * Sets the cargo collector to the desired set speed
     */
    @Override
    public void run() {
        Intake.runCargo(speed);
    }

    /**
     * @return: is the collector set?
     */
    @Override
    public boolean finished() {
        return true;
    }
}