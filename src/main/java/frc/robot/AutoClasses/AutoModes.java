package frc.robot.AutoClasses;

import frc.robot.AutoClasses.Setup.RocketScript;
import frc.robot.AutoClasses.Functions.DriveTime;
import frc.robot.AutoClasses.Functions.MoveElevator;

public class AutoModes {
    private RocketScript rocketScript;

    public AutoModes(){
        rocketScript = new RocketScript();
    }
    
    public void crossLine(){
        rocketScript.runFunction(new DriveTime(), -0.25, 1.0);
        rocketScript.runInParallel(new MoveElevator(), 200.0);
        rocketScript.runFunction(new DriveTime(), 0.25, 1.0);
    }
    public void runAuto(){
        rocketScript.run();
    }
}