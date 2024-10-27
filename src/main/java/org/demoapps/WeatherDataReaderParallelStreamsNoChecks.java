package org.demoapps;

import org.apache.commons.lang3.time.StopWatch;

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

public class WeatherDataReaderParallelStreamsNoChecks {
    private static final Logger LOGGER = Logger.getLogger(WeatherDataReaderParallelStreamsNoChecks.class.getName());
    ConcurrentSkipListSet<String> names = new ConcurrentSkipListSet<>();
    ConcurrentHashMap<String, WeatherData> weatherDataHashMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        String filePath = "C:\\dev\\workspace\\base\\intellij\\1brcRepoClone\\1brc\\measurements.txt";
        waitForUserInput();
        Date start = new Date();
        new WeatherDataReaderParallelStreamsNoChecks().readData(filePath);
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
        try (Stream<String> text = Files.lines(new File(path).toPath())) {
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
        String[] value = str.split(";");
        WeatherData data = weatherDataHashMap.get(value[0]);
        if (data == null) {
            weatherDataHashMap.put(value[0], new WeatherData(value[0], value[1]));
            names.add(value[0]);
        } else {
            data.addReading(value[1]);
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