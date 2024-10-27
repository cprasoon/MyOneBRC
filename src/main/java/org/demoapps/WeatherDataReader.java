package org.demoapps;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class WeatherDataReader {
    System.Logger logger = System.getLogger(WeatherDataReaderParallelStreams.class.getName());
    SortedSet<String> names = new TreeSet<>();
    HashMap<String, WeatherData> weatherDataHashMap = new HashMap<>();

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        try {
            System.out.println("Please press enter once ready:");
            new Scanner(System.in).nextLine();
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
        stopWatch.start();
        new WeatherDataReader().readData();
        stopWatch.stop();
        System.out.println("Completed in:" + stopWatch.formatTime());
    }

    private void readData() {
        try (FileReader fileReader = new FileReader("C:\\dev\\workspace\\base\\intellij\\1brcRepoClone\\1brc\\measurements.txt");) {
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                processData(line);
            }
        } catch (IOException e) {
            throw new MyRuntimeException(e);
        } finally {
            names.forEach(x -> System.out.println(x + "=" + weatherDataHashMap.get(x).toString() + ","));
        }
    }

    private void processData(String str) {
        if (StringUtils.isNotEmpty(str)) {

            String[] value = str.split(";");
            String stationName = value[0];
            String currentReading = value[1];
            if (null != stationName && null !=currentReading) {
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