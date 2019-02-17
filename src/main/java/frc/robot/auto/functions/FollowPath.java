package frc.robot.auto.functions;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import edu.wpi.first.wpilibj.Notifier;
import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.PrettyPrint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * Follows a motion profiling path saved in a csv
 */
public class FollowPath extends RobotFunction<String> {
    private final int minPoints = 5;
    private final int msPerPoint = 10;
    private final double kP = 0.5;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kF = 0.0;

    private boolean started;

    public FollowPath() {
        started = false;
    }

    /**
     * pushes motion profile points into the drivetrain motors
     *
     * @param values one String that is the filepath to the spline to be ran
     */
    @Override
    public void collectInputs(String... values) {
        if (values.length != 1) throw new InputMismatchException("FollowPath requires ONE filepath");

        for (TrajectoryPoint point : fromFile(values[0] + "left.csv"))
            Drivetrain.frontLeft.pushMotionProfileTrajectory(point);

        for (TrajectoryPoint point : fromFile(values[0] + "right.csv"))
            Drivetrain.frontRight.pushMotionProfileTrajectory(point);

        //push points three times as fast as robot's loop runs
        new Notifier(() -> {
            Drivetrain.frontLeft.processMotionProfileBuffer();
            Drivetrain.frontRight.processMotionProfileBuffer();
        }).startPeriodic(msPerPoint / 3000.0);

        configTalons();

        PrettyPrint.put("MP left vel", Drivetrain.frontLeft::getActiveTrajectoryVelocity);
        PrettyPrint.put("MP right vel", Drivetrain.frontRight::getActiveTrajectoryVelocity);
    }

    /**
     * puts the motion profile in hold mode, holds last pair of velocities
     */
    @Override
    public void stop() {
        Drivetrain.motionProfile(false);
        PrettyPrint.remove("MP left vel", "MP right vel");
    }

    @Override
    public void run() {
        // ensure that buffer is sufficiently filled
        started |= getLeftStatus().btmBufferCnt >= minPoints && getRightStatus().btmBufferCnt >= minPoints;

        Drivetrain.motionProfile(started);
    }

    @Override
    public boolean isFinished() {
        return Drivetrain.frontLeft.getMotionProfileTopLevelBufferCount() == 0 &&
                Drivetrain.frontRight.getMotionProfileTopLevelBufferCount() == 0 &&
                started;
    }

    public MotionProfileStatus getLeftStatus() {
        var status = new MotionProfileStatus();
        Drivetrain.frontLeft.getMotionProfileStatus(status);
        return status;
    }

    public MotionProfileStatus getRightStatus() {
        var status = new MotionProfileStatus();
        Drivetrain.frontRight.getMotionProfileStatus(status);
        return status;
    }

    private TrajectoryPoint[] fromFile(String filePath) {
        try (var reader = new BufferedReader(new FileReader(filePath))) {
            var points = reader
                    .lines()
                    .skip(1)
                    .map(s -> s.split(","))
                    .map(strArray -> Arrays
                            .stream(strArray)
                            .mapToDouble(Double::parseDouble)
                            .toArray())
                    .map(vals -> {
                        var p = new TrajectoryPoint();
                        p.profileSlotSelect0 = 0;
                        p.position = vals[0];
                        p.velocity = vals[1];
                        return p;
                    })
                    .toArray(TrajectoryPoint[]::new);
            points[0].zeroPos = true;
            points[points.length - 1].isLastPoint = true;
            return points;
        } catch (IOException e) {
            PrettyPrint.error("PathFollower file not found");
        }
        return new TrajectoryPoint[0];
    }

    /**
     * sets all values of the drivetrain talons so that motion profiling will work
     */
    private void configTalons() {
        // clear old motion profiling stuff
        Drivetrain.frontLeft.clearMotionProfileHasUnderrun();
        Drivetrain.frontLeft.clearMotionProfileTrajectories();

        Drivetrain.frontRight.clearMotionProfileTrajectories();
        Drivetrain.frontRight.clearMotionProfileHasUnderrun();

        // config motion profile values
        Drivetrain.frontLeft.configMotionProfileTrajectoryPeriod(msPerPoint);
        Drivetrain.frontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 1000);
        Drivetrain.frontLeft.setStatusFramePeriod(StatusFrameEnhanced.Status_9_MotProfBuffer, msPerPoint);
        Drivetrain.frontLeft.setStatusFramePeriod(StatusFrame.Status_10_Targets, msPerPoint);
        Drivetrain.frontLeft.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, msPerPoint);
        Drivetrain.frontLeft.setStatusFramePeriod(StatusFrame.Status_17_Targets1, msPerPoint);

        Drivetrain.frontRight.configMotionProfileTrajectoryPeriod(msPerPoint);
        Drivetrain.frontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 1000);
        Drivetrain.frontRight.setStatusFramePeriod(StatusFrameEnhanced.Status_9_MotProfBuffer, msPerPoint);
        Drivetrain.frontRight.setStatusFramePeriod(StatusFrame.Status_10_Targets, msPerPoint);
        Drivetrain.frontRight.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, msPerPoint);
        Drivetrain.frontRight.setStatusFramePeriod(StatusFrame.Status_17_Targets1, msPerPoint);

        // pid constants for motion profile
        Drivetrain.frontLeft.config_kP(0, kP);
        Drivetrain.frontLeft.config_kI(0, kI);
        Drivetrain.frontLeft.config_kD(0, kD);
        Drivetrain.frontLeft.config_kF(0, kF);

        Drivetrain.frontLeft.config_kP(0, kP);
        Drivetrain.frontLeft.config_kI(0, kI);
        Drivetrain.frontLeft.config_kD(0, kD);
        Drivetrain.frontLeft.config_kF(0, kF);
    }
}