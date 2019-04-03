package frc.robot.auto.setup;

public abstract class RobotFunction {
    /**
     * whether or not this function is to be ran in parallel with other functions
     */
    protected boolean isParallel = false;
    protected boolean isInitialized = false;

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
}