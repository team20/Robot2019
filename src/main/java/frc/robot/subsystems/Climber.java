package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;

public class Climber {
    public static final CANSparkMax back;
    public static final CANSparkMax front;
    private static final CANEncoder backEnc, frontEnc;
    public static final DigitalInput backIRSensor;
    private static final double frontHab2Height = 56; //58
//    private static double dtStartPosition;

    private static final double holdSpeed = .08;
    private static final double kP = 0.065, kI = 0, kD = 0;

    public static final double backHab3Height = 138.8;
    public static int stepNumL2 = 0, stepNumL3 = 0;
    private static final double backHab2Height = 58;
    private static boolean firstTime = true;
    private static boolean overrideFrontLegRetraction = false;

    /**
     * Initializes and sets up all motors and PID Controllers
     */
    static {
        // Declare motors
        back = new CANSparkMax(7, MotorType.kBrushless);
        back.setInverted(true);
        backEnc = new CANEncoder(back);

        front = new CANSparkMax(8, MotorType.kBrushless);
        front.setInverted(false);
        frontEnc = new CANEncoder(front);

        back.setIdleMode(IdleMode.kBrake);

        front.setEncPosition(0);
        back.setEncPosition(0);

        backIRSensor = new DigitalInput(3);
    }

    /**
     * Climb with gyro assistance
     */
    public static void climbLevelThree() {
        switch (stepNumL3) {
            case 0:     //climbing straight up
                if (getBackEncPosition() > backHab3Height) {
                    stepNumL3 = 1;
                }

                front.set(1.0);
                back.set(1.0);

                break;
            case 1:     //tilting forwards
                if (getBackEncPosition() >= 182.5) { // 35
                    stepNumL3 = 2;
                }

                front.set(0.25);
                back.set(1.0);
                break;
            case 2: // retract front legs
                Drivetrain.resetEncoders();
                if (getFrontEncPosition() <= 4) {
                    stepNumL3 = 3;
                }

                Drivetrain.drive(0.05);
                back.set(holdSpeed);
                front.set(-1.0);
                break;
            case 3: // drive forwards:
                if (!backIRSensor.get()) {
                    stepNumL3 = 4;
                }

                Drivetrain.drive(0.25);
                back.set(-0.20);
                break;
            case 4: // retract back:
                double driveSpeed = retractBackLeg(0.70);
                Drivetrain.drive(driveSpeed);
                break;
//            case 2:
//                front.set(holdSpeed);
//                back.set(holdSpeed);
//                break;
        }
    }

    /**
     * Climb to level two autonomously
     */
    public static void climbLevelTwo() {
        if (firstTime) {
            firstTime = false;
            front.setEncPosition(0);
            back.setEncPosition(0);
            Drivetrain.resetEncoders();
        }
        switch (stepNumL2) {
            case 0:     // Raise front legs
                front.set(0.75);
                Drivetrain.resetEncoders();

                if (frontEnc.getPosition() >= frontHab2Height) {
                    Drivetrain.resetEncoders();
//                    dtStartPosition = Drivetrain.getEncoderPosition();
                    stepNumL2 = 1;
                }
                break;
            case 1:     // Drive front wheels onto platform
                front.set(0);
                Drivetrain.drive(0.55);

                if (Drivetrain.getEncoderPosition() > 120 && Drivetrain.getEncoderVelocity() <= 15) { // was 85
                    stepNumL2 = 2;
                }
                break;
            case 2:     // Raise back leg/retract front leg
                Drivetrain.drive(0);
                back.set(1.0);
                front.set(-1.0);

                Drivetrain.resetEncoders(); // reset early cuz talons are slow?

                if (backEnc.getPosition() >= backHab2Height) {
                    if (frontEnc.getPosition() <= 4) {
                        stepNumL2 = 3;
                        front.set(0);
                    } else {
                        back.set(0);
                    }
                } else {
                    if (frontEnc.getPosition() <= 4) {
                        front.set(0);
                    }
                }

                break;
            case 3:     // Drive onto platform
                back.set(0);
                front.set(0);
                Drivetrain.drive(0.5);

                if ((Drivetrain.getEncoderVelocity() <= 15 && Drivetrain.getEncoderPosition() >= 70) || !backIRSensor.get()) {
                    stepNumL2 = 4;
                }
                break;
            case 4:     // Retract back leg
                back.set(-1.0);
                front.set(0);
                Drivetrain.drive(0.1);
                if (backEnc.getPosition() <= 4) {
                    stepNumL2 = 5;
                }
                break;
            case 5:     // DONE
                Drivetrain.drive(0.3);
                front.set(0);
                back.set(0);
                break;
        }
    }

    /**
     * Retracts the back climber leg, using the IR sensor to stay forwards
     *
     * @param speed %output to retract leg at, positive -> retraction
     * @return speed of drivetrain forwards
     */
    public static double retractBackLeg(double speed) {
        if (backEnc.getPosition() > 15.0) {
            back.set(-speed);
            return backIRSensor.get() ? 0.15 : 0.0; // drive forwards until robot is flat
        } else {
            return .3;
        }
    }

    /**
     * @return value of the front encoder
     */
    public static double getFrontEncPosition() {
        return frontEnc.getPosition();
    }

    /**
     * @return value fo the back encoder
     */
    public static double getBackEncPosition() {
        return backEnc.getPosition();
    }

    /**
     * Stop all motors
     */
    public static void stop() {
        front.set(0.0);
        back.set(0.0);
    }
}