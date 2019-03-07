package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.controls.DriverControls;
import frc.robot.controls.OperatorControls;

import java.util.InputMismatchException;

/**
 * Allows for manual control of robot as a {@code RobotFunction}
 * <p>if {@code values[0]} is true, this function's {@code isFinished} never returns true</p>
 */
public class TeleopControls extends RobotFunction<Boolean> {
    private boolean isFullyTeleop;

    /**
     * @param values one boolean; whether or not the teleop control lasts for the rest of auto
     */
    @Override
    public void collectInputs(Boolean... values) {
        if (values.length != 1) throw new InputMismatchException("TeleopControls requires ONE boolean");

        isFullyTeleop = values[0];
        super.isParallel = false; //Don't run Teleop in parallel
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

        // TODO should the operator have this control?
        return DriverControls.isStoppingAutoControl() || OperatorControls.isStoppingAutoControl();
    }
}
