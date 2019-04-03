package frc.robot.auto;

import frc.robot.auto.functions.Align;
import frc.robot.auto.functions.DriveTime;
import frc.robot.auto.functions.TeleopControls;
import frc.robot.auto.setup.RocketScript;

public class AutoModes {
    private RocketScript rocketScript;

    public enum Mode {
        CrossLine,
        Align,
        FullyTeleop
    }

    private Mode selectedMode;

    /**
     * Initializes RocketScript
     */
    public AutoModes() {
        rocketScript = new RocketScript();
        selectedMode = null;
    }

    /**
     * Gets selected auto mode
     */
    public Mode getMode() {
        return selectedMode;
    }

    /**
     * Sets selected auto mode
     */
    public void setMode(Mode m) {
        selectedMode = m;
    }

    /**
     * Autonomous: crosses the HAB line
     */
    public void crossLine() {
        rocketScript.addFunction(new DriveTime(0.5, 2.0));
    }

    /**
     * Align to target (hatch on rocket/cargo ship or loading station)
     *
     * @param skip true if turning towards alignment tape should be skipped
     */
    public void align(boolean skip) {
        rocketScript.addFunction(new Align(skip));
    }

    /**
     * No auto - just driver and operator controls the whole time
     */
    public void fullyTeleop() {
        rocketScript.addFunction(new TeleopControls(true));
    }

    /**
     * Runs the set autonomous - must be run in autonomous periodic
     */
    public void runAuto() {
        rocketScript.run();
    }
}