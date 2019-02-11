package frc.robot.auto.functions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.setup.RobotFunction;

import java.util.InputMismatchException;

public class Wait extends RobotFunction<Double> {

    private boolean setStartTime, isFinished;
    private double startTime, time;

    /**
     * Initializes all necessary variables
     */
    public Wait() {
        setStartTime = false;
        isFinished = false;
        startTime = 0;
        time = 0;
    }

    /**
     * Collects the length of time of the wait
     */
    @Override
    public void collectInputs(Double... values) {
        if (values.length != 1) throw new InputMismatchException("Wait requires ONE input");

        time = values[0];
    }

    /**
     * Waits the entered amount of time
     */
    @Override
    public void run() {
        if (!setStartTime) {
            startTime = Timer.getFPGATimestamp();
            setStartTime = true;
        }
        if (Math.abs(Timer.getFPGATimestamp() - startTime) > time) {
            isFinished = true;
        }
    }

    @Override
    public void stop() {
    }

    /**
     * @return returns true if the drivetrain is done moving
     */
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}