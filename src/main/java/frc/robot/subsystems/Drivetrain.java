package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Drivetrain {

    private static TalonSRX frontRight, frontLeft;
    private static VictorSPX backRight, backLeft;

    /**
     * Initializes and sets up all motors for the drivetrain
     */
    public Drivetrain() {
        frontRight = new TalonSRX(25);
        frontLeft = new TalonSRX(20);

        backRight = new VictorSPX(15);
        backLeft = new VictorSPX(10);

        backRight.follow(frontRight);
        backLeft.follow(backLeft);

        frontRight.setInverted(false);
        frontLeft.setInverted(false);

        backRight.setInverted(false);
        backLeft.setInverted(false);
    }

    /**
     * makes the drive train move in arcade drive
     *
     * @param speed:     straight axis value
     * @param rightTurn: right axis value
     * @param leftTurn:  left axis value
     */
    public static void drive(double speed, double rightTurn, double leftTurn) {
        frontRight.set(ControlMode.PercentOutput, (speed - rightTurn + leftTurn) * 0.75);
        frontLeft.set(ControlMode.PercentOutput, (-speed + leftTurn - rightTurn));
    }
}