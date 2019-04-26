package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;

public class Intake {
    public static final VictorSPX intakeMotor;
    private static final DigitalInput cargoSensor;
    private static boolean intakeRunning;

    /**
     * Initializes and sets up all motors for the intake
     */
    static {
        intakeMotor = new VictorSPX(9);
        cargoSensor = new DigitalInput(0);

        intakeMotor.enableVoltageCompensation(true);

        intakeRunning = false;
    }

    /**
     * Runs the hatch motor at collection speed
     */
    public static void collectHatch() {
        runCargoMotor(-0.55);
        intakeRunning = true;
    }

    /**
     * Runs the cargo motor at collection speed
     */
    public static void collectCargo() {
        runCargoMotor(-0.6);
        intakeRunning = true;
    }

    /**
     * Runs the cargo motor at spitting speed
     */
    public static void spitCargo() {
        if (Elevator.getSetPosition() > Elevator.Position.CARGO_LEVEL_TWO.value && Elevator.getSetPosition() != Elevator.Position.HATCH_LEVEL_THREE.value) {
            runCargoMotor(0.65);
        } else {
            runCargoMotor(0.9);
        }
        intakeRunning = true;
    }

    /**
     * Runs the cargo motor at spitting speed
     */
    public static void outtakeCargo() {
        runCargoMotor(1.0);
        intakeRunning = true;
    }

    /**
     * Stops the cargo motor
     */
    public static void stopCargoRollers() {
        runCargoMotor(-0.07);
        intakeRunning = false;
    }

    public static double getOutputCurrent() {
        return intakeMotor.getMotorOutputPercent();
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
     * Runs the cargo intake until the digital sensor is tripped
     *
     */
    public static void intakeMode() {
//        if (cargoNotPresent()) {
            collectCargo();
//            //return false;
//        } else {
//            stopCargoRollers();
//            //return true;
//        }
//    }
    }

    /**
     * Runs the cargo collector
     *
     * @param speed = speed of the cargo motor (-1.0 to 1.0)
     */
    private static void runCargoMotor(double speed) {
        intakeMotor.set(ControlMode.PercentOutput, speed * 7.0 / 5.0);
        intakeRunning = true;
    }

    public static boolean intakeRunning() {
        return intakeRunning;
    }
}
