package org.example.sorterUnit;

import java.util.ArrayList;
import java.util.List;

public class IntegerQuickSorter implements SorterUnit<Integer> {

    @Override
    public Integer[] sort(Integer[] arrayToSort) {
        if (arrayToSort.length < 2) {
            return arrayToSort;
        }

        int baseElementIndex = 0;
        Integer baseElement = arrayToSort[baseElementIndex];
        List<Integer> lesserElements = new ArrayList<>();
        List<Integer> greaterElements = new ArrayList<>();
        List<Integer> equalElements = new ArrayList<>();

        for(int i = 0; i < arrayToSort.length; i++) {
            if (arrayToSort[i].equals(baseElement)) {
                equalElements.add(arrayToSort[i]);
            } else if (arrayToSort[i] < baseElement) {
                lesserElements.add(arrayToSort[i]);
            } else if (arrayToSort[i] > baseElement) {
                greaterElements.add(arrayToSort[i]);
            }
        }

        Integer[] result = new Integer[arrayToSort.length];

        Integer[] lesserSortedElements = sort(lesserElements.toArray(new Integer[0]));
        Integer[] greaterSortedElements = sort(greaterElements.toArray(new Integer[0]));

        int i = 0;
        for (Integer value : lesserSortedElements) {
            result[i++] = value;
        }

        for (Integer value : equalElements) {
            result[i++] = value;
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
        return "0.6";
    }
}
