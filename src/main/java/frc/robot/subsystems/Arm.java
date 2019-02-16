package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Arm {
    private static CANSparkMax armMotor;
    private static CANPIDController pidController;
    private static CANEncoder armEncoder;

    private static double setPosition, prevPosition;

    private static final double DEADBAND = 0.3;

    public static final double FLOOR_POSITION = 0.0;
    public static final double CARGO_SHOOT_POSITION = 0.0;
    public static final double PLACE_POSITION = 0.0;
    public static final double STARTING_CONFIG_POSITION = 0.0;

    /**
     * Initializes all necessary objects and variables
     */
    public Arm() {
        //motor setup
        armMotor = new CANSparkMax(1, MotorType.kBrushless);
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
     * Sets the arm to the floor position
     */
    public static void setStartingConfigPosition(){
        setPosition(STARTING_CONFIG_POSITION);
    }

    /**
     * Sets the arm to the place (cargo or hatch) position
     */
    public static void setPlacePosition(){
        setPosition(PLACE_POSITION);
    }

    /**
     * Sets the arm to the cargo shoot position
     */
    public static void setCargoShootPosition(){
        setPosition(CARGO_SHOOT_POSITION);
    }

    /**
     * Sets the arm to the floor position
     */
    public static void setFloorPosition(){
        setPosition(FLOOR_POSITION);
    }

    /**
     * Sets the position of the elevator
     * @param pos: desired position
     */
    public static void setPosition(double pos){
        setPosition = pos;
        pidController.setReference(setPosition, ControlType.kPosition);
    }

 	/**
	 * @return true if the arm is within deadband of its set position
	 */
	public static boolean armDoneMoving() {
		if(Math.abs(armEncoder.getPosition() - prevPosition) > DEADBAND){
			prevPosition = armEncoder.getPosition();
			return true;
		} else {
			return false;
		}
	}

}