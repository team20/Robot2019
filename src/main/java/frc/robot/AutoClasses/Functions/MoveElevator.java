package frc.robot.AutoClasses.Functions;

import frc.robot.AutoClasses.Setup.RobotFunction;
import frc.robot.Subsystems.Elevator;

public class MoveElevator extends RobotFunction {

    private int position;
    private boolean isFinished;

    public MoveElevator(){
        isFinished = false;
        position = 0;
    }

    @Override
    public void collectInputs(int pos){
        position = pos;
    }

    @Override
    public void run(){
        Elevator.setPosition(position);
        isFinished = true;
    }

    @Override
    public boolean finished(){
        return isFinished;
    }
}