package org.example.sorterUnit;

import org.example.sorterUnit.SorterUnit;

import java.util.ArrayList;
import java.util.List;

public class IntegerQuickSorter implements SorterUnit<Integer> {

    @Override
    public Integer[] sort(Integer[] arrayToSort) {
        if (arrayToSort.length < 2) {
            return arrayToSort;
        }

        Integer baseElement = arrayToSort[0];
        List<Integer> lesserElements = new ArrayList<>();
        List<Integer> greaterElements = new ArrayList<>();

        for(int i = 1; i < arrayToSort.length; i++) {
            if (arrayToSort[i] <= baseElement) {
                lesserElements.add(arrayToSort[i]);
            } else {
                greaterElements.add(arrayToSort[i]);
            }
        }

        Integer[] result = new Integer[lesserElements.size() + 1 + greaterElements.size()];

        Integer[] lesserSortedElements = sort(lesserElements.toArray(new Integer[0]));
        Integer[] greaterSortedElements = sort(greaterElements.toArray(new Integer[0]));

        int i = 0;
        for (Integer value : lesserSortedElements) {
            result[i++] = value;
        }
        result[i++] = baseElement;

        for (Integer value : greaterSortedElements) {
            result[i++] = value;
        }

        return result;
    }
}
