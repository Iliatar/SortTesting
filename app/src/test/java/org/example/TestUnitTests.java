package org.example;

import org.example.dataProvider.DataProvider;
import org.example.dataProvider.SimpleIntegerDataProvider;
import org.example.sorterUnit.BenchmarkIntegerSorter;
import org.example.sorterUnit.IntegerQuickSorter;
import org.example.sorterUnit.InvalidSorterUnit;
import org.example.sorterUnit.SorterUnit;
import org.example.testUnit.SorterUnitValidationFailedException;
import org.example.testUnit.TestItem;
import org.example.testUnit.TestUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class TestUnitTests {
    private final static int ITERATIONS_COUNT = 10;
    private final static int DATA_LENGTH = 100;

    @Test
    void testPassedTest() throws Exception {
        IntegerQuickSorter sorterUnit = new IntegerQuickSorter();
        SimpleIntegerDataProvider dataProvider = new SimpleIntegerDataProvider();
        BenchmarkIntegerSorter benchmarkSorter = new BenchmarkIntegerSorter();

        var testUnit = new TestUnit(Integer.class, sorterUnit, benchmarkSorter);
        var testItem = new TestItem(dataProvider, DATA_LENGTH, ITERATIONS_COUNT);
        testUnit.addTestItem(testItem);
        testUnit.runTest();

        Assertions.assertTrue(testUnit.isComplete());
        for (TestItem item : testUnit.getTestItemsList()) {
            Assertions.assertEquals(ITERATIONS_COUNT, item.getBenchmarkResultsArray().length);
            Assertions.assertEquals(ITERATIONS_COUNT, item.getSorterUnitResultsArray().length);
        }
    }

    @Test
    void testValidationFailed() {
        InvalidSorterUnit sorterUnit = new InvalidSorterUnit();
        SimpleIntegerDataProvider dataProvider = new SimpleIntegerDataProvider();
        BenchmarkIntegerSorter benchmarkSorter = new BenchmarkIntegerSorter();

        var testUnit = new TestUnit(Integer.class, sorterUnit, benchmarkSorter);
        var testItem = new TestItem(dataProvider, DATA_LENGTH, ITERATIONS_COUNT);
        testUnit.addTestItem(testItem);
        Assertions.assertThrowsExactly(SorterUnitValidationFailedException.class, () -> testUnit.runTest());
    }
}
