package org.demoapps;

import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

public class WeatherDataReaderParallelStreamsNoChecks {
    ConcurrentSkipListSet<String> names = new ConcurrentSkipListSet<>();
    ConcurrentHashMap<String, WeatherData> weatherDataHashMap = new ConcurrentHashMap<>();
    private String pathname;

    WeatherDataReaderParallelStreamsNoChecks(String path) {
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
        new WeatherDataReaderParallelStreamsNoChecks("C:\\dev\\workspace\\base\\intellij\\1brcRepoClone\\1brc\\measurements.txt").readData();
        stopWatch.stop();
        System.out.println("Completed in:" + stopWatch.formatTime());
    }

    private void readData() {
        try (Stream<String> text = Files.lines(new File(pathname).toPath());) {
            text.parallel().forEach(this::processData);
        } catch (IOException e) {
            throw new MyRuntimeException(e);
        } finally {
            names.forEach(x -> System.out.println(x + "=" + weatherDataHashMap.get(x).toString() + ","));
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
}