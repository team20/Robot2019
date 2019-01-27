package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Drivetrain {

    static TalonSRX frontRight, backRight, frontLeft, backLeft;


    public Drivetrain(){
        frontRight = new TalonSRX(25);
        backRight = new TalonSRX(15);
        frontLeft = new TalonSRX(20);
        backLeft = new TalonSRX(10);

        backRight.follow(frontRight);
        backLeft.follow(backLeft);

        frontRight.setInverted(false);
        backRight.setInverted(false);
        frontLeft.setInverted(false);
        backLeft.setInverted(false);
    }

    /**
	 * makes the drive train move in arcade drive
	 * @param speed: straight axis value
	 * @param rightTurn: right axis value
	 * @param leftTurn: left axis value
	 */
	public static void drive(double speed, double rightTurn, double leftTurn) {
		frontRight.set(ControlMode.PercentOutput, (speed - rightTurn + leftTurn)*0.75);
		frontLeft.set(ControlMode.PercentOutput, (-speed + leftTurn - rightTurn));
    }
}