package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

public class Arm {
    public static final CANSparkMax armMotor;
    private static final CANPIDController pidController;
    private static final CANEncoder armEncoder;

    private static double setPosition, prevPosition, zeroPosition;

    private static final double DEADBAND = 0.3;

    public enum Position {
        ARM_FLOOR(-41.38),
        CARGO_SHOOT(-28.9),
        PLACING(-9.5),
        STARTING_CONFIG(-4.0),
        ARM_COLLECT_CARGO(-49.3),
        DROP_AND_COLLECT_HATCH(-40.0),
        CARGO_SHIP_ANGLE(-51.14);

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
        armMotor.setEncPosition(0);
        setPosition(armEncoder.getPosition());
        zeroPosition = 0.0;
        prevPosition = 0.0;

        armMotor.setSmartCurrentLimit(40);

        //sends corresponding values to the pid controller object
        pidController.setP(0.08);
        pidController.setI(0.0);
        pidController.setD(2.0);
        pidController.setOutputRange(-1.0, 1.0);
    }

    /**
     * Sets the value of the elevator
     *
     * @param pos: desired value
     */
    public static void setPosition(double pos) {
        setPosition = pos + zeroPosition;
        pidController.setReference(setPosition, ControlType.kPosition);
    }

    /**
     * Sets the current arm position to the new zero
     */
    public static void resetEncoder() {
//        armMotor.setEncPosition(0);
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
    public static boolean doneMoving() {
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
        setPosition(getPosition());
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
     * @return the value of the arm encoder
     */
    public static double getSetPosition() {
        return setPosition - zeroPosition;
    }

    /**
     * @return the value of the arm encoder
     */
    public static double getPosition() {
        return armEncoder.getPosition();
    }
}