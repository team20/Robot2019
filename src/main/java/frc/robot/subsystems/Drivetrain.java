package frc.robot.subsystems;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
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

        //config motion profile values
        frontLeft.configMotionProfileTrajectoryPeriod(20);
        frontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 1000);
        frontLeft.setStatusFramePeriod(StatusFrameEnhanced.Status_9_MotProfBuffer, 20);
        frontLeft.setStatusFramePeriod(StatusFrame.Status_10_Targets, 20);
        frontLeft.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20);
        frontLeft.setStatusFramePeriod(StatusFrame.Status_17_Targets1, 20);

        frontRight.configMotionProfileTrajectoryPeriod(20);
        frontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 1000);
        frontRight.setStatusFramePeriod(StatusFrameEnhanced.Status_9_MotProfBuffer, 20);
        frontRight.setStatusFramePeriod(StatusFrame.Status_10_Targets, 20);
        frontRight.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20);
        frontRight.setStatusFramePeriod(StatusFrame.Status_17_Targets1, 20);

        // pid constants for motion profile
        double kP = 0.2;
        double kI = 0.0;
        double kD = 0.0;
        double kF = 0.0;
        frontLeft.config_kP(0, kP);
        frontLeft.config_kI(0, kI);
        frontLeft.config_kD(0, kD);
        frontLeft.config_kF(0, kF);

        frontLeft.config_kP(0, kP);
        frontLeft.config_kI(0, kI);
        frontLeft.config_kD(0, kD);
        frontLeft.config_kF(0, kF);
    }

    public static TalonSRX frontLeft() {
        return frontLeft;
    }

    public static TalonSRX frontRight() {
        return frontRight;
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
     * makes the drive train follow whatever motion profile it has stored in it
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