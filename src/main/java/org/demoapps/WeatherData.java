package org.demoapps;

public class WeatherData {
    public static final String SEPARATOR = "/";
    public static final String EQUAL = "=";
    String name;
    float min;
    float max;
    float total;
    int num;

    WeatherData(String name, float reading) {
        this.name = name;
        this.min = reading;
        this.max = reading;
        this.total = reading;
        this.num = 1;
    }

    WeatherData(String name, String reading) {
        this.name = name;
        float value = Float.parseFloat(reading);
        this.min = value;
        this.max = value;
        this.total = value;
        this.num = 1;
    }

    public void addReading(String value) {
        float reading = Float.parseFloat(value);
        min = reading < min ? reading : min;
        max = reading > max ? reading : max;
        total = total + reading;
        num++;
    }

    public String getSummary() {
        return min + SEPARATOR + String.format("%.1f", total / num) + SEPARATOR + max;
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
