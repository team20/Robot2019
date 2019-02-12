package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class LineSensor implements Runnable, PIDSource, PIDOutput {
    //the line sensor's I2C address is hard-coded into the board as 9 and cannot be changed
    private final int address;

    //separate thread for data collection and calculations
    private Notifier thread;
    //I2C communication protocol
    private I2C wire;
    //part of using a PID controller
    private PIDSourceType pidSouceType;

    //raw data from the sensor
    private byte[] rawSensorData;
    //improved format of the data that is used in the calculations
    private int[] sensorData;
    //sum of all sensor values after each is multiplied by a value larger than that of the previous
    private int numerator;
    //sum of all sensor values
    private int denominator;
    //value from 0 to 700 representing how far right or left the sensor is over the line
    private int linePosition;
    //the speed for the robot to adjust its angle at
    private double turnSpeed;

    public LineSensor() {
        address = 9;

        thread = new Notifier(this);
        wire = new I2C(Port.kOnboard, address);

        rawSensorData = new byte[16];
        sensorData = new int[8];
        turnSpeed = 0;

        setPIDSourceType(PIDSourceType.kDisplacement);
    }

    public double getLinePosition() {
        return linePosition;
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    //start thread running once every [ms] milliseconds
    public void startThread(int ms) {
        thread.startPeriodic((double)ms / 1000);
    }

    //stops thread from running
    public void stopThread() {
        thread.stop();
    }

    //this function is called every time the thread runs
    @Override
    public void run() {
        updateLinePosition();
    }

    //calculates right-left value based off of sensor values using method described in [readLine] method here: https://www.pololu.com/docs/0J19/all
    private void updateLinePosition() {
        // wire.read(address, rawSensorData.length, rawSensorData);
        wire.readOnly(rawSensorData, rawSensorData.length);
        //store useful data from sensor in [sensorData]
        for (int i = 0; i < sensorData.length; i ++)
            sensorData[i] = rawSensorData[i * 2];
        numerator = 0;
        denominator = 0;
        for (int i = 0; i < sensorData.length; i ++) {
            numerator += sensorData[i] * i * 100;
            denominator += sensorData[i];
        }
        if (denominator != 0)
            linePosition = numerator / denominator;
        else
            System.out.println("LINE SENSOR NEEDS TO BE RESET");
    }

    @Override
    public void setPIDSourceType(PIDSourceType type) {
        pidSouceType = type;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return pidSouceType;
    }

    //input for PID controller
    @Override
    public double pidGet() {
        return linePosition;
	}

    //output for PID controller
    @Override
    public void pidWrite(double output) {
        turnSpeed = output;
    }
}