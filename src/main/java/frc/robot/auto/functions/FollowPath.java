package frc.robot.auto.functions;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Notifier;
import frc.robot.auto.setup.RobotFunction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

class FollowPath extends RobotFunction {
    private TalonSRX leftMaster, rightMaster;
    //    private BufferedTrajectoryPointStream leftPoints, rightPoints;
    private TrajectoryPoint[] leftPoints, rightPoints;
    private final int minPoints = 10;
    private boolean started = false;

    public FollowPath(String filePath, TalonSRX leftMaster, TalonSRX rightMaster) {
        leftMaster.setStatusFramePeriod(StatusFrame.Status_10_Targets, 20);
        leftMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20);
        leftMaster.setStatusFramePeriod(StatusFrame.Status_17_Targets1, 20);

        rightMaster.setStatusFramePeriod(StatusFrame.Status_10_Targets, 20);
        rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20);
        rightMaster.setStatusFramePeriod(StatusFrame.Status_17_Targets1, 20);

        leftPoints = fromFile(filePath + "left.csv");
        for (TrajectoryPoint point : leftPoints)
            leftMaster.pushMotionProfileTrajectory(point);

        rightPoints = fromFile(filePath + "right.csv");
        for (TrajectoryPoint point : rightPoints)
            rightMaster.pushMotionProfileTrajectory(point);

        this.leftMaster = leftMaster;
        this.rightMaster = rightMaster;
    }

    public void start() {
        leftMaster.configMotionProfileTrajectoryPeriod(20);
        leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 1000);
        leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_9_MotProfBuffer, 20);

        rightMaster.configMotionProfileTrajectoryPeriod(20);
        rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 1000);
        rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_9_MotProfBuffer, 20);

        new Notifier(() -> leftMaster.processMotionProfileBuffer()).startPeriodic(.01);
        new Notifier(() -> rightMaster.processMotionProfileBuffer()).startPeriodic(.01);
    }

    @Override
    public void run() {
        if (started || getLeftStatus().btmBufferCnt >= minPoints) {
            leftMaster.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
            started = true;
        } else {
            leftMaster.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
        }

        if (started || getRightStatus().btmBufferCnt >= minPoints) {
            rightMaster.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
            started = true;
        } else {
            rightMaster.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
        }
    }

    @Override
    public boolean finished() {
        return leftMaster.getMotionProfileTopLevelBufferCount() == 0 &&
                rightMaster.getMotionProfileTopLevelBufferCount() == 0 &&
                started;
    }

    private MotionProfileStatus getLeftStatus() {
        var status = new MotionProfileStatus();
        leftMaster.getMotionProfileStatus(status);
        return status;
    }

    private MotionProfileStatus getRightStatus() {
        var status = new MotionProfileStatus();
        rightMaster.getMotionProfileStatus(status);
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
                        p.velocity = vals[1]
                        ;
                        //might need to set pid slots etc
                        p.profileSlotSelect0 = 0;
                        return p;
                    })
                    .toArray(TrajectoryPoint[]::new);
            points[0].zeroPos = true;
            points[points.length - 1].isLastPoint = true;
            return points;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new TrajectoryPoint[0];
    }
}
