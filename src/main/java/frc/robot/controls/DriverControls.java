package frc.robot.controls;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;

public class DriverControls {

    private PS4Controller driverJoy;
    private double speedStraight, speedLeft, speedRight;

    /**
     * Initializes the driver controller
     */
    public DriverControls() {
        driverJoy = new PS4Controller(0, 2);
        speedStraight = 0; speedLeft = 0; speedRight = 0;
    }

    /**
     * Runs the driver controls
     */
    public void driverControls() {
        //Drivetrain Controls
        if(Math.abs(driverJoy.getLeftYAxis()) > 0.1){
			speedStraight = -driverJoy.getLeftYAxis();			
		} else {
			speedStraight = 0.0;
		}
		if(Elevator.aboveStageThreshold()){
			if (driverJoy.getSquareButton()) {
				speedLeft = driverJoy.getLeftTriggerAxis()*0.25;
				speedRight = driverJoy.getRightTriggerAxis()*0.25;	
			} else {
				speedLeft = driverJoy.getLeftTriggerAxis()*0.4;
				speedRight = driverJoy.getRightTriggerAxis()*0.4;
			}
		} else {
			if (driverJoy.getSquareButton()) {
				speedLeft = driverJoy.getLeftTriggerAxis()*0.33;
				speedRight = driverJoy.getRightTriggerAxis()*0.33;
			} else {
				speedLeft = driverJoy.getLeftTriggerAxis()*0.65;
				speedRight = driverJoy.getRightTriggerAxis()*0.65;
			}
		}
        Drivetrain.drive(speedStraight, speedRight, speedLeft);
        
        //Climber Controls
        if(driverJoy.getXButton()){
            //TODO insert climbing code here
        } else{
            //TODO insert climber stop code here
        }
    }
}