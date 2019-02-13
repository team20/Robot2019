package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Intake;

import java.util.InputMismatchException;

public class RunCargo extends RobotFunction<Double> {

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
    public void collectInputs(Double... values) {
        if (values.length != 1) throw new InputMismatchException("RunCargo requires ONE input");

        speed = values[0];
    }

    /**
     * Sets the cargo collector to the desired set speed
     */
    @Override
    public void run() {
        Intake.runCargo(speed);
    }

    @Override
    public void stop() {
        Intake.runCargo(0);
    }

    /**
     * @return is the collector set?
     */
    @Override
    public boolean isFinished() {
        return true;
    }
}