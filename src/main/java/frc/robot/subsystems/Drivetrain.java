package frc.robot.subsystems;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Drivetrain {
    public final static TalonSRX frontRight, frontLeft;
    private static VictorSPX backRight, backLeft;

    private Drivetrain() {
    }

    /*
     * Initializes and sets up all motors for the drivetrain
     */
    static {
        frontRight = new TalonSRX(1);
        frontLeft = new TalonSRX(2);

        backRight = new VictorSPX(3);
        backLeft = new VictorSPX(4);

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

    /**
     * if {@code enabled} is true,
     * makes the drive train follow whatever motion profile it has stored in it
     * otherwise, the drivetrain keeps targeting whatever the its last point was
     */
    public static void motionProfile(boolean enabled) {
        if (enabled) {
            frontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
            frontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
        } else {
            frontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
            frontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
        }
    }
}