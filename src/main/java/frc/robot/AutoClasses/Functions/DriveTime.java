package frc.robot.AutoClasses.Functions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.AutoClasses.Setup.RobotFunction;
import frc.robot.Subsystems.Drivetrain;

public class DriveTime extends RobotFunction {

    private boolean setStartTime, isFinished;
    private double startTime, speed, time;

    public DriveTime(){
        setStartTime = false; isFinished = false;
        startTime = 0; speed = 0; time = 0;
    }

    @Override
    public void collectInputs(double speedStraight, double howMuchTime){
        speed = speedStraight;
        time = howMuchTime;
    }

    @Override
    public void run(){
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

    @Override
    public boolean finished() {
        return isFinished;
    }

    @Override
    public void stop(){
        Drivetrain.drive(0.0, 0.0, 0.0);
    }

}