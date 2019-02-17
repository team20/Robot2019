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

    private static double setPosition, prevPosition;

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

    private Arm() {
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
        setPosition = pos;
        pidController.setReference(setPosition, ControlType.kPosition);
    }

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

}