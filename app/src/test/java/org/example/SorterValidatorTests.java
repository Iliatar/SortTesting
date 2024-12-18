package org.example;

import org.example.dataProvider.DataProvider;
import org.example.dataProvider.SimpleIntegerDataProvider;
import org.example.sorterUnit.BenchmarkIntegerSorter;
import org.example.sorterUnit.SorterUnit;
import org.example.utils.SorterValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SorterValidatorTests {
    private final static int ITERATIONS_COUNT = 10;
    private final static int DATA_LENGTH = 100;

    @Test
    void validationPassedTest() {
        SorterUnit<Integer> benchmarkSorterUnit = new BenchmarkIntegerSorter();
        DataProvider<Integer> dataProvider = new SimpleIntegerDataProvider();
        Assertions.assertTrue(SorterValidator.checkSorterUnitsResultEquals(benchmarkSorterUnit,
                benchmarkSorterUnit, dataProvider, ITERATIONS_COUNT, DATA_LENGTH));
    }

    @Test
    void validationFailedTest() {
        SorterUnit<Integer> benchmarkSorterUnit = new BenchmarkIntegerSorter();

        SorterUnit<Integer> corruptedSorterUnit = new SorterUnit<Integer>() {
            @Override
            public Integer[] sort(Integer[] arrayToSort) {
                return arrayToSort;
            }

            @Override
            public String getDescription() {
                return "Anonymous corrupted sorter unit";
            }

            @Override
            public String getVersion() {
                return "1.0";
            }
        };

        DataProvider<Integer> dataProvider = new SimpleIntegerDataProvider();
        Assertions.assertFalse(SorterValidator.checkSorterUnitsResultEquals(corruptedSorterUnit,
                benchmarkSorterUnit, dataProvider, ITERATIONS_COUNT, DATA_LENGTH));
    }
}
