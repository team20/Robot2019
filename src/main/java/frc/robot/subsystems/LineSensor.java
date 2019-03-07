package frc.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.I2C.Port;
import frc.robot.utils.PrettyPrint;

public class LineSensor {
    public static final PIDController linePid;
    public static final PIDSource pidSource;
    public static final PIDOutput pidOutput;
    //separate thread for data collection and calculations
    private static final Notifier thread;
    //I2C communication protocol
    private static final I2C wire;

    //threshold for whether lineSeen is true or not
    private static final int lineSeenThreshold = 20;

    //the line sensor's I2C address is hard-coded into the board as 9 and cannot be changed
    private static final int address;
    //raw data from the sensor
    private static byte[] rawSensorData;
    //improved format of the data that is used in the calculations
    private static int[] sensorData;
    //sum of all sensor values after each is multiplied by a value larger than that of the previous
    private static int weightedTotal;
    //sum of all sensor values
    private static int total;
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
        pidSource.setPIDSourceType(PIDSourceType.kDisplacement);

        linePid = new PIDController(0.001, 0, 0, LineSensor.pidSource, LineSensor.pidOutput);
        linePid.setSetpoint(350);

        thread = new Notifier(LineSensor::updateLinePosition);
        address = 9;
        wire = new I2C(Port.kOnboard, address);

        rawSensorData = new byte[16];
        sensorData = new int[8];
        turnSpeed = 0;
    }

    public static boolean isBroken() {
        return total == 0;
    }

    public static boolean isLineSeen() {
        return total > lineSeenThreshold;
    }

    public static double getLinePosition() {
        return linePosition;
    }

    public static int getTotal() {
        return total;
    }

    public static double getTurnSpeed() {
        return turnSpeed;
    }

    //start thread running
    public static void startThread() {
        thread.startPeriodic(0.15);
    }

    //stops thread from running
    public static void stopThread() {
        thread.stop();
    }

    //calculates right-left value based off of sensor values using method described in [readLine] method here: https://www.pololu.com/docs/0J19/all
    private static void updateLinePosition() {
        wire.readOnly(rawSensorData, rawSensorData.length);
        //store useful data from sensor in [sensorData]
        for (int i = 0; i < sensorData.length; i++)
            sensorData[i] = rawSensorData[i * 2];
        weightedTotal = 0;
        total = 0;
        for (int i = 0; i < sensorData.length; i++) {
            weightedTotal += sensorData[i] * i * 100;
            total += sensorData[i];
        }
        if (total != 0)
            linePosition = weightedTotal / total;
         for (int data : sensorData)
            System.out.print(data + "\t");
         System.out.println();

    }
}