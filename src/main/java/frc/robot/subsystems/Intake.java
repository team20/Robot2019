package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;

public class Intake {
    private static final Servo hatch;
    private static final VictorSPX cargo;
    private static final DigitalInput cargoSensor;
    private static boolean intakeRunning;

    /**
     * Initializes and sets up all motors for the intake
     */
    static {
        hatch = new Servo(0);
        cargo = new VictorSPX(9);
        cargoSensor = new DigitalInput(0);

        intakeRunning = false;
    }

    /**
     * Runs the cargo motor at collection speed
     */
    public static void collectCargo() {
        runCargoMotor(-0.6); // was .75
        intakeRunning = true;
    }

    /**
     * Runs the cargo motor at spitting speed
     */
    public static void spitCargo() {
        if (Elevator.aboveStageThreshold()) {
            runCargoMotor(0.5);
        } else {
            runCargoMotor(1.0);
        }
        intakeRunning = true;
    }

    /**
     * Runs the cargo motor at spitting speed
     */
    public static void outtakeCargo() {
        runCargoMotor(0.5);
        intakeRunning = true;
    }

    /**
     * Stops the cargo motor
     */
    public static void stopCargoRollers() {
        runCargoMotor(-0.07);
        intakeRunning = false;
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
     * Returns if there is cargo in the intake
     */
    public static boolean isCargoPresent() {
        return !cargoSensor.get();
    }

    public static boolean cargoNotPresent() {
        return cargoSensor.get();
    }

    /**
     * Returns if the hatch is closed
     */
    public static boolean isHatchClosed() {
        return hatch.getPosition() > 0.0;
    }

    /**
     * Runs the cargo intake until the digital sensor is tripped
     *
     * @return true if a cargo is in the intake
     */
    public static boolean intakeMode() {
        if (cargoSensor.get()) {
            collectCargo();
            return false;
        } else {
            stopCargoRollers();
            return true;
        }
    }

    /**
     * Runs the cargo collector
     *
     * @param speed = speed of the cargo motor (-1.0 to 1.0)
     */
    private static void runCargoMotor(double speed) {
        cargo.set(ControlMode.PercentOutput, speed);
        intakeRunning = true;
    }

    public static boolean intakeRunning() {
        return intakeRunning;
    }
}
