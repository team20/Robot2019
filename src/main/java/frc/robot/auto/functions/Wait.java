package frc.robot.auto.functions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.setup.RobotFunction;

/**
 * Pauses RocketScript for some amount of time
 */
public class Wait extends RobotFunction {
    private double startTime, time;

    /**
     * Initializes all necessary variables
     */
    public Wait(double time) {
        this.time = time;
    }

    @Override
    public void init() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void run() {
    }

    @Override
    public void stop() {
    }

    /**
     * @return true if the time is up
     */
    @Override
    public boolean isFinished() {
        return Math.abs(Timer.getFPGATimestamp() - startTime) > time;
    }
}