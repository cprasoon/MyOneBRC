package org.demoapps;

import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

public class WeatherDataReaderParallelStreams {
    ConcurrentSkipListSet<String> names = new ConcurrentSkipListSet<>();
    ConcurrentHashMap<String, WeatherData> weatherDataHashMap = new ConcurrentHashMap<>();
    private String pathname;

    WeatherDataReaderParallelStreams(String path) {
        this.pathname = path;
    }

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        try {
            System.out.println("Please press enter once ready:");
            new Scanner(System.in).nextLine();
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
        stopWatch.start();
        new WeatherDataReaderParallelStreams("C:\\dev\\workspace\\base\\intellij\\1brcRepoClone\\1brc\\measurements.txt").readData();
        stopWatch.stop();
        System.out.println("Completed in:" + stopWatch.formatTime());
    }

    private void readData() {
        File file = new File(pathname);
        try (Stream<String> text = Files.lines(file.toPath());) {
            text.parallel().forEach(this::processData);
        } catch (IOException e) {
            throw new MyRuntimeException(e);
        } finally {
            names.forEach(x -> System.out.println(x + "=" + weatherDataHashMap.get(x).toString() + ","));
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
}