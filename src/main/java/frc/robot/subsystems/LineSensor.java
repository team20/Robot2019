package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.robot.utils.PrettyPrint;

public class LineSensor {
    public static PIDSource pidSource;
    public static PIDOutput pidOutput;
    //separate thread for data collection and calculations
    private static Notifier thread;
    //I2C communication protocol
    private static I2C wire;

    //threshold for whether lineSeen is true or not
    private static final int lineSeenThreshold = 100;   //TODO: figure out what this actually is

    //the line sensor's I2C address is hard-coded into the board as 9 and cannot be changed
    private static final int address;
    //raw data from the sensor
    private static byte[] rawSensorData;
    //improved format of the data that is used in the calculations
    private static int[] sensorData;
    //sum of all sensor values after each is multiplied by a value larger than that of the previous
    private static int numerator;
    //sum of all sensor values
    private static int denominator;
    //value from 0 to 700 representing how far right or left the sensor is over the line
    private static int linePosition;
    //the speed for the robot to adjust its angle at
    private static double turnSpeed;

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
                return linePosition;
            }
        };
        pidOutput = output -> turnSpeed = output;

        thread = new Notifier(LineSensor::updateLinePosition);
        pidSource.setPIDSourceType(PIDSourceType.kDisplacement);
        address = 9;
        wire = new I2C(Port.kOnboard, address);

        rawSensorData = new byte[16];
        sensorData = new int[8];
        turnSpeed = 0;
    }

    public static double getLinePosition() {
        return linePosition;
    }

    public static double getTurnSpeed() {
        return turnSpeed;
    }

    //start thread running once every [ms] milliseconds
    public static void startThread(int ms) {
        thread.startPeriodic((double) ms / 1000);
    }

    //stops thread from running
    public static void stopThread() {
        thread.stop();
    }

    //calculates right-left value based off of sensor values using method described in [readLine] method here: https://www.pololu.com/docs/0J19/all
    private static void updateLinePosition() {
        // wire.read(address, rawSensorData.length, rawSensorData);
        wire.readOnly(rawSensorData, rawSensorData.length);
        //store useful data from sensor in [sensorData]
        for (int i = 0; i < sensorData.length; i++)
            sensorData[i] = rawSensorData[i * 2];
        numerator = 0;
        denominator = 0;
        for (int i = 0; i < sensorData.length; i++) {
            numerator += sensorData[i] * i * 100;
            denominator += sensorData[i];
        }
        if (denominator != 0) {
            Arduino.setDiagnosticColor(3);
            Arduino.setDiagnosticPattern(numerator > lineSeenThreshold ? 0 : 1);
            linePosition = numerator / denominator;
        } else
            PrettyPrint.once("LINE SENSOR NEEDS TO BE RESET");
    }
}