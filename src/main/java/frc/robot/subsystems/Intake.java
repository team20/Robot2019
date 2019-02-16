package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Intake {

    private static Servo hatch;
    private static VictorSPX cargo;
    private static DigitalInput cargoSensor;

    /**
     * Initializes and sets up all motors for the intake
     */
    public Intake() {
        hatch = new Servo(1);
        cargo = new VictorSPX(0);
        cargoSensor = new DigitalInput(0);
    }

    /**
     * Runs the cargo motor at collection speed
     */
    public static void collectCargo(){
        runCargoMotor(1.0);
    }

    /**
     * Runs the cargo motor at spitting speed
     */
    public static void spitCargo(){
        runCargoMotor(-1.0);
    }

    /**
     * Runs the cargo motor at spitting speed
     */
    public static void outtakeCargo(){
        runCargoMotor(-0.5);
    }

    /**
     * Stops the cargo motor
     */
    public static void stopCargoRollers(){
        runCargoMotor(0.0);
    }

    /**
     * Opens the hatch mechanism 
     */
    public static void openHatch() {
        hatch.set(0);
    }

    /**
     * Closes the hatch mechanism
     */
    public static void closeHatch() {
        hatch.set(1);
    }

    /**
     * Runs the cargo intake until the digital sensor is tripped
     * @param: speed = speed of the cargo motor (-1.0 to 1.0)
     * @return: true if a cargo is in the intake
     */
    public static boolean intakeMode(){
        if(!cargoSensor.get()){
            collectCargo();
            return false;
        } else {
            stopCargoRollers();
            return true;
        }
    }

    /**
     * Runs the cargo collector 
     * @param: speed = speed of the cargo motor (-1.0 to 1.0)
     */
    private static void runCargoMotor(double speed) {
        cargo.set(ControlMode.PercentOutput, speed);
    }

}

