package nu.fml.monkeymondayz;

/**
 * Created by Hawry on 2015-04-11.
 */
public class Constants {
    public static String AVAILABLE_SENSORS = "sensors";

    public static String PREF_ACCELEROMETER = "accelerometer";
    public static String PREF_GYROSCOPE = "gyroscope";
    public static String PREF_LIGHT = "light";
    public static String PREF_MICROPHONE = "microphone";
    public static String PREF_LOCATION_GPS = "gps";
    public static String PREF_LOCATION_NETWORK = "network";
    public static String PREF_INTERNET = "internet";

    private static Constants ourInstance = new Constants();

    public static Constants getInstance() {
        return ourInstance;
    }

    private Constants() {
    }
}
