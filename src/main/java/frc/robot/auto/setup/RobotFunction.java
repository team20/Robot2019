package frc.robot.auto.setup;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

public abstract class RobotFunction {
    /**
     * whether or not this function is to be ran in parallel with other functions
     */
    boolean isParallel = false;

    /**
     * if this function's init function has been called
     */
    boolean isInitialized = false;

    /**
     * all functions that must be completed before this runs
     */
    ArrayList<RobotFunction> prerequisiteFunctions = new ArrayList<>();

    /**
     * done once when the function is first ran
     */
    public abstract void init();

    /**
     * what the function does repeatedly
     */
    public abstract void run();

    /**
     * @return true when the function is completed
     */
    public abstract boolean isFinished();

    /**
     * any cleanup actions
     */
    public abstract void stop();

    public static RobotFunctionBuilder run(Runnable action) {
        var builder = new RobotFunctionBuilder();
        builder.run(action);
        return builder;
    }

    public static RobotFunctionBuilder doOnce(Runnable action) {
        var builder = new RobotFunctionBuilder();
        builder.onStart(action);
        builder.doneWhen(() -> true);
        return builder;
    }

    public static class RobotFunctionBuilder {
        private Runnable init;
        private Runnable action;
        private BooleanSupplier doneCondition;
        private Runnable stopAction;
        private ArrayList<RobotFunction> prereqFunctions;

        public RobotFunction build() {
            return new RobotFunction() {
                @Override
                public void init() {
                    if (init != null) init.run();
                }

                @Override
                public void run() {
                    if (action != null) action.run();
                }

                @Override
                public boolean isFinished() {
                    if (doneCondition != null)
                        return doneCondition.getAsBoolean();
                    else
                        return false;
                }

                @Override
                public void stop() {
                    if (stopAction != null) stopAction.run();
                }
            };
        }

        public RobotFunctionBuilder onStart(Runnable init) {
            this.init = init;
            return this;
        }

        public RobotFunctionBuilder run(Runnable action) {
            this.action = action;
            return this;
        }

        public RobotFunctionBuilder doneWhen(BooleanSupplier doneCondition) {
            this.doneCondition = doneCondition;
            return this;
        }

        public RobotFunctionBuilder onFinish(Runnable stopAction) {
            this.stopAction = stopAction;
            return this;
        }
    }
}