package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.I2C.Port;

public class Arduino implements Runnable, PIDSource {
    //I2C port to use with Arduino
    private final int address;

    private Notifier thread;
    private I2C Wire;
    private PIDSourceType pidSourceType;

    //data to be written to Arduino
    byte[] writeData;
    //data read from Arduino
    byte[] readData;
    //does the camera see an object?
    private boolean objInView;
    //distance in inches from distance sensor
    private int distance;
    //x-value of coordinates of point to be turned towards
    private int xValue;
    //useful variables from [Robot.java]
    private int auto;
    private double setPoint;

    public Arduino (int a) {
        thread = new Notifier(this);
        setPIDSourceType(PIDSourceType.kDisplacement);
        address = a;
        Wire = new I2C(Port.kOnboard, address);

        writeData = new byte[4];
        readData = new byte[4];
    }

    public boolean getObjInView() {
        return objInView;
    }

    public int getDistance() {
        return distance;
    }

    public int getXValue() {
        return xValue;
    }

    public void setLEDStripPattern(int pattern) {
        writeData[0] = (byte)pattern;
    }

    public void setPixyCamState(int state) {
        writeData[1] = (byte)state;
    }

    public void setUltrasonicState(boolean enabled) {
        writeData[2] = (byte)(enabled ? 1 : 0);
    }

    public void startThread() {
        thread.startPeriodic(0.02);
    }

    public void stopThread() {
        thread.stop();
    }

    @Override
    public void run() {
        read();
        write();
    }

    public void read() {
        //get data from Arduino as byte array
        Wire.read(address, readData.length, readData);
        //set values from array to variables
        objInView = readData[0] == 1;
        xValue = readData[1];
        distance = readData[2];
    }

    public void write() {
        //write data to Arduino as byte array
        Wire.writeBulk(writeData, writeData.length);
    }

    public void setAuto(int a) {
        auto = a;
    }

    public void setSetPoint(double sp) {
        setPoint = sp;
    }

    @Override
    public void setPIDSourceType(PIDSourceType type) {
        pidSourceType = type;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return pidSourceType;
    }

    @Override
    public double pidGet() {
        switch (auto) {
            case 0:
                return getXValue();
            case 1:
                return -getDistance() + setPoint * 2;
            default:
                return 0;
        }
	}
}