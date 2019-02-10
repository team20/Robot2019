package frc.robot.auto.functions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Drivetrain;

public class DriveTime extends RobotFunction {

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
     */
    @Override
    public void collectInputs(double speedStraight, double howMuchTime) {
        speed = speedStraight;
        time = howMuchTime;
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
        System.out.println("Time Elapsed: " + (Timer.getFPGATimestamp() - startTime));
        if (Math.abs(Timer.getFPGATimestamp() - startTime) < time) {
            Drivetrain.drive(speed, 0.0, 0.0);
        } else {

            Drivetrain.drive(0.0, 0.0, 0.0);
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

    /**
     * Stops the drivetrain
     */
    @Override
    public void stop() {
        Drivetrain.drive(0.0, 0.0, 0.0);
    }

}