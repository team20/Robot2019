package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Elevator {

    static TalonSRX elevator;

    private static int setPosition = 0, prevPosition = 0;
    private static final int TICKS_PER_INCH = 695;
    private static final int MAX_POSITION = 0;
    private static final int DEADBAND = 50;

    public static final int FLOOR_POSITION = 0;
    public static final int HATCH_LEVEL_ONE_POSITION = 0;
    public static final int HATCH_LEVEL_TWO_POSITION = 0;
    public static final int HATCH_LEVEL_THREE_POSITION = 0;
    public static final int CARGO_LEVEL_ONE_POSITION = 0;
    public static final int CARGO_LEVEL_TWO_POSITION = 0;
    public static final int CARGO_LEVEL_THREE_POSITION = 0;
    public static final int CARGO_SHIP_POSITION = 0;

	
	public Elevator(){
        elevator = new TalonSRX(0);
        elevator.setInverted(false);
        elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 1000);
		setPID(0.075, 0.000015, 1.1, 0.0);

        setPosition = elevator.getSelectedSensorPosition(0);
	}
	
    /**
     * Prevents the user from going past the maximum position of the elevator
     */
    public static void limitPosition(){
		if(setPosition < MAX_POSITION){
			setPosition = MAX_POSITION;
			elevator.set(ControlMode.Position, setPosition);
		}
	}

    /**
	 * inserts the p i d f values into the Talon SRX
	 * @param p: proportional value
	 * @param i: integral value
	 * @param d: derivative value
	 * @param f: feed forward value
	 */
	public static void setPID(double p, double i, double d, double f) {
		elevator.config_kP(0, p, 1000);
		elevator.config_kI(0, i, 1000);
		elevator.config_kD(0, d, 1000);
		elevator.config_kF(0, f, 1000);
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
     * 
     */
    public static void setPosition(int pos){
        setPosition = pos;
        elevator.set(ControlMode.Position, setPosition);
    }

    /**
	 * sets the elevator set position to its current position
	 */
	public static void stop(){
		setPosition = elevator.getSelectedSensorPosition(0);
        setPosition(setPosition);
	}

	/**
	 * brings the elevator up six inches
	 */
	public static void upIncrement(){
		setPosition = elevator.getSelectedSensorPosition(0) - (int)6*TICKS_PER_INCH;
        setPosition(setPosition);
	}
	
	/**
	 * brings the elevator down six inches
	 */
	public static void downIncrement(){
		setPosition = elevator.getSelectedSensorPosition(0) + (int)6*TICKS_PER_INCH;
		if (setPosition > 0)
			setPosition = 0;
            setPosition(setPosition);
        }
	
	/**
	 * @return the set point of the elevator
	 */
	public static int getSetPosition(){
		return setPosition;
	}
	
	/**
	 * @return true if the elevator is within deadband of its set position
	 */
	public static boolean elevatorMoving(){
		if(Math.abs(elevator.getSelectedSensorPosition(0) - prevPosition) > DEADBAND){
			prevPosition = elevator.getSelectedSensorPosition(0);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * moves the elevator at a speed (percent output)
	 * @param speed: speed of the elevator (-1.0 to 1.0)
	 */
	public static void moveSpeed(double speed){
		elevator.set(ControlMode.PercentOutput, speed);
	}

    /**
     * Resets the elevator encoder to zero
     */
    public static void reset(){
		elevator.setSelectedSensorPosition(0, 0, 1000);
	}	 
}