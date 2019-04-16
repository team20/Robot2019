package frc.robot.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.lang.Math.min;

/**
 * <p>Utility class to make logging things to the Driver Station easier</p>
 * <p>Usage:</p>
 * <pre><code>
 * autonomousInit() {
 *     PrettyPrint.once("Outputted once");
 *     PrettyPrint.put("Navx angle", () -> navx.getYaw()); //OR
 *     PrettyPrint.put("Other way", navx::getYaw); //OR (in periodic)
 * }
 *
 * autonomousPeriodic() {
 *     //OR:
 *     PrettyPrint.put("constant", navx.getYaw());
 * }
 *
 * disabledInit() {
 *     PrettyPrint.removeAll();
 * }
 *
 * robotPeriodic() {
 *     PrettyPrint.print();
 * }
 * </code></pre>
 *
 * <p>Output looks like this:</p>
 * <p>| Navx angle = 180 | Other way = 180 | constant = 180 | Outputted once |</p>
 * <p>| Navx angle = 170 | Other way = 170 | constant = 170 |</p>
 * <p>| Navx angle = 160 | Other way = 160 | constant = 160 |</p>
 * <p>| Navx angle = 150 | Other way = 150 | constant = 150 |</p>
 * <p>| Navx angle = 140 | Other way = 140 | constant = 140 |</p>
 * <p>| Navx angle = 130 | Other way = 130 | constant = 130 |</p>
 * <p>| Navx angle = 120 | Other way = 120 | constant = 120 |</p>
 */
public class PrettyPrint {
    private static int messageLength = 7;
    private static int frequency = 1;
    private static int count = 0;

    private static LinkedHashMap<String, Supplier<Object>> values = new LinkedHashMap<>(); // Linked to preserve order
    private static LinkedHashMap<String, Object> tempValues = new LinkedHashMap<>();
    private static ArrayList<String> tempMessages = new ArrayList<>();
    private static ArrayList<String> errors = new ArrayList<>();

    /**
     * store a {@code message} and expression for a value that will be prettyprinted
     *
     * @param value supplier of a value of any type. use either <code>someObj::someMethod</code> or <code>() -> someObj.someMethod()</code>
     */
    public static void put(String message, Supplier<Object> value) {
        values.put(message, value);
    }

    /**
     * store a {@code message} and {@code value} to be prettyprinted
     *
     * @param value constant value of any type
     */
    public static void put(String message, Object value) {
        tempValues.put(message, value);
    }

    /**
     * append the message at the end of prettyprint table only once
     */
    public static void once(String message) {
        tempMessages.add(message);
    }

    /**
     * print an error message in an easily viewable way
     * <p>
     * CURRENTLY THIS THROWS AN ERROR DON'T USE
     */
    public static void error(String errMessage) {
        errors.add(errMessage);
    }

    /**
     * stops a message from being printed
     */
    public static void remove(String message) {
        values.remove(message);
    }

    /**
     * stops multiple messages from being printed
     */
    public static void remove(String... messages) {
        for (String message : messages) values.remove(message);
    }

    /**
     * stops all messages from being printed. good to call in an init method
     */
    public static void removeAll() {
        values.clear();
    }

    /**
     * values will be printed once every <i>frequency</i> iterations. default is 1. set to a higher value to prevent spam, at the cost of missing some data
     */
    public static void setFrequency(int frequency) {
        PrettyPrint.frequency = frequency;
    }

    /**
     * how many characters wide each value will be
     * default is 1
     */
    public static void setMessageLength(int messageLength) {
        PrettyPrint.messageLength = messageLength;
    }

    /**
     * called to print all stored messages/values. must be called in a periodic mode
     */
    public static void print() {
        if (values.isEmpty() && tempMessages.isEmpty() && errors.isEmpty() && tempValues.isEmpty()) return;

        // are iterator loops for thread safety
        for (Iterator<String> iterator = errors.iterator(); iterator.hasNext(); ) {
            String errorMessage = iterator.next();
            for (int i = 0; i < 70; i++) System.out.print("-");
            System.out.println();
            System.out.println();
            System.out.println(errorMessage);
            for (int i = 0; i < 70; i++) System.out.print("-");
            System.out.println();
        }

        if (++count != frequency) return;
        count = 0;

        System.out.print("|");
        // are iterator loops for thread safety
        for (Iterator<Map.Entry<String, Supplier<Object>>> iterator = values.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Supplier<Object>> entry = iterator.next();
            String key = entry.getKey();
            Supplier<Object> valSup = entry.getValue();
            System.out.printf(" %s = %-" + messageLength + "s |", key, shortened(valSup.get()));
        }
        for (Iterator<Map.Entry<String, Object>> iterator = tempValues.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            String str = entry.getKey();
            Object val = entry.getValue();
            System.out.printf(" %s = %-" + messageLength + "s |", str, shortened(val));
        }
        for (Iterator<String> iterator = tempMessages.iterator(); iterator.hasNext(); ) {
            String message = iterator.next();
            System.out.printf(" %s |", message);
        }
        System.out.println();

        tempValues.clear();
        tempMessages.clear();
        errors.clear();
    }

    /**
     * utility method to ensure that each value is the same num of characters to maintain table's vertical straightness
     */
    private static String shortened(Object val) {
        String valStr = val.toString();
        return valStr.substring(0, min(valStr.length(), messageLength));
    }
}