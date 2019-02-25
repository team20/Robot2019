package frc.robot.auto.setup;

import frc.robot.auto.functions.TeleopControls;
import frc.robot.controls.DriverControls;
import frc.robot.controls.OperatorControls;
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

//        PrettyPrint.put("script size", auto::size);
    }

    /**
     * Adds a subsequent function to the ArrayList
     * the use of {@code values} depends on the function
     */
    @SafeVarargs
    final public <T> void addFunction(RobotFunction<T> fxn, T... values) {
        auto.add(fxn);
        fxn.collectInputs(values);
    }

    /**
     * Adds a function to run parallel to the one added before to the ArrayList,
     * the use of {@code values} depends on the function
     */
    @SafeVarargs
    final public <T> void addInParallel(RobotFunction<T> fxn, T... values) {
        auto.add(fxn);
        fxn.isParallel = true;
        fxn.collectInputs(values);
    }

    /**
     * Runs the added functions in order
     * <p>Parallel functions are run at the same time as the function before it</p>
     * <p>If the driver presses the override auto button, a {@code TeleopControls} function is inserted
     * at the beginning of the RocketScript</p>
     */
    public void run() {
        if (DriverControls.isOverridingAuto() || OperatorControls.isOverridingAuto()) {
            if (!(auto.get(0) instanceof TeleopControls)) {
                TeleopControls fxn = new TeleopControls();
                fxn.collectInputs(false);
                auto.add(0, fxn);
            }
        }

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