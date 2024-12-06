package org.example.testUnit;

import org.example.utils.SorterValidator;
import org.example.dataProvider.DataProvider;
import org.example.sorterUnit.SorterUnit;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUnit<K> {
    private static final int VALIDATOR_ITERATIONS_COUNT = 10;
    private static final double ONE_POINT_PROGRESS = 4f;
    private static final int MILESTONE_POINTS_COUNT = 5;

    private final SorterUnit<K> sorterUnit;
    private final SorterUnit<K> benchmarkUnit;
    private final List<TestItem<K>> testItemsList;
    private double iterationProgress;
    private double currentPercentProgress = 0;
    private double totalProgress = 0;
    private boolean completeFlag = false;

    public TestUnit(SorterUnit<K> sorterUnit, SorterUnit<K> benchmarkUnit) {
        this.sorterUnit = sorterUnit;
        this.benchmarkUnit = benchmarkUnit;
        testItemsList = new ArrayList<>();
    }

    public void addTestItem(TestItem<K> testItem) {
        testItemsList.add(testItem);
    }

    public void runTest() throws SorterUnitValidationFailedException {
        int totalIterations = testItemsList.stream()
                .map(item -> item.getIterationsCount())
                .reduce(0, (x,y) -> x + y);

        iterationProgress = 100f / (double) totalIterations;

        System.out.print("Progress: 0%");

        for(TestItem item : testItemsList) {
            processTestItem(item);
        }

        completeFlag = true;
        System.out.println("\nTests complete!");
    }

    public void processTestItem(TestItem<K> testItem) throws SorterUnitValidationFailedException {

        testItem.initialize();

        var dataProvider = testItem.getDataProvider();
        var dataLength = testItem.getDataLength();
        var iterationsCount = testItem.getIterationsCount();

        if(!SorterValidator.checkSorterUnitsResultEquals(sorterUnit, benchmarkUnit,
                dataProvider, VALIDATOR_ITERATIONS_COUNT, dataLength)) {
            //TODO если тесты не пройдены, то сообщение появляется на той же сторке, что и "Progress: 0%"
            throw new SorterUnitValidationFailedException("Tested sorter unit (" + sorterUnit.getClass().getName()
                    + ") validation failed! Result don't match benchmark unit result!");
        }

        DecimalFormat df = new DecimalFormat("#");

        for (int iterationNum = 0; iterationNum < iterationsCount; iterationNum++) {
            K[] data = dataProvider.getData(dataLength);
            testItem.setSorterUnitResult(iterationNum, runTest(data, sorterUnit));
            testItem.setBenchmarkResult(iterationNum, runTest(data, benchmarkUnit));

            currentPercentProgress += iterationProgress;

            while (currentPercentProgress >= ONE_POINT_PROGRESS) {
                totalProgress += ONE_POINT_PROGRESS;
                currentPercentProgress -= ONE_POINT_PROGRESS;
                if (totalProgress % (MILESTONE_POINTS_COUNT * ONE_POINT_PROGRESS) == 0) {
                    System.out.print(df.format(totalProgress) + "%");
                } else {
                    System.out.print(".");
                }
            }
        }

        testItem.close();
    }

    private <K> long runTest(K[] data, SorterUnit<K> sorterUnit) {
        data = Arrays.copyOf(data, data.length);
        Instant startTime = Instant.now();
        sorterUnit.sort(data);
        Instant endTime = Instant.now();
        return Duration.between(startTime, endTime).toNanos();
    }

    public SorterUnit<K> getSorterUnit() {
        return sorterUnit;
    }

    public SorterUnit<K> getBenchmarkUnit() {
        return benchmarkUnit;
    }

    public List<TestItem<K>> getTestItemsList() { return testItemsList; }

    public boolean isComplete() { return completeFlag; }
}
