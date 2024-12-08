package org.example.utils;

import org.example.dataProvider.DataProvider;
import org.example.sorterUnit.SorterUnit;

public class SorterValidator {
    public static <K> boolean checkSorterUnitsResultEquals(SorterUnit<K> validatedUnit,
                                                           SorterUnit<K> benchmarkUnit,
                                                           DataProvider<K> dataProvider,
                                                           int iterationsCount,
                                                           int dataLength) {
        for(int i = 0; i < iterationsCount; i++) {
            K[] data = dataProvider.getData(dataLength);
            K[] sortedValidatedData = validatedUnit.sort(data.clone());
            K[] sortedBenchmarkData = benchmarkUnit.sort(data.clone());

            if (sortedBenchmarkData.length != sortedValidatedData.length) {
                return false;
            }

            for (int j = 0; j < sortedBenchmarkData.length; j++) {
                if (!sortedBenchmarkData[j].equals(sortedValidatedData[j])) {
                    printNeighborData(j, sortedBenchmarkData, "Benchmark");
                    printNeighborData(j, sortedValidatedData, "Sorter Unit");
                    return false;
                }
            }
        }

        return true;
    }

    private static <K> void printNeighborData(int j, K[] sortedBenchmarkData, String dataName) {
        System.out.println();
        System.out.println(dataName + " neighbor elements are: ");
        final int neighborRadius = 5;
        for(int k = j - neighborRadius; k < j + neighborRadius; k++) {
            if (k < 0 || k >= sortedBenchmarkData.length) continue;
            System.out.print(sortedBenchmarkData[k] + " ");
        }
        System.out.println();
    }
}
