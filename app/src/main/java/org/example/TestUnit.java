package org.example;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;

public class TestUnit<K> {
    private static final double MEDIAN_COEFF = 0.5;
    private static final double TOP_COEFF = 0.1;
    private static final double BOTTOM_COEFF = 0.9;

    private static final double MEDIAN_WEIGHT = 0.8;
    private static final double TOP_WEIGHT = 0.1;
    private static final double BOTTOM_WEIGHT = 0.1;

    private static final double NANOS_TO_MILLIS_COEFF = 1000000;

    private final SorterUnit<K> sorterUnit;
    private final SorterUnit<K> benchmarkUnit;
    private final DataProvider<K> dataProvider;
    private final int dataLength;
    private final int iterationsCount;
    private long[] benchmarkResultsArray;
    private long[] sorterUnitResultsArray;

    public TestUnit(SorterUnit<K> sorterUnit, SorterUnit<K> benchmarkUnit,
                        DataProvider<K> dataProvider, int dataLength, int iterationsCount) {
        this.sorterUnit = sorterUnit;
        this.benchmarkUnit = benchmarkUnit;
        this.dataProvider = dataProvider;
        this.dataLength = dataLength;
        this.iterationsCount = iterationsCount;


    }

    public void test() {
        if(!validateSorterUnit(sorterUnit, benchmarkUnit, dataProvider, dataLength)) {
            System.out.println("Tested sorter unit (" + sorterUnit.getClass().getName() + ") result don't match benchmark unit result!");
            return;
        }

        benchmarkResultsArray = new long[iterationsCount];
        sorterUnitResultsArray = new long[iterationsCount];

        for (int iterationNum = 1; iterationNum <= iterationsCount; iterationNum++) {
            K[] data = dataProvider.getData(dataLength);
            sorterUnitResultsArray[iterationNum - 1] = runTest(data, sorterUnit);
            benchmarkResultsArray[iterationNum - 1] = runTest(data, benchmarkUnit);
        }

        Arrays.sort(sorterUnitResultsArray);
        Arrays.sort(benchmarkResultsArray);

        StringBuilder resultInfoBuilder = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.###");
        double medianUnitResult = (double)(sorterUnitResultsArray[(int)(iterationsCount * MEDIAN_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double medianBenchmarkResult = (double)(benchmarkResultsArray[(int)(iterationsCount * MEDIAN_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double topUnitResult = (double)(sorterUnitResultsArray[(int)(iterationsCount * TOP_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double topBenchmarkResult = (double)(benchmarkResultsArray[(int)(iterationsCount * TOP_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double bottomUnitResult = (double)(sorterUnitResultsArray[(int)(iterationsCount * BOTTOM_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double bottomBenchmarkResult = (double)(benchmarkResultsArray[(int)(iterationsCount * BOTTOM_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double benchmarkEfficiency = (medianBenchmarkResult / medianUnitResult) * MEDIAN_WEIGHT
                + (topBenchmarkResult / topUnitResult) * TOP_WEIGHT
                + (bottomBenchmarkResult / bottomUnitResult) * BOTTOM_WEIGHT;
        benchmarkEfficiency *= 1000;

        resultInfoBuilder.append("TEST INFO\nSorter unit: ");
        resultInfoBuilder.append(sorterUnit.getClass().getName());
        resultInfoBuilder.append("\nBenchmark unit: ");
        resultInfoBuilder.append(benchmarkUnit.getClass().getName());
        resultInfoBuilder.append("\nData Provider Unit: ");
        resultInfoBuilder.append(dataProvider.getClass().getName());
        resultInfoBuilder.append("\nData length: ");
        resultInfoBuilder.append(dataLength);
        resultInfoBuilder.append("\nIterations count: ");
        resultInfoBuilder.append(iterationsCount);

        resultInfoBuilder.append("\nMedian result (millis): ");
        resultInfoBuilder.append(df.format(medianUnitResult));
        resultInfoBuilder.append(" (benchmark is ");
        resultInfoBuilder.append(df.format(medianBenchmarkResult));
        resultInfoBuilder.append(")");

        resultInfoBuilder.append("\nTop 10% result (millis): ");
        resultInfoBuilder.append(df.format(topUnitResult));
        resultInfoBuilder.append(" (benchmark is ");
        resultInfoBuilder.append(df.format(topBenchmarkResult));
        resultInfoBuilder.append(")");

        resultInfoBuilder.append("\nBottom 10% result (millis): ");
        resultInfoBuilder.append(df.format(bottomUnitResult));
        resultInfoBuilder.append(" (benchmark is ");
        resultInfoBuilder.append(df.format(bottomBenchmarkResult));
        resultInfoBuilder.append(")");

        resultInfoBuilder.append("\nBenchmark efficiency is ");
        resultInfoBuilder.append(df.format(benchmarkEfficiency));
        resultInfoBuilder.append("\n");
        System.out.println(resultInfoBuilder);
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

    public SorterUnit<K> getSorterUnit() {
        return sorterUnit;
    }

    public SorterUnit<K> getBenchmarkUnit() {
        return benchmarkUnit;
    }

    public DataProvider<K> getDataProvider() {
        return dataProvider;
    }

    public int getDataLength() {
        return dataLength;
    }

    public int getIterationsCount() {
        return iterationsCount;
    }

    public long[] getBenchmarkResultsArray() {
        return benchmarkResultsArray;
    }

    public long[] getSorterUnitResultsArray() {
        return sorterUnitResultsArray;
    }
}
