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

    /*
     * Initializes all necessary objects and variables
     */
    static {
        //motor setup
        armMotor = new CANSparkMax(6, MotorType.kBrushless);
        armMotor.enableVoltageCompensation(13.0); //TODO Sydney made this 13 (not 12) because it is slower when charged and this could be why?
        pidController = armMotor.getPIDController();
        armEncoder = new CANEncoder(armMotor);

        //initialize variables
        armMotor.setEncPosition(0);
        setPosition(armEncoder.getPosition());
        zeroPosition = 0.0;
        prevPosition = 0.0;

        armMotor.setSmartCurrentLimit(40);

        //sends corresponding values to the pid controller object
        pidController.setP(0.07); // was .08
        pidController.setI(0.0);
        pidController.setD(2.0);
        pidController.setOutputRange(-1.0, 1.0);
    }

    public enum Position {
        ARM_FLOOR(-41.38), //Straight Vertical- Cargo L1, L2
        CARGO_SHOOT(-26.0), //28.9 Orig - L3 Cargo
        PLACING(-9.0), //Vertical - Hatches
        STARTING_CONFIG(-1.0), //Defense Position
        ARM_COLLECT_CARGO(-50.0), //Collection
        CARGO_SHIP_ANGLE(-51.14); //Cargo Ship

        public double value;

        Position(double position) {
            value = position;
        }
    }

    /**
     * @return true if the arm is within deadband of its set value
     */
    public static boolean atSetPosition() {
        return Math.abs(armEncoder.getPosition() - setPosition) > DEADBAND;
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