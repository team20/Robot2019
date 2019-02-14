package frc.robot.auto.functions;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import edu.wpi.first.wpilibj.Notifier;
import frc.robot.auto.setup.RobotFunction;
import frc.robot.utils.PrettyPrint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.InputMismatchException;

import static frc.robot.subsystems.Drivetrain.frontLeft;
import static frc.robot.subsystems.Drivetrain.frontRight;
import static frc.robot.subsystems.Drivetrain.motionProfile;

class FollowPath extends RobotFunction<String> {
    private final int minPoints = 5;
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
            frontLeft().pushMotionProfileTrajectory(point);

        for (TrajectoryPoint point : fromFile(values[0] + "right.csv"))
            frontRight().pushMotionProfileTrajectory(point);

        //push points twice as fast as robot's loop runs
        new Notifier(() -> frontLeft().processMotionProfileBuffer()).startPeriodic(.01);
        new Notifier(() -> frontRight().processMotionProfileBuffer()).startPeriodic(.01);
        PrettyPrint.put("MP left vel", frontLeft()::getActiveTrajectoryVelocity);
        PrettyPrint.put("MP right vel", frontRight()::getActiveTrajectoryVelocity);
    }

    /**
     * puts the motion profile in hold mode, holds last pair of velocities
     */
    @Override
    public void stop() {
        motionProfile(false);
        PrettyPrint.remove("MP left vel", "MP right vel");
    }

    @Override
    public void run() {
        // ensure that buffer is sufficiently filled
        motionProfile(started || getLeftStatus().btmBufferCnt >= minPoints && getRightStatus().btmBufferCnt >= minPoints);
        started = started || getLeftStatus().btmBufferCnt >= minPoints && getRightStatus().btmBufferCnt >= minPoints;
    }

    @Override
    public boolean isFinished() {
        return frontLeft().getMotionProfileTopLevelBufferCount() == 0 &&
                frontRight().getMotionProfileTopLevelBufferCount() == 0 &&
                started;
    }

    public MotionProfileStatus getLeftStatus() {
        var status = new MotionProfileStatus();
        frontLeft().getMotionProfileStatus(status);
        return status;
    }

    public MotionProfileStatus getRightStatus() {
        var status = new MotionProfileStatus();
        frontRight().getMotionProfileStatus(status);
        return status;
    }

    private TrajectoryPoint[] fromFile(String filePath) {
        try {
            var points = new BufferedReader(new FileReader(filePath))
                    .lines()
                    .skip(1)
                    .map(s -> s.split(","))
                    .map(strArray -> Arrays
                            .stream(strArray)
                            .mapToDouble(Double::parseDouble)
                            .toArray())
                    .map(vals -> {
                        var p = new TrajectoryPoint();
                        p.position = vals[0];
                        p.velocity = vals[1];
//                        p.headingDeg = vals[2]; not tested (maybe not used)
                        // might need to set pid slots etc
                        p.profileSlotSelect0 = 0;
                        return p;
                    })
                    .toArray(TrajectoryPoint[]::new);
            points[0].zeroPos = true;
            points[points.length - 1].isLastPoint = true;
            return points;
        } catch (FileNotFoundException e) {
            PrettyPrint.error("PathFollower file not found");
        }
        return new TrajectoryPoint[0];
    }
}
