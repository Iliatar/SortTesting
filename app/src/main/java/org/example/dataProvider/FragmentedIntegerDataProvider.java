package org.example.dataProvider;

import java.util.Random;

public class FragmentedIntegerDataProvider implements DataProvider<Integer> {
    private final static double FRAGMENT_MAX_LENGTH = 0.2;
    private final static double FRAGMENT_MIN_LENGTH = 0.05;
    @Override
    public Integer[] getData(int dataLength) {
        Integer[] result =  new Integer[dataLength];
        Random random = new Random();

        int fragmentCounter = 0;
        int currentValue = 0;
        for (int i = 0; i < dataLength; i++) {
            if (--fragmentCounter <= 0) {
                fragmentCounter = (int)(dataLength * (FRAGMENT_MIN_LENGTH + random.nextDouble() * (FRAGMENT_MAX_LENGTH - FRAGMENT_MIN_LENGTH)));
                currentValue = random.nextInt();
            }
            result[i] = currentValue;
        }

        return result;
    }
}
