package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.controls.DriverControls;
import frc.robot.controls.OperatorControls;

/**
 * Allows for manual control of robot as a {@code RobotFunction}
 */
public class TeleopControls extends RobotFunction {
    private boolean isFullyTeleop;

    public TeleopControls(boolean isFullyTeleop) {
        this.isFullyTeleop = isFullyTeleop;
    }

    @Override
    public void init() {
    }

    @Override
    public void run() {
        DriverControls.driverControls();
        OperatorControls.operatorControls();
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isFinished() {
        if (isFullyTeleop) return false;

        return DriverControls.isStoppingAutoControl() || OperatorControls.isStoppingAutoControl();
    }
}
