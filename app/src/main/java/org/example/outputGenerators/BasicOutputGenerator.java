package org.example.outputGenerators;

import org.example.testUnit.TestItem;
import org.example.testUnit.TestUnit;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

public class BasicOutputGenerator implements OutputGenerator {

    private final DecimalFormat df = new DecimalFormat("#.###");

    @Override
    public <K> String generateOutput(TestUnit<K> testUnit) {

        StringBuilder resultInfoBuilder = new StringBuilder();

        var sorterUnit = testUnit.getSorterUnit();
        var benchmarkUnit = testUnit.getBenchmarkUnit();
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = now.format(new DateTimeFormatterBuilder().appendPattern("yyyy/MM/dd kk:mm:ss").toFormatter());

        resultInfoBuilder.append("\n------------------TEST RESULT INFO------------------\n\n");
        resultInfoBuilder.append(timeStamp);
        resultInfoBuilder.append("\n\nSorter unit: ");
        resultInfoBuilder.append(sorterUnit.getClass().getName());
        resultInfoBuilder.append("\nDescription: ");
        resultInfoBuilder.append(sorterUnit.getDescription());
        resultInfoBuilder.append("\nVersion: ");
        resultInfoBuilder.append(sorterUnit.getVersion());

        resultInfoBuilder.append("\n\nBenchmark unit: ");
        resultInfoBuilder.append(benchmarkUnit.getClass().getName());
        resultInfoBuilder.append("\nDescription: ");
        resultInfoBuilder.append(benchmarkUnit.getDescription());
        resultInfoBuilder.append("\nVersion: ");
        resultInfoBuilder.append(benchmarkUnit.getVersion());
        resultInfoBuilder.append("\n----------------------------------------------------");

        double benchmarkEfficiency = 0;
        int totalIterations = 0;

        for(TestItem<K> testItem : testUnit.getTestItemsList()) {
            var testItemResult = new TestItemResult(testItem);
            resultInfoBuilder.append(generateItemOutput(testItemResult));
            benchmarkEfficiency += testItemResult.benchmarkEfficiency * testItem.getIterationsCount();
            totalIterations += testItem.getIterationsCount();
        }

        resultInfoBuilder.append("\n\nTotal benchmark result is: " + df.format(benchmarkEfficiency / totalIterations));

        return resultInfoBuilder.toString();
    }

    private String generateItemOutput(TestItemResult testItemResult) {

        var outputBuilder = new StringBuilder();
        var dataProvider = testItemResult.getTestItem().getDataProvider();
        var dataLength = testItemResult.getTestItem().getDataLength();
        var iterationsCount = testItemResult.getTestItem().getIterationsCount();

        outputBuilder.append("\n\nData Provider Unit: ");
        outputBuilder.append(dataProvider.getClass().getName());
        outputBuilder.append("\nData length: ");
        outputBuilder.append(dataLength);
        outputBuilder.append("\nIterations count: ");
        outputBuilder.append(iterationsCount);

        outputBuilder.append("\n\nMedian result (millis): ");
        outputBuilder.append(df.format(testItemResult.getMedianUnitResult()));
        outputBuilder.append(" (benchmark is ");
        outputBuilder.append(df.format(testItemResult.getMedianBenchmarkResult()));
        outputBuilder.append(")");

        outputBuilder.append("\nTop 10% result (millis): ");
        outputBuilder.append(df.format(testItemResult.getTopUnitResult()));
        outputBuilder.append(" (benchmark is ");
        outputBuilder.append(df.format(testItemResult.getTopBenchmarkResult()));
        outputBuilder.append(")");

        outputBuilder.append("\nBottom 10% result (millis): ");
        outputBuilder.append(df.format(testItemResult.getBottomUnitResult()));
        outputBuilder.append(" (benchmark is ");
        outputBuilder.append(df.format(testItemResult.getBottomBenchmarkResult()));
        outputBuilder.append(")");

        outputBuilder.append("\n\nBenchmark efficiency is ");
        outputBuilder.append(df.format(testItemResult.getBenchmarkEfficiency()));
        outputBuilder.append("\n----------------------------------------------------");

        return outputBuilder.toString();
    }

    private static class TestItemResult {
        private static final double MEDIAN_COEFF = 0.5;
        private static final double TOP_COEFF = 0.1;
        private static final double BOTTOM_COEFF = 0.9;
        private static final double MEDIAN_WEIGHT = 0.8;
        private static final double TOP_WEIGHT = 0.1;
        private static final double BOTTOM_WEIGHT = 0.1;
        private static final double NANOS_TO_MILLIS_COEFF = 1000000;
        private static final double BENCHMARK_EFF_COEFF = 1000;
        private final TestItem testItem;
        private final double benchmarkEfficiency;

        private TestItemResult(TestItem testItem) {
            this.testItem = testItem;
            benchmarkEfficiency = ((getMedianBenchmarkResult() / getMedianUnitResult()) * MEDIAN_WEIGHT
                    + (getTopBenchmarkResult() / getTopUnitResult()) * TOP_WEIGHT
                    + (getBottomBenchmarkResult() / getBottomUnitResult()) * BOTTOM_WEIGHT)
                    * BENCHMARK_EFF_COEFF;
        }

        public TestItem getTestItem() {
            return testItem;
        }

        public double getBenchmarkEfficiency() {
            return benchmarkEfficiency;
        }

        private double getMedianUnitResult() {
            return getResult(testItem.getSorterUnitResultsArray(), testItem.getIterationsCount(), MEDIAN_COEFF);
        }
        private double getMedianBenchmarkResult() {
            return getResult(testItem.getBenchmarkResultsArray(), testItem.getIterationsCount(), MEDIAN_COEFF);
        }

        private double getTopUnitResult() {
            return getResult(testItem.getSorterUnitResultsArray(), testItem.getIterationsCount(), TOP_COEFF);
        }

        private double getTopBenchmarkResult() {
            return getResult(testItem.getBenchmarkResultsArray(), testItem.getIterationsCount(), TOP_COEFF);
        }

        private double getBottomUnitResult() {
            return getResult(testItem.getSorterUnitResultsArray(), testItem.getIterationsCount(), BOTTOM_COEFF);
        }

        private double getBottomBenchmarkResult() {
            return getResult(testItem.getBenchmarkResultsArray(), testItem.getIterationsCount(), BOTTOM_COEFF);
        }

        private double getResult(long[] resultArray, int itereationsCount, double coeff) {
            return resultArray[(int)(itereationsCount * coeff)] / NANOS_TO_MILLIS_COEFF;
        }
    }
}
