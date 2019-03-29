package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.robot.Robot;
import frc.robot.controls.DriverControls;
import frc.robot.utils.PrettyPrint;

public class Climber {
    private static final CANSparkMax back, front;
    private static final CANEncoder backEnc, frontEnc;
    private static final PIDController PID;
    private static double balancePidOutput;

    private static int stepNum = 0;
    private static double dtStartPosition;

    private static final double holdSpeed = .08; // TODO make this much smaller
    private static final double kP = 0.065, kI = 0, kD = 0;

    private static final double backHab3Height = 138.5;
    private static final double frontHab3Height = 138.5; // TODO front 3 climb height

    private static final double neoSpeedEqualizingCoefficient = 0.75; //0.43
    private static final double frontHab2Height = 73000; // todo
    private static final double backHab2Height = 58;
    private static boolean firstTime = true;

    /*
     * Initializes and sets up all motors and PID Controllers
     */
    static {
        // Declare motors
        back = new CANSparkMax(7, MotorType.kBrushless);
        back.setInverted(true);
        backEnc = new CANEncoder(back);

        front = new CANSparkMax(8, MotorType.kBrushless);
        front.setInverted(true);
        frontEnc = new CANEncoder(front);

        // Declare PID Output
        PIDOutput pidOutput = output -> balancePidOutput = output;

        // Declare PID Source
        PIDSource source = new PIDSource() {
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                return PIDSourceType.kDisplacement;
            }

            @Override
            public double pidGet() {
                return Robot.gyro.getPitch();
            }
        };

        // Declare PID Controller
        PID = new PIDController(kP, kI, kD, source, pidOutput);
        PID.setSetpoint(0);
        PID.setInputRange(-180, 180);
        PID.setOutputRange(-1, 1);
        PID.setAbsoluteTolerance(1);

        // Enable
        PID.enable();

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
                if (getBackEncPosition() > backHab3Height || DriverControls.getShareButton()) {
                    PID.setSetpoint(-10);
                    stepNum++;
                }

                front.set(speed + balancePidOutput);
                back.set(speed - balancePidOutput);

                break;
            case 1:     //tilting forwards
                if (Robot.gyro.getPitch() < -10) {
                    stepNum++;
                }

                front.set(speed + balancePidOutput);
                back.set(speed - balancePidOutput);
                break;
            case 2:     //drive forward
                // TODO how to determine if front wheels are on
                final double currentDriving = 90;
                if (Drivetrain.frontRight.getOutputCurrent() >= currentDriving &&
                        Drivetrain.frontLeft.getOutputCurrent() >= currentDriving) {
                    stepNum++;
                }

                Drivetrain.drive(.2, 0, 0);
                back.set(holdSpeed);
                front.set(holdSpeed);
                break;
            case 3:     // retract front leg
                if (frontEnc.getPosition() <= 2) { // TODO retracted position
                    stepNum++;
                }

                front.set(-1); //TODO PID?
                break;
            case 4:     // drive forward:
                // TODO how to determine if wheels are on? current again, but higher?
//                if ()

                front.set(holdSpeed);
                Drivetrain.drive(.2, 0, 0);
                break;
            case 5:     // retract back leg while driving forward slightly
                if (backEnc.getPosition() <= 2) {
                    stepNum++;
                }

                back.set(-1); // TODO PID?
                Drivetrain.drive(.1, 0, 0);
                break;
            case 6:     // DONE
                stepNum++;
                manualClimbBoth(0);
                break;
        }
    }

    /**
     * Climb to level two autonomously
     */
    public static void climbLevelThree() {
        switch (stepNum) {
            case 0:     // Raise front legs
                if (frontEnc.getPosition() >= frontHab2Height) {
                    dtStartPosition = Drivetrain.getEncoderPosition();
                    stepNum++;
                }

                front.set(1); // TODO PID?
                break;
            case 1:     // Drive front wheels onto platform
                if (Drivetrain.getEncoderPosition() - dtStartPosition >= 2000) { // TODO distance
                    stepNum++;
                }

                front.set(0);
                Drivetrain.drive(.2, 0, 0);
                break;
            case 2:
                manualClimbBack(holdSpeed);
                manualClimbFront(holdSpeed);
                break;
//            case 2:     // Raise back leg/retract front leg
//                if (backEnc.getPosition() >= backHab2Height) {
//                    if (frontEnc.getPosition() <= 2) {
//                        dtStartPosition = Drivetrain.getEncoderPosition();
//                        stepNum++;
//                    } else {
//                        back.set(0);
//                    }
//                } else {
//                    if (frontEnc.getPosition() <= 2) {
//                        front.set(0);
//                    }
//                }
//
//                Drivetrain.drive(0, 0, 0);
//                back.set(1); // TODO PID?
//                front.set(-1);
//                break;
        }
    }

    /**
     * Climb to level two autonomously
     */
    public static void climbLevelTwo() {
        PrettyPrint.put("Step", stepNum);
        if (firstTime) {
            firstTime = false;
            front.setEncPosition(0);
            back.setEncPosition(0);
        }
        switch (stepNum) {
            case 0:     // Raise front legs
                manualClimbFront(.5);

                if (frontEnc.getPosition() >= frontHab2Height) {
                    dtStartPosition = Drivetrain.getEncoderPosition();
                    stepNum++;
                }
                break;
            case 1:     // Drive front wheels onto platform
                manualClimbFront(0);
                Drivetrain.drive(0.5, 0, 0);

                PrettyPrint.put("Forwards traveled", Drivetrain.getEncoderPosition() - dtStartPosition);
                if (Drivetrain.getEncoderPosition() - dtStartPosition >= 120) { // was 85
                    stepNum++;
                }
                break;
            case 2:     // Raise back leg/retract front leg
                Drivetrain.drive(0, 0, 0);
                back.set(.5);
                manualClimbFront(-.5);

                if (backEnc.getPosition() >= backHab2Height) {
                    if (frontEnc.getPosition() <= 4000) {
                        dtStartPosition = Drivetrain.getEncoderPosition();
                        stepNum++;
                    } else {
                        back.set(0);
                    }
                } else {
                    if (frontEnc.getPosition() <= 4000) {
                        manualClimbFront(0);
                    }
                }

                break;
            case 3:     // Drive onto platform
                back.set(0);
                PrettyPrint.put("Vel", Drivetrain.getEncoderVelocity());
                if (Drivetrain.getEncoderPosition() - dtStartPosition >= 100) {
                    Drivetrain.drive(.2, 0, 0);
                } else {
                    Drivetrain.drive(.4, 0, 0);
                }
                if (Drivetrain.getEncoderPosition() - dtStartPosition >= 140) {
                    stepNum++;
                }
                break;
            case 4:     // Retract back leg
                back.set(-.5);
//                if (backEnc.getPosition() <= 9) {
//                    Drivetrain.drive(.3, 0, 0);
//                } else {
                Drivetrain.drive(.05, 0, 0);
//                }
                if (backEnc.getPosition() <= 4) {
                    stepNum++;
                }
                break;
            case 5:     // DONE
                Drivetrain.drive(0.3, 0, 0);
                back.set(0);
                break;
        }
    }


    /**
     * Climb without gyro assistance
     *
     * @param speed: the speed at which to climb
     */
    public static void manualClimbFront(double speed) {
        front.set(speed);
    }

    /**
     * Climb without gyro assistance
     *
     * @param speed: the speed at which to climb
     */
    public static void manualClimbBack(double speed) {
        back.set(speed);
    }

    public static void manualClimbBoth(double speed) {
        front.set(speed);
        back.set(speed);
    }

    /**
     * Retracts the climber, front legs first, then back
     *
     * @param speed: the speed of retraction
     */
    public static void retractClimber(double speed) {
        if (stepNum == 2) {
            if (frontEnc.getPosition() < 2000) {
                front.set(-speed);
            } else {
                back.set(-speed);
            }
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
}