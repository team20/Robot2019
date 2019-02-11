package frc.robot.auto.functions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.setup.RobotFunction;

public class Wait extends RobotFunction {

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
    public void collectInputs(double howMuchTime) {
        time = howMuchTime;
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

    /**
     * @return: returns true if the drivetrain is done moving
     */
    @Override
    public boolean finished() {
        return isFinished;
    }
}