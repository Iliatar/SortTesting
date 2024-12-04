package org.example.sorterUnit;

public class InvalidSorterUnit implements SorterUnit<Integer> {
    @Override
    public Integer[] sort(Integer[] arrayToSort) {
        return arrayToSort;
    }

    @Override
    public String getDescription() {
        return "Invalid sorter unit, that do nothing";
    }

    @Override
    public String getVersion() {
        return "1.0 final";
    }
}
