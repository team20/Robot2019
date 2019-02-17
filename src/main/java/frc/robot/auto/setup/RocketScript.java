package frc.robot.auto.setup;

import frc.robot.utils.PrettyPrint;

import java.util.ArrayList;

public class RocketScript {
    private ArrayList<RobotFunction<?>> auto;
    private int autoSteps;
    private boolean lastCommand;

    /**
     * Initializes necessary ArrayLists and variables
     */
    public RocketScript() {
        auto = new ArrayList<>();
        autoSteps = 0;
        lastCommand = false;

        PrettyPrint.put("script size", auto::size);
    }

    /**
     * Adds a subsequent function to the ArrayList
     * the use of nums depends on the function
     */
    @SafeVarargs
    final public <T> void runFunction(RobotFunction<T> fxn, T... values) {
        auto.add(fxn);
        fxn.collectInputs(values);
    }

    /**
     * Adds a function to run parallel to the one added before to the ArrayList
     * the use of nums depends on the function
     */
    @SafeVarargs
    final public <T> void runInParallel(RobotFunction<T> fxn, T... values) {
        auto.add(fxn);
        fxn.isParallel = true;
        fxn.collectInputs(values);
    }

    /**
     * Runs the added functions in order
     * Parallel functions are run at the same time as the function before it
     */
    public void run() {
        if (autoSteps == auto.size() - 1)
            lastCommand = true;

        if (autoSteps < auto.size()) {
            auto.get(autoSteps).run();
            if (!lastCommand && auto.get(autoSteps + 1).isParallel) {
                auto.get(autoSteps + 1).run();
            }
            if (!lastCommand && auto.get(autoSteps + 1).isParallel && auto.get(autoSteps).isFinished() && auto.get(autoSteps + 1).isFinished()) {
                auto.get(autoSteps).stop();
                auto.get(autoSteps + 1).stop();
                autoSteps = autoSteps + 2;
            } else if (!lastCommand && !auto.get(autoSteps + 1).isParallel && auto.get(autoSteps).isFinished()) {
                auto.get(autoSteps).stop();
                autoSteps++;
            } else if (lastCommand && auto.get(autoSteps).isFinished()) {
                auto.get(autoSteps).stop();
                autoSteps++;
            }
            PrettyPrint.put("auto step num", autoSteps);
        }
    }
}