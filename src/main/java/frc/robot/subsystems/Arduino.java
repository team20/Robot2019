package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.I2C.Port;

public class Arduino {
    public static PIDSource pidSource;
    public static PIDOutput pidOutput;
    //separate thread for data collection and calculations
    private static Notifier thread;
    //I2C communication protocol
    private static I2C wire;

    //I2C port to use with Arduino
    private static final int address;
    //data to be written to Arduino
    private static byte[] writeData;
    //data read from Arduino
    private static byte[] readData;
    //does the camera see an object?
    private static boolean objInView;
    //distance in inches from distance sensor
    private static int distance;
    //x-value of coordinates of point to be turned towards
    private static int xValue;
    //speed to turn
    private static double turnSpeed;
    //change in turnSpeed
    private static double dTurnSpeed;
    //speed to drive
    private static double driveSpeed;
    //change in driveSpeed
    private static double dDriveSpeed;
    //useful variables from [Robot.java]
    private static int auto;
    private static double setPoint;

    static {
        pidSource = new PIDSource() {
            private PIDSourceType pidSourceType;

            @Override
            public void setPIDSourceType(PIDSourceType type) {
                pidSourceType = type;
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
            @Override
            public PIDSourceType getPIDSourceType() {
                return pidSourceType;
            }
        };
        pidOutput = new PIDOutput() {
            @Override
            public void pidWrite(double output) {
                switch (auto) {
                    case 0:
                        dTurnSpeed = Math.abs(turnSpeed - output);
                        turnSpeed = output;
                        break;
                    case 1:
                        dDriveSpeed = Math.abs(driveSpeed - output);
                        driveSpeed = output;
                        break;
                }
            }
        };
        thread = new Notifier(() -> update());
        pidSource.setPIDSourceType(PIDSourceType.kDisplacement);
        address = 0;
        wire = new I2C(Port.kOnboard, address);

        writeData = new byte[4];
        readData = new byte[3];
        turnSpeed = 0;
        dTurnSpeed = 0;
        driveSpeed = 0;
        dDriveSpeed = 0;
    }

    public static boolean getObjInView() {
        return objInView;
    }

    public static int getDistance() {
        return distance;
    }

    public static int getXValue() {
        return xValue;
    }

    public static double getTurnSpeed() {
        return turnSpeed;
    }

    public static double getDTurnSpeed() {
        return dTurnSpeed;
    }

    public static double getDriveSpeed() {
        return driveSpeed;
    }

    public static double getDDriveSpeed() {
        return dDriveSpeed;
    }

    public static void setLEDStripPattern(int main, int diagnostic) {
        writeData[0] = (byte)main;
        writeData[1] = (byte)diagnostic;
    }

    public static void setPixyCamState(int state) {
        writeData[2] = (byte)state;
    }

    public static void setUltrasonicState(boolean enabled) {
        writeData[3] = (byte)(enabled ? 1 : 0);
    }

    public static void startThread() {
        thread.startPeriodic(0.02);
    }

    public static void stopThread() {
        thread.stop();
    }

    public static void update() {
        read();
        write();
    }

    public static void read() {
        //get data from Arduino as byte array
        wire.read(address, readData.length, readData);
        //set values from array to variables
        objInView = readData[0] == 1;
        xValue = readData[1];
        distance = readData[2];
    }

    public static void write() {
        //write data to Arduino as byte array
        wire.writeBulk(writeData, writeData.length);
    }

    public static void setAuto(int a) {
        auto = a;
    }

    public static void setSetPoint(double sp) {
        setPoint = sp;
    }
}