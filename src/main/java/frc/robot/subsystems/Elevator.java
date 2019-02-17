package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;

public class Elevator {
    private static CANSparkMax elevator;
    private static CANEncoder elevatorEncoder;

    private static double setPosition, prevPosition;

    private static final double STAGE_THRESHOLD = 0.0;
    private static final double MAX_POSITION = 0.0;
    private static final double DEADBAND = 0.5;

    public enum Position {
        ELEVATOR_FLOOR(0.0),
        HATCH_LEVEL_ONE(0.0),
        HATCH_LEVEL_TWO(0.0),
        HATCH_LEVEL_THREE(0.0),
        CARGO_LEVEL_ONE(0.0),
        CARGO_LEVEL_TWO(0.0),
        CARGO_LEVEL_THREE(0.0),
        CARGO_SHIP(0.0);

        double value;

        Position(double position) {
            value = position;
        }
    }

    private Elevator() {
    }

    /*
     * Initializes the elevator motor, sets PID values, and zeros the elevator encoder
     */
    static {
        elevator = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
        elevator.setInverted(false);
        elevator.getPIDController().setOutputRange(-1.0, 1.0);
        setPID(0.075, 0.000015, 1.1, 0.0);

        elevatorEncoder = new CANEncoder(elevator);

        setPosition = elevatorEncoder.getPosition();
        prevPosition = 0.0;
    }

    /**
     * inserts the p i d f values into the Talon SRX
     *
     * @param p: proportional value
     * @param i: integral value
     * @param d: derivative value
     * @param f: feed forward value
     */
    public static void setPID(double p, double i, double d, double f) {
        elevator.getPIDController().setP(p);
        elevator.getPIDController().setI(i);
        elevator.getPIDController().setD(d);
        elevator.getPIDController().setFF(f);
    }

//    /**
//     * Sets the elevator to the floor value
//     */
//    public static void setFloor() {
//        setPosition(Position.FLOOR_POSITION);
//    }
//
//    /**
//     * Sets the elevator to the hatch one value
//     */
//    public static void setHatchLevelOne() {
//        setPosition(Position.HATCH_LEVEL_ONE_POSITION);
//    }
//
//    /**
//     * Sets the elevator to the hatch two value
//     */
//    public static void setHatchLevelTwo() {
//        setPosition(Position.HATCH_LEVEL_TWO_POSITION);
//    }
//
//    /**
//     * Sets the elevator to the hatch three value
//     */
//    public static void setHatchLevelThree() {
//        setPosition(Position.HATCH_LEVEL_THREE_POSITION);
//    }
//
//    /**
//     * Sets the elevator to the cargo one value
//     */
//    public static void setCargoLevelOne() {
//        setPosition(Position.CARGO_LEVEL_ONE_POSITION);
//    }
//
//    /**
//     * Sets the elevator to the cargo two value
//     */
//    public static void setCargoLevelTwo() {
//        setPosition(Position.CARGO_LEVEL_TWO_POSITION);
//    }
//
//    /**
//     * Sets the elevator to the cargo three value
//     */
//    public static void setCargoLevelThree() {
//        setPosition(Position.CARGO_LEVEL_THREE_POSITION);
//    }
//
//    /**
//     * Sets the elevator to the cargo ship value
//     */
//    public static void setCargoShip() {
//        setPosition(Position.CARGO_SHIP_POSITION);
//    }

    /**
     * sets the elevator set value to its current value
     */
    public static void stop() {
        setPosition(elevatorEncoder.getPosition());
    }

    /**
     * @return the set point of the elevator
     */
    public static double getPosition() {
        return setPosition;
    }

    public static boolean aboveStageThreshold() {
        return elevatorEncoder.getPosition() > STAGE_THRESHOLD;
    }

    /**
     * @return true if the elevator is within deadband of its set value
     */
    public static boolean elevatorDoneMoving() {
        if (Math.abs(elevatorEncoder.getPosition() - prevPosition) > DEADBAND) {
            prevPosition = elevatorEncoder.getPosition();
            return true;
        } else {
            return false;
        }
    }

    /**
     * moves the elevator at a speed (percent output)
     *
     * @param speed: speed of the elevator (-1.0 to 1.0)
     */
    public static void moveSpeed(double speed) {
        elevator.set(speed);
    }

    /**
     * Sets the elevator to the entered value
     *
     * @param position: desired elevator value
     */
    public static void setPosition(double position) {
        setPosition = position;
        limitPosition();
    }

    /**
     * Sets the elevator to the entered position
     *
     * @param position desired elevator position
     */
    public static void setPosition(Position position) {
        setPosition(position.value);
    }

    /**
     * Prevents the user from going past the maximum value of the elevator
     */
    private static void limitPosition() {
        if (setPosition > MAX_POSITION) {
            setPosition = MAX_POSITION;
            elevator.getPIDController().setReference(setPosition, ControlType.kPosition);
        } else {
            elevator.getPIDController().setReference(setPosition, ControlType.kPosition);
        }
    }
}