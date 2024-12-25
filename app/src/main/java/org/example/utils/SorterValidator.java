package org.example.utils;

import org.example.dataProvider.DataProvider;
import org.example.sorterUnit.SorterUnit;

import java.lang.reflect.Method;

public class SorterValidator {
    public static boolean checkSorterUnitsResultEquals(Class<?> unitClass,
                                                       Object validatedUnit,
                                                       Object benchmarkUnit,
                                                       Object dataProvider,
                                                       int iterationsCount,
                                                       int dataLength) throws Exception {
        for(int i = 0; i < iterationsCount; i++) {
            Method getDataMethod = dataProvider.getClass().getMethod("getData", int.class);
            Object[] data = (Object[]) getDataMethod.invoke(dataProvider, dataLength);
            Method sortValidatedUnitMethod = validatedUnit.getClass().getMethod("sort", unitClass.arrayType());
            Object[] sortedValidatedData = (Object[]) sortValidatedUnitMethod.invoke(validatedUnit, new Object[] {data.clone()});
            Method sortBenchmarkUnitMethod = benchmarkUnit.getClass().getMethod("sort", unitClass.arrayType());
            Object[] sortedBenchmarkData = (Object[]) sortBenchmarkUnitMethod.invoke(benchmarkUnit, new Object[] {data.clone()});

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

    private static void printNeighborData(int j, Object[] sortedBenchmarkData, String dataName) {
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
