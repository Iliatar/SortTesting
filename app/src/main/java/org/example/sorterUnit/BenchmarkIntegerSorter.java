package org.example.sorterUnit;

import org.example.sorterUnit.SorterUnit;

import java.util.Arrays;

public class BenchmarkIntegerSorter {
    public Integer[] sort(Integer[] arrayToSort) {
        Arrays.sort(arrayToSort);
        return arrayToSort;
    }

    public String getDescription() {
        return "Arrays.sort()";
    }

    public String getVersion() {
        return "1";
    }
}
