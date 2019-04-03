package frc.robot.auto.functions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.PrettyPrint;

/**
 * Drives straight for some amount of time
 * <p>{@code values[0]} is percent output</p>
 * <p>{@code values[1} is time</p>
 */
public class DriveTime extends RobotFunction {

    private double startTime, speed, time;

    /**
     * Initializes all necessary variables
     */
    public DriveTime(double speed, double time) {
        this.speed = speed;
        this.time = time;
    }

    @Override
    public void init() {
        startTime = Timer.getFPGATimestamp();
    }

    /**
     * Runs the drivetrain at the desired speed for the entered amount of time
     */
    @Override
    public void run() {
        PrettyPrint.put("DriveTime Elapsed", Timer.getFPGATimestamp() - startTime);
        Drivetrain.drive(speed);
    }

    /**
     * @return returns true if the drivetrain is done moving
     */
    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime > time;
    }

    /**
     * Stops the drivetrain
     */
    @Override
    public void stop() {
        Drivetrain.drive(0.0);
    }

}