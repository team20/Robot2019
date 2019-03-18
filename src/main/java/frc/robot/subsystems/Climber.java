package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
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
    private static final CANSparkMax back;
    private static final CANEncoder backEnc;
    private static final TalonSRX front;
    private static final PIDController PID;
    private static double balancePidOutput;
    private static int stepNum = 0;
    private static double dtStartPosition = 0;

    private static final double holdSpeed = 0.08;
    private static final double kP = 0.065, kI = 0, kD = 0;

    private static final double neoSpeedEqualizingCoefficient = 0.75; //0.43
    private static final double backHab3Height = 138.5;
    private static final double frontHab2Height = 73000;
    private static final double backHab2Height = 58;
    private static boolean firstTime = true;

    private Climber() {
    }

    /*
     * Initializes and sets up all motors and PID Controllers
     */
    static {
        // Declare motors
        back = new CANSparkMax(7, MotorType.kBrushless);
        back.setInverted(true);
        backEnc = new CANEncoder(back);
        front = new TalonSRX(8);
        front.setInverted(true);

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

        front.setSelectedSensorPosition(0);
        back.setEncPosition(0);
    }

    /**
     * Climb with gyro assistance
     *
     * @param speed: the speed at which to climb
     */
    public static void balanceClimb(double speed) {
        switch (stepNum) {
            case 0:     //climbing straight up
                if (getBackEncPosition() > backHab3Height || DriverControls.getShareButton()) {
                    PID.setSetpoint(-10);
                    stepNum = 1;
                }

                front.set(ControlMode.PercentOutput, speed + balancePidOutput);
                back.set(neoSpeedEqualizingCoefficient * (speed - balancePidOutput));

                break;
            case 1:     //tilting forwards
                if (Robot.gyro.getPitch() < -10) {
                    stepNum = 2;
                }

                front.set(ControlMode.PercentOutput, speed + balancePidOutput);
                back.set(neoSpeedEqualizingCoefficient * (speed - balancePidOutput));
                break;
            case 2:
                manualClimbBack(holdSpeed);
                manualClimbFront(holdSpeed);
                break;
        }
    }

    /**
     * Climb to level two autonomously
     */
    public static void climbLevelTwo() {
        PrettyPrint.put("Step", stepNum);
        if (firstTime) {
            firstTime = false;
            front.setSelectedSensorPosition(0);
            back.setEncPosition(0);
        }
        switch (stepNum) {
            case 0:     // Raise front legs
                manualClimbFront(.5);

                if (front.getSelectedSensorPosition() >= frontHab2Height) {
                    dtStartPosition = Drivetrain.getEncoderPosition();
                    stepNum++;
                }
                break;
            case 1:     // Drive front wheels onto platform
                manualClimbFront(0);
                Drivetrain.drive(0.7, 0, 0);

                PrettyPrint.put("Forwards traveled", Drivetrain.getEncoderPosition() - dtStartPosition);
                if (Drivetrain.getEncoderPosition() - dtStartPosition >= 85) {
                    stepNum++;
                }
                break;
            case 2:     // Raise back leg/retract front leg
                Drivetrain.drive(0, 0, 0);
                back.set(.5);
                manualClimbFront(-.5);

                if (backEnc.getPosition() >= backHab2Height) {
                    if (front.getSelectedSensorPosition() <= 4000) {
                        dtStartPosition = Drivetrain.getEncoderPosition();
                        stepNum++;
                    } else {
                        back.set(0);
                    }
                } else {
                    if (front.getSelectedSensorPosition() <= 4000) {
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
                    Drivetrain.drive(.5, 0, 0);
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
        front.set(ControlMode.PercentOutput, speed);
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
        manualClimbFront(speed * 1.2);
        manualClimbBack(speed * neoSpeedEqualizingCoefficient);
    }

    /**
     * Retracts the climber, front legs first, then back
     *
     * @param speed: the speed of retraction
     */
    public static void retractClimber(double speed) {
        if (stepNum == 2) {
            if (front.getSelectedSensorPosition() < 2000) {
                manualClimbFront(speed);
            } else {
                manualClimbBack(speed);
            }
        }
    }

    /**
     * @return value of the front encoder
     */
    public static int getFrontEncPosition() {
        return front.getSelectedSensorPosition();
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
        front.set(ControlMode.PercentOutput, 0.0);
        back.set(0.0);
    }

    public static void setStepNum(int i) {
        stepNum = i;
    }
}