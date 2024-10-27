package org.demoapps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class WeatherDataReaderParallelStreams {
    private static final Logger LOGGER = Logger.getLogger(WeatherDataReaderParallelStreams.class.getName());
    ConcurrentSkipListSet<String> names = new ConcurrentSkipListSet<>();
    ConcurrentHashMap<String, WeatherData> weatherDataHashMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        String filePath = "C:\\dev\\workspace\\base\\intellij\\1brcRepoClone\\1brc\\measurements.txt";
        waitForUserInput();
        Date start = new Date();
        new WeatherDataReaderParallelStreams().readData(filePath);
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
        File file = new File(path);
        try (Stream<String> text = Files.lines(file.toPath())) {
            text.parallel().forEach(this::processData);
        } catch (IOException e) {
            throw new MyRuntimeException(e);
        } finally {
            List<String> listOut = names.stream().map(x -> String.format("%s=%s", x, weatherDataHashMap.get(x).toString())).toList();
            String output = listOut.toString();
            LOGGER.log(Level.INFO, output);
        }
    }

    private void processData(String str) {
        if (null != str && !str.isEmpty()) {
            String[] value = str.split(";");
            if (value.length == 2) {
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