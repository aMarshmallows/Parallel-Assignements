import java.util.*;

public class MarsRoverTemperature {
    private static final int NUM_SENSORS = 8;
    private static final int NUM_MINUTES = 60;
    private static final int TIME_COMPRESSION = 1; // 1000 -> 1 hour will take 60 minutes, 1 -> 1 hour will take 3.6
                                                   // seconds

    private static final float MIN_TEMP = -100f;
    private static final float MAX_TEMP = 70f;

    private static final int REPORT_INTERVAL = 60 * 60 * TIME_COMPRESSION; // 1 hour in milliseconds

    private static float[][] temperatures = new float[NUM_SENSORS][NUM_MINUTES];

    public static void main(String[] args) {
        // Start temperature sensor threads
        for (int i = 0; i < NUM_SENSORS; i++) {
            new TemperatureSensor(i).start();
        }

        // Start report thread
        System.out.println("in main!");
        new ReportGenerator().start();
    }

    static class TemperatureSensor extends Thread {
        private int sensorId;

        TemperatureSensor(int sensorId) {
            this.sensorId = sensorId;
        }

        @Override
        public void run() {
            while (true) {
                float temperature = getRandomTemperature();
                int minute = getCurrentMinute();
                synchronized (temperatures) {
                    temperatures[sensorId][minute] = temperature;
                }
                try {
                    sleep(TIME_COMPRESSION * 60);
                } catch (InterruptedException e) {
                    System.out.println("sleep did not work");
                    e.printStackTrace();
                } // sleep for 1 minute IF TIME_COMPRESSION set to 1000
            }
        }

        private float getRandomTemperature() {
            return MIN_TEMP + new Random().nextFloat() * (MAX_TEMP - MIN_TEMP);
        }

        private int getCurrentMinute() {
            return (int) (System.currentTimeMillis() / (1000 * 60)) % NUM_MINUTES;
        }
    }

    static class ReportGenerator extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(REPORT_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                float[] maxTemperatures = new float[NUM_SENSORS];
                float[] minTemperatures = new float[NUM_SENSORS];
                float maxDiff = 0;
                int maxDiffStartMinute = 0;

                synchronized (temperatures) {
                    for (int minute = 0; minute < NUM_MINUTES; minute++) {
                        float[] minuteTemperatures = new float[NUM_SENSORS];
                        for (int sensorId = 0; sensorId < NUM_SENSORS; sensorId++) {
                            float temperature = temperatures[sensorId][minute];
                            minuteTemperatures[sensorId] = temperature;
                            if (temperature > maxTemperatures[sensorId]) {
                                maxTemperatures[sensorId] = temperature;
                            }
                            if (temperature < minTemperatures[sensorId]) {
                                minTemperatures[sensorId] = temperature;
                            }
                        }

                        float minuteMax = Float.NEGATIVE_INFINITY;
                        for (float temp : minuteTemperatures) {
                            if (temp > minuteMax) {
                                minuteMax = temp;
                            }
                        }

                        float minuteMin = Float.POSITIVE_INFINITY;
                        for (float temp : minuteTemperatures) {
                            if (temp < minuteMin) {
                                minuteMin = temp;
                            }
                        }
                        float diff = Math.abs(minuteMax - minuteMin);
                        if (diff > maxDiff) {
                            maxDiff = diff;
                            maxDiffStartMinute = minute;
                        }
                    }
                }

                System.out.println("Top 5 highest temperatures: " + Arrays.toString(getTopN(maxTemperatures, 5)));
                System.out.println("Top 5 lowest temperatures: " + Arrays.toString(getTopN(minTemperatures, 5)));
                System.out.println("Largest temperature difference of " + maxDiff + " observed between minutes "
                        + maxDiffStartMinute + " and " + (maxDiffStartMinute + 10));
            }
        }

        private float[] getTopN(float[] arr, int n) {
            float[] topN = new float[n];
            Arrays.fill(topN, Float.NEGATIVE_INFINITY);
            for (float x : arr) {
                for (int i = 0; i < n; i++) {
                    if (x > topN[i]) {
                        for (int j = n - 1; j > i; j--) {
                            topN[j] = topN[j - 1];
                        }
                        topN[i] = x;
                        break;
                    }
                }
            }
            return topN;
        }
    }
}
