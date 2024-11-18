package org.example;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;

public class TestUnit {
    public <K> void test(SorterUnit<K> sorterUnit, SorterUnit<K> benchmarkSorter, DataProvider<K> dataProvider, int dataLength, int iterationsCount) {
        if(!validateSorterUnit(sorterUnit, benchmarkSorter, dataProvider, dataLength)) {
            System.out.println("Tested sorter unit result don't match benchmark unit result!");
            return;
        }

        Long[] benchmarkResultsArray = new Long[iterationsCount];
        Long[] sorterUnitResultsArray = new Long[iterationsCount];

        for (int iterationNum = 1; iterationNum <= iterationsCount; iterationNum++) {
            K[] data = dataProvider.getData(dataLength);
            sorterUnitResultsArray[iterationNum - 1] = runTest(data, sorterUnit);
            benchmarkResultsArray[iterationNum - 1] = runTest(data, benchmarkSorter);
        }

        Arrays.sort(sorterUnitResultsArray);
        Arrays.sort(benchmarkResultsArray);

        StringBuilder resultInfoBuilder = new StringBuilder();
        resultInfoBuilder.append("TEST INFO\nSorter unit: ");
        resultInfoBuilder.append(sorterUnit.getClass().getName());
        resultInfoBuilder.append("\nBenchmark unit: ");
        resultInfoBuilder.append(benchmarkSorter.getClass().getName());
        resultInfoBuilder.append("\nData Provider Unit: ");
        resultInfoBuilder.append(dataProvider.getClass().getName());
        resultInfoBuilder.append("\nData length: ");
        resultInfoBuilder.append(dataLength);
        resultInfoBuilder.append("\nIterations count: ");
        resultInfoBuilder.append(iterationsCount);
        resultInfoBuilder.append("\nMedian result (millis): ");
        resultInfoBuilder.append(sorterUnitResultsArray[iterationsCount / 2]);
        resultInfoBuilder.append(" (benchmark is ");
        resultInfoBuilder.append(benchmarkResultsArray[iterationsCount / 2]);
        resultInfoBuilder.append(")\n");
        System.out.println(resultInfoBuilder.toString());
    }

    private <K> long runTest(K[] data, SorterUnit<K> sorterUnit) {
        data = Arrays.copyOf(data, data.length);
        Instant startTime = Instant.now();
        sorterUnit.sort(data);
        Instant endTime = Instant.now();
        return Duration.between(startTime, endTime).toNanos();
    }

    private <K> boolean validateSorterUnit(SorterUnit<K> validatedUnit, SorterUnit<K> benchmarkUnit, DataProvider<K> dataProvider, int dataLength) {
        K[] data = dataProvider.getData(dataLength);
        K[] sortedValidatedData = validatedUnit.sort(data.clone());
        K[] sortedBenchmarkData = benchmarkUnit.sort(data.clone());

        if (sortedBenchmarkData.length != sortedValidatedData.length) {
            return false;
        }

        for(int i = 0; i < sortedBenchmarkData.length; i++) {
            if(sortedBenchmarkData[i] != sortedValidatedData[i]) {
                return false;
            }
        }

        return true;
    }
}
