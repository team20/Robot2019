package frc.robot.auto.functions;

import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.PrettyPrint;

import java.util.InputMismatchException;

/**
 * Align to vision target (do not use)
 * <p>if {@code values[0]} is true, {@code Align} skips the turn to target phase</p>
 */
public class Align extends RobotFunction<Boolean> {
    private int
            step,
            prevStep;

    private boolean
            autoChanged,
            finished;

    public Align() {
        step = 0;
        prevStep = -1;
        autoChanged = true;
        finished = false;
    }

    /**
     * whether or not to skip turning towards the target
     */
    @Override
    public void collectInputs(Boolean... values) {
        if (values.length != 1) throw new InputMismatchException("Align requires ONE value");

        step = values[0] ? 1 : 0;
    }

    @Override
    public void run() {
//        Arduino.setAuto(step);
//        switch (step) {
//            //turning towards target
//            case 0:
//                //first time only...
//                if (autoChanged) {
//                    Arduino.setPixyCamState(2);
//                    //enable angle PID controller
//                    anglePid.enable();
//                    PrettyPrint.once("AUTO CHANGED (CASE 0)");
//                    // //THIS CODE NEEDS TO BE TESTED (is it really needed?)
//                    // int temp = arduino.getXValue();
//                    // while (temp == arduino.getXValue()) { System.out.println("waiting for x-value..."); }
//                    // //END OF CODE THAT NEEDS TO BE TESTED
//                }
//                PrettyPrint.put("x-value", Arduino.getXValue());
//                //set speed for robot to turn
//                Drivetrain.drive(0, -Arduino.getTurnSpeed(), Arduino.getTurnSpeed());
//                //if angle is within set tolerance and speed is relatively stable...
//                if (anglePid.onTarget() && Arduino.getDTurnSpeed() < 0.0001) {
//                    PrettyPrint.once("ON TARGET (CASE 0)");
//                    anglePid.reset();
//                    step++;
//                }
//                break;
//            //drive towards target using ultrasonic sensor
//            case 1:
//                //first time only...
//                if (autoChanged) {
//                    Arduino.setPixyCamState(0);
//                    Arduino.setUltrasonicState(true);
//                    //disable angle PID controller
//                    anglePid.reset();
//                    //enable speed PID controller
//                    speedPid.enable();
//                    linePid.enable();
//                    PrettyPrint.once("AUTO CHANGED (CASE 1)");
//                }
//                PrettyPrint.put("linePosition", LineSensor.getLinePosition());
//                PrettyPrint.put("turnSpeed", LineSensor.getTurnSpeed());
//                PrettyPrint.put("distance", Arduino.getDistance());
//                //set speed for robot to drive forwards
//                Drivetrain.drive(Arduino.getDriveSpeed(), -LineSensor.getTurnSpeed(), LineSensor.getTurnSpeed());
//                //if distance from wall is within set tolerance...
//                if (speedPid.onTarget()) {
//                    PrettyPrint.once("ON TARGET (CASE 1)");
//                    speedPid.reset();
//                    step++;
//                }
//                break;
//            case 2:
//                Drivetrain.drive(0, 0, 0);
//                finished = true;
//                step++;
//                break;
//        }
//        //makes it possible to run code first time only in each case in switch statement above
//        autoChanged = step != prevStep;
//        prevStep = step;
    }

    @Override
    public void stop() {
        Drivetrain.drive(0);
//        anglePid.reset();
//        speedPid.reset();
        // linePid.reset();
        PrettyPrint.once("ALIGN DONE");
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}