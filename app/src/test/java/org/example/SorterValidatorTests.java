package org.example;

import org.example.dataProvider.DataProvider;
import org.example.dataProvider.SimpleIntegerDataProvider;
import org.example.sorterUnit.BenchmarkIntegerSorter;
import org.example.sorterUnit.InvalidSorterUnit;
import org.example.sorterUnit.SorterUnit;
import org.example.utils.SorterValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class SorterValidatorTests {
    private final static int ITERATIONS_COUNT = 10;
    private final static int DATA_LENGTH = 100;

    @Test
    void validationPassedTest(){
        Object benchmarkSorterUnit = new BenchmarkIntegerSorter();
        Object dataProvider = new SimpleIntegerDataProvider();
        Assertions.assertDoesNotThrow(() -> Assertions.assertTrue(SorterValidator.checkSorterUnitsResultEquals(Integer.class, benchmarkSorterUnit,
                benchmarkSorterUnit, dataProvider, ITERATIONS_COUNT, DATA_LENGTH)));
    }

    @Test
    void validationFailedTest() throws Exception {
        Object benchmarkSorterUnit = new BenchmarkIntegerSorter();

        Object corruptedSorterUnit = new InvalidSorterUnit();

        DataProvider<Integer> dataProvider = new SimpleIntegerDataProvider();
        Assertions.assertDoesNotThrow(() -> Assertions.assertFalse(SorterValidator.checkSorterUnitsResultEquals(Integer.class, corruptedSorterUnit,
                benchmarkSorterUnit, dataProvider, ITERATIONS_COUNT, DATA_LENGTH)));
    }
}
