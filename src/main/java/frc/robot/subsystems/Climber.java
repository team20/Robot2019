package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.robot.Robot;

public class Climber {
    private static CANSparkMax back;
    private static TalonSRX front;
    private static PIDController frontPID, backPID;
    private static double kP = .08, kI = 0, kD = 0;
    private static double balancePidOutput;

    private Climber() {
    }

    /*
     * Initializes and sets up all motors and PID Controllers
     */
    static {
        // Declare motors
        back = new CANSparkMax(7, MotorType.kBrushless);
        front = new TalonSRX(8);

        // Declare PID Output
        PIDOutput pidOutput = output -> balancePidOutput = output;

        // Declare PID Source
        GyroSource input = new GyroSource();

        // Declare PID Controllers
        frontPID = new PIDController(kP, kI, kD, input, pidOutput);
        backPID = new PIDController(kP, kI, kD, input, pidOutput);
        frontPID.setSetpoint(0);
        frontPID.setInputRange(-180, 180);
        frontPID.setOutputRange(-1, 1);
        backPID.setSetpoint(0);
        backPID.setInputRange(-180, 180);
        backPID.setOutputRange(-1, 1);

        // Enable
        frontPID.enable();
        backPID.enable();
    }

    /**
     * Climb with gyro assistance
     *
     * @param speed: the speed at which to climb
     */
    public static void balanceClimb(double speed) {
        front.set(ControlMode.PercentOutput, balancePidOutput + speed);
        back.set(-balancePidOutput + speed);
    }

    /**
     * Climb without gyro assistance
     *
     * @param speed: the speed at which to climb
     */
    public static void manualClimbFront(double speed) {
        front.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Climb without gyro assistance
     *
     * @param speed: the speed at which to climb
     */
    public static void manualClimbBack(double speed) {
        back.set(speed);
    }

    /**
     * Retract the front leg
     *
     * @param speed: the speed at which to retract (positive)
     */
    public static void retractFront(double speed) {
        front.set(ControlMode.PercentOutput, -speed);
    }

    /**
     * Retract the rear leg
     *
     * @param speed: the speed at which to retract (positive)
     */
    public static void retractBack(double speed) {
        back.set(-speed);
    }

    /**
     * Stop all motors
     */
    public static void stop() {
        front.set(ControlMode.PercentOutput, 0);
        back.set(0);
    }

    static class GyroSource implements PIDSource {
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return PIDSourceType.kDisplacement;
        }

        @Override
        public double pidGet() {
            return Robot.gyro.getPitch();
        }
    }
}