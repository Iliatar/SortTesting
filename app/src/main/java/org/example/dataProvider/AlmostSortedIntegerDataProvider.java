package org.example.dataProvider;

import java.util.Arrays;
import java.util.Random;

public class AlmostSortedIntegerDataProvider implements DataProvider<Integer> {
    @Override
    public Integer[] getData(int dataLength) {
        SortedIntegerDataProvider sortedIntegerDataProvider = new SortedIntegerDataProvider();
        Integer[] result = sortedIntegerDataProvider.getData(dataLength);
        Arrays.sort(result);

        int sortCorruptions = dataLength / 100;
        Random random = new Random();
        for(int i = 0; i < sortCorruptions; i ++) {
            int firstElementIndex = random.nextInt(dataLength);
            int secondElementIndex = random.nextInt(dataLength);
            Integer firstElement = result[firstElementIndex];
            result[firstElementIndex] = result[secondElementIndex];
            result[secondElementIndex] = firstElement;
        }

        return result;
    }
}
