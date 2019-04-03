package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController.AccelStrategy;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

public class Elevator {
    public static final CANSparkMax elevator;
    private static final CANEncoder elevatorEncoder;

    private static double setPosition, prevPosition, zeroPosition;
    private static double prevVelocity, currentVelocity;
    private static double currentAcceleration;

    private static final double STAGE_THRESHOLD = 30.0;
    public static final double MAX_POSITION = 47.5;
    private static final double DEADBAND = 0.5;
    private static final double HATCH_DROP_OFFSET = 3.2;
    private static final double HATCH_PLACE_OFFSET = 1.5; //1.2

//    private static final double highAccel = 30000;
//    private static final double mediumAccel = 10000; // was 13000
//    private static final double slowMediumAccel = 6000; // was 11000
//    private static final double lowAccel = 3000; // was 7000

    private static final double maxVelocity = 60000;

    public static boolean setHatchDrop, setHatchPlace;

    /**
     * Initializes the elevator motor, sets PID values, and zeros the elevator
     * encoder
     */
    static {
        elevator = new CANSparkMax(5, MotorType.kBrushless);
        elevator.setInverted(false);
        elevator.getPIDController().setOutputRange(-1.0, 1.0);
        elevator.getPIDController().setSmartMotionAccelStrategy(AccelStrategy.kTrapezoidal, 0);
        elevator.getPIDController().setSmartMotionMaxAccel(30000, 0);
        elevator.getPIDController().setSmartMotionMaxVelocity(maxVelocity, 0);
        elevator.getPIDController().setSmartMotionAllowedClosedLoopError(0.2, 0);
        elevator.getPIDController().setSmartMotionMinOutputVelocity(0.01, 0);
        elevator.enableVoltageCompensation(12.00);

        elevator.getPIDController().setReference(0, ControlType.kSmartMotion);

        elevator.setSmartCurrentLimit(60);

        setPID(0.000_15, 5.0E-09, 0.000_00, 0.0); // setPID(0.000_18, 0.0, 0.001_00, 0.0);

        elevatorEncoder = new CANEncoder(elevator);

        elevator.setEncPosition(0);
        setPosition = elevatorEncoder.getPosition();
        prevPosition = elevatorEncoder.getPosition();
        zeroPosition = elevatorEncoder.getPosition();
        prevVelocity = 0;

        setHatchDrop = false;
        setHatchPlace = false;
    }

    /**
     * @return true if the elevator is within deadband of its set value
     */
    public static boolean doneMoving() {
        return Math.abs(elevatorEncoder.getPosition() - setPosition) < DEADBAND;
    }

    /**
     * inserts the p i d f values into the Talon SRX
     *
     * @param p: proportional value
     * @param i: integral value
     * @param d: derivative value
     * @param f: feed forward value
     */
    private static void setPID(double p, double i, double d, double f) {
        elevator.getPIDController().setP(p);
        elevator.getPIDController().setI(i);
        elevator.getPIDController().setD(d);
        elevator.getPIDController().setFF(f);
    }

    /**
     * sets the elevator set value to its current value
     */
    public static void stop() {
        setPosition = elevatorEncoder.getPosition();
        setPosition(setPosition);
    }

    /**
     * sets the elevator set value to its current value
     */
    public static double getCurrent() {
        return elevator.getOutputCurrent();
    }

    /**
     * @return the set point of the elevator
     */
    public static double getSetPosition() {
        return setPosition - zeroPosition;
    }

    /**
     * @return the position of the elevator
     */
    public static double getPosition() {
        return elevatorEncoder.getPosition() - zeroPosition;
    }

    /**
     * @return true if the elevator is above the stationary stage
     */
    public static boolean aboveStageThreshold() {
        return elevatorEncoder.getPosition() > STAGE_THRESHOLD + zeroPosition;
    }

    /**
     * Sets the elevator to the entered value
     *
     * @param targetPosition: desired elevator value
     */
    public static void setPosition(double targetPosition) {
        if (targetPosition < getPosition()) { // down
            if (getPosition() - targetPosition < 25) { // down medium
                if (getPosition() - targetPosition < 4) { // down short
                    elevator.getPIDController().setSmartMotionMaxAccel(10000, 0);
                } else {
                    elevator.getPIDController().setSmartMotionMaxAccel(15000, 0);
                }
            } else { // down big
                elevator.getPIDController().setSmartMotionMaxAccel(20000, 0);
            }
        } else if (targetPosition > getPosition()) { // up
            if (targetPosition - getPosition() < 25) { // up medium
                elevator.getPIDController().setSmartMotionMaxAccel(40000, 0);
            } else { // up high
                elevator.getPIDController().setSmartMotionMaxAccel(80000, 0);
            }
        } else {
            elevator.getPIDController().setSmartMotionMaxAccel(80000, 0);
        }

        setPosition = targetPosition + zeroPosition;
        limitPosition();
        setHatchDrop = false;
        setHatchPlace = false;
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
     * Positions of elevator
     */
    public enum Position {
        ELEVATOR_FLOOR(0.0),
        HATCH_LEVEL_ONE(1.5),
        HATCH_LEVEL_TWO(22.0),
        HATCH_LEVEL_THREE(44.5),
        CARGO_LEVEL_ONE(17.0),
        CARGO_LEVEL_TWO(39.5),
        CARGO_LEVEL_THREE(46.5),
        CARGO_SHIP(33.0),
        ELEVATOR_COLLECT_CARGO(6.8),
        ELEVATOR_COLLECT_HATCH(HATCH_DROP_OFFSET + HATCH_PLACE_OFFSET);

        public double value;

        Position(double position) {
            value = position;
        }

    }

    /**
     * Sets the elevator to the entered position
     *
     * @param position desired elevator position
     */
    public static void setPosition(Position position) {
        if (position.name().toLowerCase().contains("hatch")) {
            setPosition(position.value + HATCH_DROP_OFFSET);
        } else {
            setPosition(position.value);
        }
    }

    /**
     * Sets the elevator lower by the hatch offset to drop the hatch panel
     */
    public static void dropHatch() {
        if (!setHatchDrop) {
            setPosition -= HATCH_DROP_OFFSET;
            setPosition(setPosition);
            setHatchDrop = true;
        }
    }

    /**
     * Sets the elevator lower by the hatch offset to drop the hatch panel
     */
    public static void placeHatch() {
        if (!setHatchPlace) {
            setPosition -= HATCH_PLACE_OFFSET;
            setPosition(setPosition);
            setHatchPlace = true;
        }
    }


    /**
     * Sets the current elevator position to the new zero
     */
    public static void resetEncoder() {
//        elevator.setEncPosition(0);
        zeroPosition = elevatorEncoder.getPosition();
        setPosition = Position.ELEVATOR_FLOOR.value;
        setPosition(setPosition);
    }

    /**
     * Prevents the user from going past the maximum value of the elevator
     */
    private static void limitPosition() {
        setPosition = Math.min(setPosition, MAX_POSITION + zeroPosition);
        setPosition = Math.max(setPosition, zeroPosition);
        elevator.getPIDController().setReference(setPosition, ControlType.kSmartMotion);
    }

    public static double getVelocity() {
        prevVelocity = currentVelocity;
        currentVelocity = elevatorEncoder.getVelocity();
//        if (currentVelocity > elevator.getPIDController().getSmartMotionMaxVelocity(0) * .9) PrettyPrint.once("ABOVE 90%");
        return currentVelocity;
    }

    public static double getAcceleration() {
        currentAcceleration = (currentVelocity - prevVelocity) / .02;
        return currentAcceleration;
    }

    public static double getTemperature() {
        return elevator.getMotorTemperature();
    }

    public static double percentOutput() {
        return elevator.getAppliedOutput();
    }
}