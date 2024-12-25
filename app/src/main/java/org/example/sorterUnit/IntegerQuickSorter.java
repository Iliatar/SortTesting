package org.example.sorterUnit;

import java.util.LinkedList;
import java.util.List;

public class IntegerQuickSorter implements SorterUnit<Integer> {

    @Override
    public Integer[] sort(Integer[] arrayToSort) {
        if (arrayToSort.length < 2) {
            return arrayToSort;
        }

        if (arrayToSort.length == 2) {
            if (arrayToSort[0] < arrayToSort[1]) {
                return arrayToSort;
            } else {
                return new Integer[]{arrayToSort[1], arrayToSort[0]};
            }
        }

        int baseElementIndex = arrayToSort.length / 2;
        Integer baseElement = arrayToSort[baseElementIndex];
        List<Integer> lesserElements = new LinkedList<>();
        List<Integer> greaterElements = new LinkedList<>();
        int equalElementsCount = 0;

        boolean arrayIsSorted = true;

        for(int i = 0; i < arrayToSort.length; i++) {
            if (arrayToSort[i] < baseElement) {
                lesserElements.add(arrayToSort[i]);
            } else if (arrayToSort[i] > baseElement) {
                greaterElements.add(arrayToSort[i]);
            } else {
                equalElementsCount++;
            }
            if (i > 0 && arrayIsSorted && arrayToSort[i] < arrayToSort[i-1]) {
                arrayIsSorted = false;
            }
        }

        if (arrayIsSorted) return arrayToSort;

        Integer[] result = new Integer[arrayToSort.length];

        Integer[] lesserSortedElements = sort(lesserElements.toArray(new Integer[0]));
        Integer[] greaterSortedElements = sort(greaterElements.toArray(new Integer[0]));

        int i = 0;
        for (Integer value : lesserSortedElements) {
            result[i++] = value;
        }

        for (int j = 1; j <= equalElementsCount; j++) {
            result[i++] = Integer.valueOf(baseElement.intValue());
        }

        for (Integer value : greaterSortedElements) {
            result[i++] = value;
        }

        return result;
    }

    @Override
    public String getDescription() {
        return "My quick-sort algorithm";
    }

    @Override
    public String getVersion() {
        return "0.65";
    }
}
