package frc.robot.subsystems;

import frc.robot.Robot;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Climber {
    private static CANSparkMax back;
    private static TalonSRX front;
    private static PIDController frontC, backC;
    private static double kP = .08, kI = 0, kD = 0;

    /**
     * Initializes and sets up all motors and PID Controllers
     */
    public Climber() {
        // Declare motors
        back = new CANSparkMax(0, MotorType.kBrushless);
        front = new TalonSRX(0);

        // Declare PID Output
        PIDOut output = new PIDOut();

        // Declare PID Source
        GyroSource input = new GyroSource();

        // Declare PID Controllers
        frontC = new PIDController(kP, kI, kD, input, output);
        backC = new PIDController(kP, kI, kD, input, output);
        frontC.setSetpoint(0);
        frontC.setInputRange(-180, 180);
        frontC.setOutputRange(-1, 1);
        backC.setSetpoint(0);
        backC.setInputRange(-180, 180);
        backC.setOutputRange(-1, 1);

        // Enable
        frontC.enable();
        backC.enable();
    }

    /**
     * Climb with gyro assistance
     * 
     * @param speed: the speed at which to climb
     */
    public static void balanceClimb(double speed) {
        front.set(ControlMode.PercentOutput, PIDOut.output + speed);
        back.set(-PIDOut.output + speed);
    }

    /**
     * Climb without gyro assistance
     * 
     * @param speed: the speed at which to climb
     */
    public static void manualClimb(double speed) {
        front.set(ControlMode.PercentOutput, speed);
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

        public GyroSource() {
        }

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

    static class PIDOut implements PIDOutput {
        public static double output = 0;

        public PIDOut() {
        }

        @Override
        public void pidWrite(double output) {
            PIDOut.output = output;
        }
    }
}