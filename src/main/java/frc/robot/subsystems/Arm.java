package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

public class Arm {
    private static CANSparkMax armMotor;
    private static CANPIDController pidController;
    private static CANEncoder armEncoder;

    private static double setPosition, prevPosition, zeroPosition;

    private static final double DEADBAND = 0.3;

    public enum Position {
        ARM_FLOOR(0.0),
        CARGO_SHOOT(0.0),
        PLACING(0.0),
        STARTING_CONFIG(0.0);
        
        double value;

        Position(double position) {
            value = position;
        }
    }

    /*
     * Initializes all necessary objects and variables
     */
    static {
        //motor setup
        armMotor = new CANSparkMax(6, MotorType.kBrushless);
        pidController = armMotor.getPIDController();
        armEncoder = new CANEncoder(armMotor);

        //initialize variables
        setPosition = armEncoder.getPosition();
        prevPosition = 0.0;
        zeroPosition = 0.0;

        //sends corresponding values to the pid controller object
        pidController.setP(0.001);
        pidController.setI(0.0);
        pidController.setD(0.0);
        pidController.setOutputRange(-1.0, 1.0);
    }

    /**
     * Sets the value of the elevator
     *
     * @param pos: desired value
     */
    public static void setPosition(double pos) {
        setPosition = zeroPosition + pos;
        pidController.setReference(setPosition, ControlType.kPosition);
    }

    /**
     * Sets the current arm position to the new zero
     */
    public static void resetEncoder(){
        zeroPosition = armEncoder.getPosition();
    }

    /**
     * Sets the value of the elevator
     *
     * @param position: desired value
     */
    public static void setPosition(Position position) {
        setPosition(position.value);
    }

    /**
     * @return true if the arm is within deadband of its set value
     */
    public static boolean armDoneMoving() {
        if (Math.abs(armEncoder.getPosition() - prevPosition) > DEADBAND) {
            prevPosition = armEncoder.getPosition();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Stops the arm from moving
     */
    public static void stop() {
        setPosition(armEncoder.getPosition());
    }

    /**
     * Moves the arm at the desired speed
     * 
     * @param speed: the desired speed
     */
    public static void moveSpeed(double speed) {
        armMotor.set(speed);
    }

    /**
     * @return: the value of the arm encoder
     */
    public static double getPosition() {
        return armEncoder.getPosition();
    }
}