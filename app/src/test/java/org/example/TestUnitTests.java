package org.example;

import org.example.outputGenerators.BasicOutputGenerator;
import org.example.outputGenerators.OutputGenerator;
import org.example.outputUnit.ConsoleOutputUnit;
import org.example.outputUnit.OutputUnit;
import org.example.outputUnit.TextFileOutputUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUnitTests {
    private final static int ITERATIONS_COUNT = 10;
    private final static int DATA_LENGTH = 1000;
    @Test
    void testPassedTest() {
        SorterUnit<Integer> sorterUnit = new IntegerQuickSorter();
        DataProvider<Integer> dataProvider = new SimpleIntegerDataProvider();
        SorterUnit<Integer> benchmarkSorter = new BenchmarkIntegerSorter();

        var testUnit = new TestUnit<>(sorterUnit, benchmarkSorter,
                dataProvider, DATA_LENGTH, ITERATIONS_COUNT);
        testUnit.test();

        Assertions.assertTrue(testUnit.isComplete());
        Assertions.assertEquals(ITERATIONS_COUNT, testUnit.getBenchmarkResultsArray().length);
        Assertions.assertEquals(ITERATIONS_COUNT, testUnit.getSorterUnitResultsArray().length);
    }
}