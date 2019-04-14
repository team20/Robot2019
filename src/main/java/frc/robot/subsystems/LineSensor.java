package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.I2C.Port;

public class LineSensor {
    public static final PIDController linePid;
    private static final PIDSource pidSource;
    private static final PIDOutput pidOutput;
    // separate thread for data collection and calculations
    private static final Notifier thread;
    // I2C communication protocol
    private static final I2C multiplexWire;
    private static final I2C sensorWire;

    // threshold for whether lineSeen is true or not
    private static final int lineSeenThreshold = 400;
    // number of line sensor modules being used
    private static final int numModules = 2;
    // // should the right/left value be reversed?
    // private static final boolean reversed = false;

    // the line sensor's I2C address is hard-coded into the board as 9 and cannot be changed
    private static final byte multiplexAddress;
    private static final byte sensorAddress;
    // raw data from the sensor
    private static byte[][] rawSensorData;
    // improved format of the data that is used in the calculations
    private static byte[] sensorData;
    // sum of all sensor values after each is multiplied by a value larger than that of the previous
    private static int weightedTotal;
    // sum of all sensor values
    private static int total;
    // value representing how far right or left the sensor is over the line (values range from [minValue] to [maxValue])
    private static int linePosition;
    // the speed for the robot to adjust its angle at
    private static double turnSpeed;
    // maximum value for line position
    private static int maxValue;

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

        maxValue = 800 * numModules - 100;

        linePid = new PIDController(0.001, 0.0, 0.0, LineSensor.pidSource, LineSensor.pidOutput);
        linePid.setInputRange(maxValue, 0);
        linePid.setOutputRange(-1, 1);
        linePid.setContinuous(false);
        linePid.setSetpoint(maxValue / 2);

        thread = new Notifier(() -> {
            getSensorData();
            calculateLinePosition();
        });
        multiplexAddress = 0x70;
        multiplexWire = new I2C(Port.kOnboard, multiplexAddress);
        sensorAddress = 0x9;
        sensorWire = new I2C(Port.kOnboard, sensorAddress);

        rawSensorData = new byte[numModules][16];
        sensorData = new byte[8 * numModules];
        turnSpeed = 0;
    }

    //read sensor data via I2C
    private static void getSensorData() {
        // get data from sensor
        for (int i = 0; i < numModules; i++) {
            multiplexWire.writeBulk(new byte[] { (byte) (1 << i) });
            sensorWire.readOnly(rawSensorData[i], rawSensorData[i].length);
        }
        //make data easier to use by putting it all in one array and getting rid of useless values
        for (int i = 0; i < numModules; i++)
            for (int j = 0; j < 8; j++)
                sensorData[sensorData.length * i / numModules + j] = rawSensorData[i][j * 2];
    }

    //calculates right-left value based off of sensor values using method described in [readLine] method here: https://www.pololu.com/docs/0J19/all (it's about halfway down the page)
    public static void calculateLinePosition() {
        weightedTotal = 0;
        total = 0;
        for (int i = 0; i < sensorData.length; i++) {
            weightedTotal += sensorData[i] * i * 100;
            total += sensorData[i];
        }
        if (total != 0)
            linePosition = weightedTotal / total;
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