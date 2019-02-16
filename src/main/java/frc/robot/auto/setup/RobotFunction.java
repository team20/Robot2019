package frc.robot.auto.setup;

public abstract class RobotFunction<T> {
    /**
     * whether or not this function is to be ran in parallel with other functions
     */
    public boolean isParallel = false;

    /**
     * Stores values
     *
     * @param values type differs between functions
     */
    public abstract void collectInputs(T...values);

    /**
     * Runs the function
     */
    public abstract void run();

    /**
     * Stops the function
     */
    public abstract void stop();

    /**
     * @return true when the function is completed
     */
    public abstract boolean isFinished();
}