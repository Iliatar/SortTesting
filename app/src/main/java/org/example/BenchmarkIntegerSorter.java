package org.example;

import java.util.Arrays;

public class BenchmarkIntegerSorter implements SorterUnit<Integer> {
    @Override
    public Integer[] sort(Integer[] arrayToSort) {
        Arrays.sort(arrayToSort);
        return arrayToSort;
    }
}
