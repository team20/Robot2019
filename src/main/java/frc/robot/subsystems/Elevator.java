package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;

public class Elevator {

    static CANSparkMax elevator;
    static CANEncoder elevatorEncoder;

    private static double setPosition, prevPosition;

    private static final double TICKS_PER_INCH = 695;
    private static final double STAGE_THRESHOLD = 0.0;
    private static final double MAX_POSITION = 0.0;
    private static final double DEADBAND = 50;

    public static final double FLOOR_POSITION = 0.0;
    public static final double HATCH_LEVEL_ONE_POSITION = 0.0;
    public static final double HATCH_LEVEL_TWO_POSITION = 0.0;
    public static final double HATCH_LEVEL_THREE_POSITION = 0.0;
    public static final double CARGO_LEVEL_ONE_POSITION = 0.0;
    public static final double CARGO_LEVEL_TWO_POSITION = 0.0;
    public static final double CARGO_LEVEL_THREE_POSITION = 0.0;
    public static final double CARGO_SHIP_POSITION = 0.0;
	
    /**
     * Initializes the elevator motor, sets PID values, and zeros the elevator encoder
     */
    public Elevator() {
        elevator = new CANSparkMax(30, CANSparkMaxLowLevel.MotorType.kBrushless);
        elevator.setInverted(false);
        elevator.getPIDController().setOutputRange(-1.0, 1.0);
		setPID(0.075, 0.000015, 1.1, 0.0);

        elevatorEncoder = new CANEncoder(elevator);

        setPosition = elevatorEncoder.getPosition();
        prevPosition = 0.0;
    }
	
    /**
	 * inserts the p i d f values into the Talon SRX
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
	
    /**
     * Sets the elevator to the floor position
     */
    public static void setFloor() {
        setPosition = FLOOR_POSITION;
        setPosition(setPosition);
    }

    /**
     * Sets the elevator to the hatch one position
     */
    public static void setHatchLevelOne() {
        setPosition = HATCH_LEVEL_ONE_POSITION;
        setPosition(setPosition);
    }

    /**
     * Sets the elevator to the hatch two position
     */
    public static void setHatchLevelTwo() {
        setPosition = HATCH_LEVEL_TWO_POSITION;
        setPosition(setPosition);
    }

    /**
     * Sets the elevator to the hatch three position
     */
    public static void setHatchLevelThree() {
        setPosition = HATCH_LEVEL_THREE_POSITION;
        setPosition(setPosition);
    }

    /**
     * Sets the elevator to the cargo one position
     */
    public static void setCargoLevelOne() {
        setPosition = CARGO_LEVEL_ONE_POSITION;
        setPosition(setPosition);
    }

    /**
     * Sets the elevator to the cargo two position
     */
    public static void setCargoLevelTwo() {
        setPosition = CARGO_LEVEL_TWO_POSITION;
        setPosition(setPosition);
    }
   
    /**
     * Sets the elevator to the cargo three position
     */
    public static void setCargoLevelThree() {
        setPosition = CARGO_LEVEL_THREE_POSITION;
        setPosition(setPosition);
    }
    
    /**
     * Sets the elevator to the cargo ship position
     */
    public static void setCargoShip() {
        setPosition = CARGO_SHIP_POSITION;
        setPosition(setPosition);
    }

    /**
	 * sets the elevator set position to its current position
	 */
	public static void stop() {
		setPosition = elevatorEncoder.getPosition();
        setPosition(setPosition);
	}

	/**
	 * brings the elevator up six inches
	 */
	public static void upIncrement() {
		setPosition = elevatorEncoder.getPosition() - (int)6*TICKS_PER_INCH;
        setPosition(setPosition);
	}
	
	/**
	 * brings the elevator down six inches
	 */
	public static void downIncrement() {
		setPosition = elevatorEncoder.getPosition() + (int)6*TICKS_PER_INCH;
		if (setPosition > 0)
			setPosition = 0;
            setPosition(setPosition);
        }
	
	/**
	 * @return the set point of the elevator
	 */
	public static double getSetPosition() {
		return setPosition;
    }
    
    public static boolean aboveStageThreshold(){
        if(elevatorEncoder.getPosition() > STAGE_THRESHOLD){
            return true;
        }
        return false;
    }
	
	/**
	 * @return true if the elevator is within deadband of its set position
	 */
	public static boolean elevatorDoneMoving() {
		if(Math.abs(elevatorEncoder.getPosition() - prevPosition) > DEADBAND){
			prevPosition = elevatorEncoder.getPosition();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * moves the elevator at a speed (percent output)
	 * @param speed: speed of the elevator (-1.0 to 1.0)
	 */
	public static void moveSpeed(double speed) {
		elevator.set(speed);
	}

    /**
     * Sets the elevator to the entered position
     * @param: desired elevator position
     */
    public static void setPosition(double pos) {
        setPosition = pos;
        limitPosition();
    }

    /**
     * Prevents the user from going past the maximum position of the elevator
     */
    private static void limitPosition() {
		if(setPosition > MAX_POSITION){
			setPosition = MAX_POSITION;
            elevator.getPIDController().setReference(setPosition, ControlType.kPosition);
		} else {
            elevator.getPIDController().setReference(setPosition, ControlType.kPosition);
        }
	}
}