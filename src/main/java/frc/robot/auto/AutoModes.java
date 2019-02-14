package frc.robot.auto;

import frc.robot.auto.functions.*;
import frc.robot.auto.setup.RocketScript;

public class AutoModes {
    private RocketScript rocketScript;
    public enum Mode {
        CrossLine,
        Align
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
        rocketScript.runFunction(new DriveTime(), 0.5, 2.0);
    }

    public void align(int step) {
        rocketScript.runFunction(new Align(), step);
    }

    /**
     * Runs the set autonomous - must be run in autonomous periodic
     */
    public void runAuto() {
        rocketScript.run();
    }
}