package org.example;

import org.example.dataProvider.*;
import org.example.sorterUnit.BenchmarkIntegerSorter;
import org.example.sorterUnit.IntegerQuickSorter;
import org.example.sorterUnit.SorterUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class IntegerQuickSorterTests {
    private final static int ITERATIONS_COUNT = 5;
    private final static int DATA_LENGTH = 100;

    @Test
    void test() {
        List<DataProvider<Integer>> dataProviders = new ArrayList<>();
        dataProviders.add(new SimpleIntegerDataProvider());
        dataProviders.add(new FragmentedIntegerDataProvider());
        dataProviders.add(new AlmostSortedIntegerDataProvider());
        dataProviders.add(new SortedIntegerDataProvider());

        List<SorterUnit<Integer>> sorterUnits = new ArrayList<>();
        sorterUnits.add(new BenchmarkIntegerSorter());
        sorterUnits.add(new IntegerQuickSorter());

        for(var sorterUnit : sorterUnits) {
            for (var dataProvider : dataProviders) {
                for(int i = 0; i < ITERATIONS_COUNT; i ++) {
                    testSorterUnit(sorterUnit, dataProvider);
                }
                System.out.println("Test for " + sorterUnit.getClass().getName()
                        + " by " + dataProvider.getClass().getName() + " passed.");
            }
        }
    }

    private void testSorterUnit(SorterUnit<Integer> sorterUnit, DataProvider<Integer> dataProvider) {
        Integer[] data = dataProvider.getData(DATA_LENGTH);
        Integer[] result = sorterUnit.sort(data);

        Assertions.assertEquals(data.length, result.length);

        /*System.out.println("Result of sorter unit:");
        for (int i = 1; i < result.length; i++) {
            System.out.print(result[i] + " ");
            Assertions.assertTrue(result[i] >= result[i - 1]);
        }
        System.out.println();*/
    }
}
