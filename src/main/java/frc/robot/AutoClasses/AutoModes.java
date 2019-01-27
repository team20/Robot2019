package frc.robot.AutoClasses;

import frc.robot.AutoClasses.Setup.RocketScript;
import frc.robot.AutoClasses.Functions.DriveTime;

public class AutoModes {
    private RocketScript rocketScript;

    /**
     * Initializes RocketScript
     */
    public AutoModes() {
        rocketScript = new RocketScript();
    }
    
    /**
     * Autonomous: crosses the HAB line
     */
    public void crossLine() {
        rocketScript.runFunction(new DriveTime(), 0.5, 2.0);
    }

    /**
     * Runs the set autonomous - must be run in autonomous periodic
     */
    public void runAuto() {
        rocketScript.run();
    }
}