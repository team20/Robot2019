package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Climber {
    public static final CANSparkMax back;
    public static final CANSparkMax front;
    private static final CANEncoder backEnc, frontEnc;

    private static int stepNum = 0;
//    private static double dtStartPosition;

    private static final double holdSpeed = .08;
    private static final double kP = 0.065, kI = 0, kD = 0;

    public static final double backHab3Height = 138.8;

    private static final double frontHab2Height = 58;
    private static final double backHab2Height = 58;
    private static boolean firstTime = true;

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
    }

    /**
     * Climb with gyro assistance
     *
     * @param speed: the speed at which to climb
     */
    public static void climbLevelThree(double speed) {
        switch (stepNum) {
            case 0:     //climbing straight up
                if (getBackEncPosition() > backHab3Height) {
                    stepNum++;
                }

                front.set(speed);
                back.set(speed);

                break;
            case 1:     //tilting forwards
                if (getBackEncPosition() - getFrontEncPosition() >= 32) { // 35
                    stepNum++;
                }

                front.set(speed - 0.65);
                back.set(speed);
                break;
            case 2:
                front.set(holdSpeed);
                back.set(holdSpeed);
                break;
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
        switch (stepNum) {
            case 0:     // Raise front legs
                front.set(0.75);

                if (frontEnc.getPosition() >= frontHab2Height) {
                    Drivetrain.resetEncoders();
//                    dtStartPosition = Drivetrain.getEncoderPosition();
                    stepNum = 1;
                }
                break;
            case 1:     // Drive front wheels onto platform
                front.set(0);
                Drivetrain.drive(0.55);

//                PrettyPrint.put("Forwards traveled", Drivetrain.getEncoderPosition() - dtStartPosition);
                if (Drivetrain.getEncoderPosition() > 120) { // was 85
                    stepNum = 2;
                }
                break;
            case 2:     // Raise back leg/retract front leg
                Drivetrain.drive(0);
                back.set(1.0);
                front.set(-1.0);

                Drivetrain.resetEncoders(); // reset early cuz talons are slow?

                if (backEnc.getPosition() >= backHab2Height) {
                    if (frontEnc.getPosition() <= 4) {
                        stepNum = 3;
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
                Drivetrain.drive(0.5);

                if (Drivetrain.getEncoderVelocity() <= -25 && Drivetrain.getEncoderPosition() >= 70) {
                    stepNum = 4;
                }
                break;
            case 4:     // Retract back leg
                back.set(-1.0);
                Drivetrain.drive(0.1);
                if (backEnc.getPosition() <= 4) {
                    stepNum = 5;
                }
                break;
            case 5:     // DONE
                Drivetrain.drive(0.3);
                back.set(0);
                break;
        }
    }

    /**
     * Retracts the climber, front legs first, then back
     *
     * @return speed of drivetrain forwards
     */
    public static double retractBackLeg(double speed) {
        if (backEnc.getPosition() > 15.0) { // can be larger
            back.set(speed);
            return 0.0; //TODO use IR sensor to drive slightly forwards
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

    public static void setStepNum(int i) {
        stepNum = i;
    }

    public static int getStepNum() {
        return stepNum;
    }
}