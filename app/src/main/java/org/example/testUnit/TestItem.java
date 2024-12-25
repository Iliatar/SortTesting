package org.example.testUnit;

import org.example.dataProvider.DataProvider;
import org.example.sorterUnit.SorterUnit;

import java.util.Arrays;

public class TestItem {
    private final Object dataProvider;
    private final int dataLength;
    private final int iterationsCount;
    private long[] benchmarkResultsArray;
    private long[] sorterUnitResultsArray;

    public TestItem(Object dataProvider, int dataLength, int iterationsCount) {
        this.dataProvider = dataProvider;
        this.dataLength = dataLength;
        this.iterationsCount = iterationsCount;
    }

    public void initialize() {
        benchmarkResultsArray = new long[iterationsCount];
        sorterUnitResultsArray = new long[iterationsCount];
    }

    public void close() {
        Arrays.sort(benchmarkResultsArray);
        Arrays.sort(sorterUnitResultsArray);
    }

    public void setBenchmarkResult(int index, long result) {
        benchmarkResultsArray[index] = result;
    }

    public void setSorterUnitResult(int index, long result) {
        sorterUnitResultsArray[index] = result;
    }

    public Object getDataProvider() {
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
