package frc.robot.auto.functions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.PrettyPrint;

import java.util.InputMismatchException;

/**
 * Drives straight for some amount of time
 * <p>{@code values[0]} is percent output</p>
 * <p>{@code values[1} is time</p>
 */
public class DriveTime extends RobotFunction<Double> {

    private boolean setStartTime, isFinished;
    private double startTime, speed, time;

    /**
     * Initializes all necessary variables
     */
    public DriveTime() {
        setStartTime = false;
        isFinished = false;
        startTime = 0;
        speed = 0;
        time = 0;
    }

    /**
     * Collects the speed and length of time the robot should move for
     *
     * @param values nums[0] is speed, nums[1] is time
     */
    @Override
    public void collectInputs(Double... values) {
        if (values.length != 2) throw new InputMismatchException("DriveTime requires TWO inputs");

        speed = values[0];
        time = values[1];
    }

    /**
     * Runs the drivetrain at the desired speed for the entered amount of time
     */
    @Override
    public void run() {
        if (!setStartTime) {
            startTime = Timer.getFPGATimestamp();
            setStartTime = true;
        }
        PrettyPrint.put("DriveTime Elapsed", Timer.getFPGATimestamp() - startTime);
        if (Math.abs(Timer.getFPGATimestamp() - startTime) < time) {
            Drivetrain.drive(speed);
        } else {
            Drivetrain.drive(0.0);
            isFinished = true;
        }
    }

    /**
     * @return returns true if the drivetrain is done moving
     */
    @Override
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Stops the drivetrain
     */
    @Override
    public void stop() {
        Drivetrain.drive(0.0);
    }

}