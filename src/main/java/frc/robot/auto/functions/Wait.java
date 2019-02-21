package frc.robot.auto.functions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.setup.RobotFunction;

import java.util.InputMismatchException;

/**
 * Pauses RocketScript for some amount of time
 * <p>{@code values[0]} is the length of time to wait</p>
 */
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
     * Collects the length of time to wait
     */
    @Override
    public void collectInputs(Double... values) {
        if (values.length != 1) throw new InputMismatchException("Wait requires ONE input");

        time = values[0];
        super.isParallel = false;
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

        if (Math.abs(Timer.getFPGATimestamp() - startTime) > time)
            isFinished = true;
    }

    @Override
    public void stop() {
    }

    /**
     * @return true if the drivetrain is done moving
     */
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}