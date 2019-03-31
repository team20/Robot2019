package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class Arduino {
    public static final PIDController pixyPid;
    public static final PIDSource pidSource;
    public static final PIDOutput pidOutput;
    //separate thread for data collection and calculations
    private final static Notifier thread;
    //I2C communication protocol
    private final static I2C wire;

    //colors for diagnostic LED signals
    public enum Colors {
        Red, Orange, Yellow, Green, Blue, Purple, None
    }

    //I2C port to use with Arduino
    private static final int address;
    //data to be written to Arduino
    private static byte[] writeData;
    //data read from Arduino
    private static byte[] readData;
    //does the camera see an object?
    private static boolean isObjInView;
//    //distance in inches from distance sensor
//    private static int distance;
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
    // //useful variables from [Robot.java]
    // private static int auto;
    // private static double setPoint;

    static {
        pidSource = new PIDSource() {
            private PIDSourceType pidSourceType;

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
                return xValue;
            }
        };
        pidSource.setPIDSourceType(PIDSourceType.kDisplacement);

        pidOutput = output -> {
            dTurnSpeed = Math.abs(turnSpeed - output);
            turnSpeed = output;
        };

        pixyPid = new PIDController(0.001, 0, 0, Arduino.pidSource, Arduino.pidOutput);
        pixyPid.setInputRange(0, 315);
        pixyPid.setOutputRange(-1, 1);
        pixyPid.setContinuous(false);
        pixyPid.setSetpoint(157);

        thread = new Notifier(() -> {
            write();
            read();
        });
        address = 0x1;
        wire = new I2C(Port.kOnboard, address);

        writeData = new byte[5];
        readData = new byte[2];
        turnSpeed = 0;
        dTurnSpeed = 0;
//        driveSpeed = 0;
//        dDriveSpeed = 0;
    }

    public static boolean isObjInView() {
        return isObjInView;
    }

    // public static int getDistance() {
    //     return distance;
    // }

    public static int getXValue() {
        return xValue;
    }

    public static double getTurnSpeed() {
        return turnSpeed;
    }

    public static double getDTurnSpeed() {
        return dTurnSpeed;
    }

    // public static double getDriveSpeed() {
    //     return driveSpeed;
    // }

    // public static double getDDriveSpeed() {
    //     return dDriveSpeed;
    // }

    public static void setAllianceColor(Alliance color) {
        switch (color) {
            case Red:
                writeData[0] = 0;
                break;
            case Blue:
                writeData[0] = 1;
                break;
            case Invalid:
                writeData[0] = 2;
                break;
        }
    }

    public static void setPattern(int pattern) {
        writeData[1] = (byte) pattern;
    }

    public static void setDiagnosticPattern(Colors color, int pattern) {
        if (color != null) {
            switch (color) {
                case Red:
                    writeData[2] = 0;
                    break;
                case Orange:
                    writeData[2] = 1;
                    break;
                case Yellow:
                    writeData[2] = 2;
                    break;
                case Green:
                    writeData[2] = 3;
                    break;
                case Blue:
                    writeData[2] = 4;
                    break;
                case Purple:
                    writeData[2] = 5;
                    break;
                case None:
                default:
                    break;
            }
        }
        writeData[3] = (byte) pattern;
    }

    public static void setPixyCamState(int state) {
        writeData[4] = (byte) state;
    }

    // public static void setUltrasonicState(boolean enabled) {
    //     writeData[5] = (byte) (enabled ? 1 : 0);
    // }

    public static void startThread() {
        thread.startPeriodic(0.05);
    }

    public static void stopThread() {
        thread.stop();
    }

    public static void read() {
        //get data from Arduino as byte array
        wire.read(address, readData.length, readData);
        //set values from array to variables
        isObjInView = readData[0] == 1;
        xValue = readData[1];
        // distance = readData[2];
    }

    public static void write() {
        //write data to Arduino as byte array
        wire.writeBulk(writeData);
    }

//    public static void setAuto(int a) {
//        auto = a;
//    }
//
//    public static void setSetPoint(double sp) {
//        setPoint = sp;
//    }
}