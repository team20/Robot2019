package frc.robot.AutoClasses.Setup;

import java.util.ArrayList;

public class RocketScript {
    private ArrayList<RobotFunction> auto;
    private ArrayList<Boolean> inParallel;
    private int autoSteps;
    private boolean lastCommand;

    public RocketScript(){
        auto = new ArrayList<RobotFunction>();
        inParallel = new ArrayList<Boolean>();
        autoSteps = 0;
        lastCommand = false;
    }

    public void runFunction(RobotFunction fxn, int one){
        auto.add(fxn);
        inParallel.add(false);
        fxn.collectInputs(one);
    }

    public void runFunction(RobotFunction fxn, double one){
        auto.add(fxn);
        inParallel.add(false);
        fxn.collectInputs(one);
    }

    public void runFunction(RobotFunction fxn, double one, double two){
        auto.add(fxn);
        inParallel.add(false);
        fxn.collectInputs(one, two);
    }

    public void runInParallel(RobotFunction fxn, int one){
        auto.add(fxn);
        inParallel.add(true);
        fxn.collectInputs(one);
    }

    public void runInParallel(RobotFunction fxn, double one){
        auto.add(fxn);
        inParallel.add(true);
        fxn.collectInputs(one);
    }

    public void runInParallel(RobotFunction fxn, double one, double two){
        auto.add(fxn);
        inParallel.add(true);
        fxn.collectInputs(one, two);
    }

    public void run(){
        System.out.println(auto.size());
        if(autoSteps == auto.size()-1){
            lastCommand = true;
        }
        if(autoSteps < auto.size()){
            auto.get(autoSteps).run();
            if(!lastCommand && inParallel.get(autoSteps+1)){
                auto.get(autoSteps+1).run();
            }
            if(!lastCommand && inParallel.get(autoSteps+1) && auto.get(autoSteps).finished() && auto.get(autoSteps+1).finished()){
                auto.get(autoSteps).stop();
                auto.get(autoSteps+1).stop();
                autoSteps = autoSteps + 2;
            } else if(!lastCommand && !inParallel.get(autoSteps+1) && auto.get(autoSteps).finished()){
                auto.get(autoSteps).stop();
                autoSteps++;
            } else if (lastCommand && auto.get(autoSteps).finished()){
                auto.get(autoSteps).stop();
                autoSteps++;
            }
            System.out.println("AutoSteps: " + autoSteps);
        }
    }
}