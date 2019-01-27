package frc.robot.AutoClasses.Setup;

import java.util.ArrayList;

public class RocketScript {
    private ArrayList<RobotFunction> auto;
    private ArrayList<Boolean> inParallel;
    private int autoSteps;
    private boolean lastCommand;

    /**
     * Initializes necessary ArrayLists and variables
     */
    public RocketScript() {
        auto = new ArrayList<RobotFunction>();
        inParallel = new ArrayList<Boolean>();
        autoSteps = 0;
        lastCommand = false;
    }

    /**
     * Adds a subsequent function to the ArrayList
     * Stores the integer for the function
     */
    public void runFunction(RobotFunction fxn, int one) {
        auto.add(fxn);
        inParallel.add(false);
        fxn.collectInputs(one);
    }

    /**
     * Adds a subsequent function to the ArrayList
     * Stores the double for the function
     */
    public void runFunction(RobotFunction fxn, double one) {
        auto.add(fxn);
        inParallel.add(false);
        fxn.collectInputs(one);
    }

    /**
     * Adds a subsequent function to the ArrayList
     * Stores the two doubles for the function
     */
    public void runFunction(RobotFunction fxn, double one, double two) {
        auto.add(fxn);
        inParallel.add(false);
        fxn.collectInputs(one, two);
    }

    /**
     * Adds a function to run parallel to the one added before to the ArrayList
     * Stores the integer for the function
     */
    public void runInParallel(RobotFunction fxn, int one) {
        auto.add(fxn);
        inParallel.add(true);
        fxn.collectInputs(one);
    }

    /**
     * Adds a function to run parallel to the one added before to the ArrayList
     * Stores the double for the function
     */
    public void runInParallel(RobotFunction fxn, double one) {
        auto.add(fxn);
        inParallel.add(true);
        fxn.collectInputs(one);
    }

    /**
     * Adds a function to run parallel to the one added before to the ArrayList
     * Stores the two doubles for the function
     */
    public void runInParallel(RobotFunction fxn, double one, double two) {
        auto.add(fxn);
        inParallel.add(true);
        fxn.collectInputs(one, two);
    }

    /**
     * Runs the added functions in order
     * Parallel functions are run at the same time as the function before it
     */
    public void run() {
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