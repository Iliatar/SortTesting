/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import java.time.Instant;
import java.util.List;

public class App {
    public final static int OBJECTS_COUNT = 10000;

    public static void main(String[] args) {
        SorterUnit<Integer> sorterUnit = new IntegerQuickSorter();
        DataProvider<Integer> dataProvider = new SimpleIntegerDataProvider();
        SorterUnit<Integer> benchmarkSorter = new BenchmarkIntegerSorter();
        TestUnit testUnit = new TestUnit();

        testUnit.test(sorterUnit, benchmarkSorter, dataProvider, 10000, 500);
        testUnit.test(benchmarkSorter, benchmarkSorter, dataProvider, 10000, 500);
        testUnit.test(benchmarkSorter, sorterUnit, dataProvider, 10000, 500);
    }
}
