package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.robot.Robot;
import frc.robot.utils.PrettyPrint;

public class Climber {
    private static CANSparkMax back;
    private static CANEncoder backEnc;
    private static TalonSRX front;
    private static PIDController PID;
    private static double kP = 0.055, kI = 0, kD = 0;
    private static double balancePidOutput;

    private Climber() {
    }

    /*
     * Initializes and sets up all motors and PID Controllers
     */
    static {
        // Declare motors
        back = new CANSparkMax(7, MotorType.kBrushless);
        back.setInverted(true);
        backEnc = new CANEncoder(back);
        front = new TalonSRX(8);
        front.setInverted(true);

        // Declare PID Output
        PIDOutput pidOutput = output -> balancePidOutput = output;

        // Declare PID Source
        GyroSource input = new GyroSource();

        // Declare PID Controller
        PID = new PIDController(kP, kI, kD, input, pidOutput);
        PID.setSetpoint(0);
        PID.setInputRange(-180, 180);
        PID.setOutputRange(-1, 1);

        // Enable
        PID.enable();
    }

    /**
     * Climb with gyro assistance
     *
     * @param speed: the speed at which to climb
     */
    public static void balanceClimb(double speed) {
        front.set(ControlMode.PercentOutput, (balancePidOutput) + speed);
        back.set(.4175*(-balancePidOutput + speed));
        PrettyPrint.put("Front Speed", ((balancePidOutput) + speed));
        PrettyPrint.put("Back Speed", (-balancePidOutput + speed));
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
     * Retracts the climber, front legs first, then back
     * 
     * @param speed: the speed of retraction
     */
    public static void retractClimber(double speed){
        // if (front.getSelectedSensorPosition() < 1.0) {
        //     front.set(ControlMode.PercentOutput, 0.0);
        //     if(backEnc.getPosition() < 1.0){
        //         back.set(0.0);
        //     } else {
        //         back.set(speed);
        //     }
        // } else {
        //     front.set(ControlMode.PercentOutput, -speed);
        // }
            front.set(ControlMode.PercentOutput, speed);
            back.set(0.5*speed);
    }

    /**
     * Retract the front leg
     *
     * @param speed: the speed at which to retract (positive)
     */
    public static void retractFront(double speed) {
        // if (front.getSelectedSensorPosition() < 1.0) {
        //     front.set(ControlMode.PercentOutput, 0.0);
        // } else {
            front.set(ControlMode.PercentOutput, -speed);
//        }
    }

    /**
     * Retract the rear leg
     *
     * @param speed: the speed at which to retract (positive)
     */
    public static void retractBack(double speed) {
        // if (backEnc.getPosition() < 1.0) {
        //     back.set(0.0);
        // } else {
            back.set(-speed);
//        }
    }

    /**
     * @return: value of the front encoder
     */
    public static int getFrontEncPosition(){
        return front.getSelectedSensorPosition();
    }

    /**
     * @return: value fo the back encoder
     */
    public static double getBackEncPosition(){
        return backEnc.getPosition();
    }

    /**
     * Stop all motors
     */
    public static void stop() {
        front.set(ControlMode.PercentOutput, 0.0);
        back.set(0.0);
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