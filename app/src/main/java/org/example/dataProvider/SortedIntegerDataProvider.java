package org.example.dataProvider;

import java.util.Arrays;

public class SortedIntegerDataProvider implements DataProvider<Integer> {
    @Override
    public Integer[] getData(int dataLength) {
        SimpleIntegerDataProvider simpleIntegerDataProvider = new SimpleIntegerDataProvider();
        Integer[] result = simpleIntegerDataProvider.getData(dataLength);
        Arrays.sort(result);
        return result;
    }
}
