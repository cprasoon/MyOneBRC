package org.demoapps;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherDataReader {
    private static final Logger LOGGER = Logger.getLogger(WeatherDataReader.class.getName());
    SortedSet<String> names = new TreeSet<>();
    HashMap<String, WeatherData> weatherDataHashMap = new HashMap<>();

    public static void main(String[] args) {
        String filePath = "C:\\dev\\workspace\\base\\intellij\\1brcRepoClone\\1brc\\measurements.txt";
        waitForUserInput();
        Date start = new Date();
        new WeatherDataReader().readData(filePath);
        Date end = new Date();
        printTimeDiff(start, end);
    }

    private static void waitForUserInput() {
        try {
            LOGGER.log(Level.INFO, "Please press enter once ready:");
            new Scanner(System.in).nextLine();
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
    }

    private void readData(String path) {
        try (FileReader fileReader = new FileReader(path)) {
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                processData(line);
            }
        } catch (IOException e) {
            throw new MyRuntimeException(e);
        } finally {
            List<String> listOut = names.stream().map(x -> String.format("%s=%s", x, weatherDataHashMap.get(x).toString())).toList();
            String output = listOut.toString();
            LOGGER.log(Level.INFO, output);
        }
    }

    private void processData(String str) {
        if (StringUtils.isNotEmpty(str)) {

            String[] value = str.split(";");
            String stationName = value[0];
            String currentReading = value[1];
            if (null != stationName && null != currentReading) {
                WeatherData data = weatherDataHashMap.get(stationName);
                if (data == null) {
                    weatherDataHashMap.put(stationName, new WeatherData(stationName, currentReading));
                    names.add(stationName);
                } else {
                    data.addReading(currentReading);
                }
            }
        }
    }

    private static void printTimeDiff(Date start, Date end) {
        Duration duration = Duration.between(start.toInstant(), end.toInstant());
        String output = String.format("%02d:%02d:%02d.%03d",
                duration.toHoursPart(),
                duration.toMinutesPart(),
                duration.toSecondsPart(),
                duration.toMillisPart()
        );
        LOGGER.log(Level.INFO, output);
    }
}