package frc.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.I2C.Port;

public class LineSensor {
    public static final PIDController linePid;
    public static final PIDSource pidSource;
    public static final PIDOutput pidOutput;
    // separate thread for data collection and calculations
    private static final Notifier thread;
    // I2C communication protocol
    private static final I2C wire;

    // threshold for whether lineSeen is true or not
    private static final int lineSeenThreshold = 30;    //TODO: needs to be changed
    // number of line sensor modules being used
    private static final int numModules = 2;

    // the line sensor's I2C address is hard-coded into the board as 9 and cannot be
    // changed
    private static final int address;
    // raw data from the sensor
    private static byte[][] rawSensorData;
    // improved format of the data that is used in the calculations
    private static int[] sensorData;
    // sum of all sensor values after each is multiplied by a value larger than that
    // of the previous
    private static int weightedTotal;
    // sum of all sensor values
    private static int total;
    // value from 0 to 700 representing how far right or left the sensor is over the
    // line
    private static int linePosition;
    // the speed for the robot to adjust its angle at
    private static double turnSpeed;
    // minimum and maximum values for line position
    private static int maxValue;
    private static int minValue;

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
        pidSource.setPIDSourceType(PIDSourceType.kDisplacement);

        pidOutput = output -> turnSpeed = output;

        // linePid = new PIDController(0.001, 0.0, 0.0, LineSensor.pidSource, LineSensor.pidOutput);
        // linePid.setInputRange(0, 700 * numModules);
        // linePid.setOutputRange(-1, 1);
        // linePid.setContinuous(false);
        // linePid.setSetpoint((700 * numModules) / 2);
        maxValue = 400 * numModules - 50;
        minValue = -400 * numModules + 50;

        linePid = new PIDController(0.001, 0.0, 0.0, LineSensor.pidSource, LineSensor.pidOutput);
        linePid.setInputRange(minValue, maxValue);
        linePid.setOutputRange(-1, 1);
        linePid.setContinuous(false);
        linePid.setSetpoint(0);

        thread = new Notifier(() -> {
            getSensorData();
            calculateLinePosition();
        });
        address = 0x70;
        wire = new I2C(Port.kOnboard, address);

        rawSensorData = new byte[numModules][16];
        sensorData = new int[8 * numModules];
        turnSpeed = 0;
    }

    /**
     * send a read sensor data via I2C
     */
    private static void getSensorData() {
        // get data from sensor
        for (int i = 0; i < numModules; i++) {
            wire.writeBulk(new byte[] { (byte) i });
            wire.readOnly(rawSensorData[i], rawSensorData[i].length);
        }
        //make data easier to use by putting it all in one array and getting rid of useless values
        for (int i = 0; i < numModules; i++)
            for (int j = 0; j < sensorData.length; j++) {
                if (i == 0)
                    sensorData[j] = rawSensorData[i][j * 2];
                else
                    sensorData[sensorData.length * i + j - 1] = rawSensorData[i][j * 2];
            }
    }

    /**
     * calculates right-left value based off of sensor values using method described
     * in [readLine] method here: https://www.pololu.com/docs/0J19/all (it's about
     * halfway down the page)
     */
    public static void calculateLinePosition() {
        weightedTotal = 0;
        total = 0;
        for (int i = 0; i < sensorData.length; i++) {
            weightedTotal += sensorData[i] * i * 100;
            total += sensorData[i];
        }
        if (total != 0)
            linePosition = weightedTotal / total - maxValue;
    }

    public static int getTotal() {
        return total;
    }

    public static boolean isLineSeen() {
        return total > lineSeenThreshold;
    }

    public static boolean isBroken() {
        return total == 0;
    }

    public static int getLinePosition() {
        return linePosition;
    }

    public static double getTurnSpeed() {
        return turnSpeed;
    }

    /**
     * start thread running
     */
    public static void startThread() {
        thread.startPeriodic(0.05);
    }

    /**
     * stops thread from running
     */
    public static void stopThread() {
        thread.stop();
    }
}