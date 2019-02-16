package frc.robot.auto.functions;

import edu.wpi.first.wpilibj.PIDController;
import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Arduino;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LineSensor;
import frc.robot.utils.PrettyPrint;

public class Align extends RobotFunction<Boolean> {
    private PIDController
            anglePid,
            speedPid,
            linePid;

    private int
            step,
            prevStep;
    private boolean autoChanged;

    public Align() {
        anglePid = new PIDController(0.012, 0.001, 0.04, Arduino.pidSource, Arduino.pidOutput);
        speedPid = new PIDController(0.02, 0, 0.05, Arduino.pidSource, Arduino.pidOutput);
        linePid = new PIDController(0.002, 0, 0, LineSensor.pidSource, LineSensor.pidOutput);

        step = 0;
        prevStep = -1;
        autoChanged = true;
    }

    @Override
    public void collectInputs(Boolean... values) {
        step = !values[0] ? 0 : 1;
    }

    @Override
    public void run() {
        Arduino.setAuto(step);
        switch (step) {
            //turning towards target
            case 0:
                //first time only...
                if (autoChanged) {
                    Arduino.setPixyCamState(2);
                    //enable angle PID controller
                    anglePid.enable();
//                    System.out.println("AUTO CHANGED (CASE 0)");
                    PrettyPrint.once("AUTO CHANGED (CASE 0)");
                    // //THIS CODE NEEDS TO BE TESTED (is it really needed?)
                    // int temp = arduino.getXValue();
                    // while (temp == arduino.getXValue()) { System.out.println("waiting for x-value..."); }
                    // //END OF CODE THAT NEEDS TO BE TESTED
                }
//                System.out.println("x-value: " + Arduino.getXValue());
                PrettyPrint.put("x-value", Arduino.getXValue());
                //set speed for robot to turn
                Drivetrain.drive(0, -Arduino.getTurnSpeed(), Arduino.getTurnSpeed());
                //if angle is within set tolerance and speed is relatively stable...
                if (anglePid.onTarget() && Arduino.getDTurnSpeed() < 0.0001) {
//                    System.out.println("ON TARGET (CASE 0)");
                    PrettyPrint.once("ON TARGET (CASE 0)");
                    anglePid.reset();
                    step++;
                }
                break;
            //drive towards target using ultrasonic sensor
            case 1:
                //first time only...
                if (autoChanged) {
                    Arduino.setPixyCamState(0);
                    Arduino.setUltrasonicState(true);
                    //disable angle PID controller
                    anglePid.reset();
                    //enable speed PID controller
                    speedPid.enable();
                    linePid.enable();
                    PrettyPrint.once("AUTO CHANGED (CASE 1)");
//                    System.out.println("AUTO CHANGED (CASE 1)");
                }
                PrettyPrint.put("linePosition", LineSensor.getLinePosition());
                PrettyPrint.put("turnSpeed", LineSensor.getTurnSpeed());
                PrettyPrint.put("distance", Arduino.getDistance());
                //set speed for robot to drive forwards
                Drivetrain.drive(Arduino.getDriveSpeed(), -LineSensor.getTurnSpeed(), LineSensor.getTurnSpeed());
                //if distance from wall is within set tolerance...
                if (speedPid.onTarget()) {
//                    System.out.println("ON TARGET (CASE 1)");
                    PrettyPrint.once("ON TARGET (CASE 1)");
                    speedPid.reset();
                    step++;
                }
                break;
            case 2:
                anglePid.reset();
                speedPid.reset();
                linePid.reset();
                Drivetrain.drive(0, 0, 0);
//                System.out.println("DONE");
                PrettyPrint.once("ALIGN DONE");
                step++;
                break;
        }
        //makes it possible to run code first time only in each case in switch statement above
        autoChanged = step != prevStep;
        prevStep = step;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}