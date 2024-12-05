package org.example.dataProvider;

import org.example.dataProvider.DataProvider;

import java.util.Random;

public class SimpleIntegerDataProvider implements DataProvider<Integer> {

    @Override
    public Integer[] getData(int dataLength) {
        Integer[] result = new Integer[dataLength];

        Random random = new Random();

        for(int i = 0; i < dataLength; i++) {
            result[i] = random.nextInt(10000);
        }

        return result;
    }
}
