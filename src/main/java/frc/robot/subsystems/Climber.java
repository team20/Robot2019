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

public class Climber {
    private static final CANSparkMax back, front;
    private static final CANEncoder backEnc, frontEnc;
    private static final PIDController PID;
    private static double balancePidOutput;
    private static int stepNum = 0;
    private static double holdSpeed = .08; // TODO make this much smaller

    private static final double kP = 0.065, kI = 0, kD = 0;
    private static final double backHab3Height = 138.5;
    private static final double frontHab3Height = 138.5; // TODO front climb height

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
    public static void balanceClimb(double speed) {
        switch (stepNum) {
            case 0:     //climbing straight up
                if (getBackEncPosition() > backHab3Height || DriverControls.getShareButton()) {
                    PID.setSetpoint(-10);
                    stepNum = 1;
                }

                front.set(speed + balancePidOutput);
                back.set(speed - balancePidOutput);

                break;
            case 1:     //tilting forwards
                if (Robot.gyro.getPitch() < -10) {
                    stepNum = 2;
                }

                front.set(speed + balancePidOutput);
                back.set(speed - balancePidOutput);
                break;
            case 2: // TODO more climbing steps
                //drive forward
                manualClimbBack(holdSpeed);
                manualClimbFront(holdSpeed);
                break;
            case 3:
                // retract front leg
                break;
            case 4:
                // drive forward
                break;
            case 5:
                // retract back leg while driving forward slightly
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
        manualClimbFront(speed);
        manualClimbBack(speed);
    }

    /**
     * Retracts the climber, front legs first, then back
     *
     * @param speed: the speed of retraction
     */
    public static void retractClimber(double speed) {
        if (stepNum == 2) {
            if (frontEnc.getPosition() < 2000) {
                manualClimbFront(speed);
            } else {
                manualClimbBack(speed);
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