package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Intake {

    private static Servo hatch;
    private static VictorSPX cargo;

    /**
     * Initializes and sets up all motors for the intake
     */
    public Intake() {
        hatch = new Servo(1);
        cargo = new VictorSPX(0);
    }

    public static void runCargo(double speed) {
        cargo.set(ControlMode.PercentOutput, speed);
    }

    public static void openHatch() {
        hatch.set(0);
    }

    public static void closeHatch() {
        hatch.set(1);
    }
}