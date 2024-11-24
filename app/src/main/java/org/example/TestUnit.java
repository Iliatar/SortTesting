package org.example;

import org.example.outputGenerators.BasicOutputGenerator;
import org.example.outputGenerators.OutputGenerator;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;

public class TestUnit<K> {
    private static final int VALIDATOR_ITERATIONS_COUNT = 10;

    private final SorterUnit<K> sorterUnit;
    private final SorterUnit<K> benchmarkUnit;
    private final DataProvider<K> dataProvider;
    private final int dataLength;
    private final int iterationsCount;
    private final OutputGenerator outputGenerator;
    private long[] benchmarkResultsArray;
    private long[] sorterUnitResultsArray;

    public TestUnit(SorterUnit<K> sorterUnit, SorterUnit<K> benchmarkUnit,
                    OutputGenerator outputGenerator, DataProvider<K> dataProvider,
                    int dataLength, int iterationsCount) {
        this.sorterUnit = sorterUnit;
        this.benchmarkUnit = benchmarkUnit;
        this.outputGenerator = outputGenerator;
        this.dataProvider = dataProvider;
        this.dataLength = dataLength;
        this.iterationsCount = iterationsCount;
    }

    public void test() {
        if(SorterValidator.checkSorterUnitsResultEquals(sorterUnit, benchmarkUnit,
                dataProvider, VALIDATOR_ITERATIONS_COUNT, dataLength)) {
            System.out.println("Tested sorter unit (" + sorterUnit.getClass().getName() + ") validation complete!");
        } else {
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
        
        System.out.println(outputGenerator.generateOutput(this));
    }

    private <K> long runTest(K[] data, SorterUnit<K> sorterUnit) {
        data = Arrays.copyOf(data, data.length);
        Instant startTime = Instant.now();
        sorterUnit.sort(data);
        Instant endTime = Instant.now();
        return Duration.between(startTime, endTime).toNanos();
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
