package org.example.outputGenerators;

import org.example.testUnit.TestItem;
import org.example.testUnit.TestUnit;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;

public class BasicOutputGenerator implements OutputGenerator {
    private static final double MEDIAN_COEFF = 0.5;
    private static final double TOP_COEFF = 0.1;
    private static final double BOTTOM_COEFF = 0.9;
    private static final double MEDIAN_WEIGHT = 0.8;
    private static final double TOP_WEIGHT = 0.1;
    private static final double BOTTOM_WEIGHT = 0.1;
    private static final double NANOS_TO_MILLIS_COEFF = 1000000;
    private final DecimalFormat df = new DecimalFormat("#.###");

    @Override
    public <K> String generateOutput(TestUnit<K> testUnit) {
        double benchmarkEfficiency = 0;
        int totalIterations = 0;

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

        for(TestItem<K> testItem : testUnit.getTestItemsList()) {
            benchmarkEfficiency += generateItemOutput(testItem, resultInfoBuilder) * testItem.getIterationsCount();
            totalIterations += testItem.getIterationsCount();
        }

        resultInfoBuilder.append("\n\nTotal benchmark result is: " + df.format(benchmarkEfficiency / totalIterations));

        return resultInfoBuilder.toString();
    }

    private double generateItemOutput(TestItem testItem, StringBuilder resultInfoBuilder) {

        var dataProvider = testItem.getDataProvider();
        var dataLength = testItem.getDataLength();
        var sorterUnitResultsArray = testItem.getSorterUnitResultsArray();
        var benchmarkResultsArray = testItem.getBenchmarkResultsArray();
        var iterationsCount = testItem.getIterationsCount();


        double medianUnitResult = (double)(sorterUnitResultsArray[(int)(iterationsCount * MEDIAN_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double medianBenchmarkResult = (double)(benchmarkResultsArray[(int)(iterationsCount * MEDIAN_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double topUnitResult = (double)(sorterUnitResultsArray[(int)(iterationsCount * TOP_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double topBenchmarkResult = (double)(benchmarkResultsArray[(int)(iterationsCount * TOP_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double bottomUnitResult = (double)(sorterUnitResultsArray[(int)(iterationsCount * BOTTOM_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double bottomBenchmarkResult = (double)(benchmarkResultsArray[(int)(iterationsCount * BOTTOM_COEFF)]) / NANOS_TO_MILLIS_COEFF;
        double benchmarkEfficiency = (medianBenchmarkResult / medianUnitResult) * MEDIAN_WEIGHT
                + (topBenchmarkResult / topUnitResult) * TOP_WEIGHT
                + (bottomBenchmarkResult / bottomUnitResult) * BOTTOM_WEIGHT;
        benchmarkEfficiency *= 1000;

        resultInfoBuilder.append("\n\nData Provider Unit: ");
        resultInfoBuilder.append(dataProvider.getClass().getName());
        resultInfoBuilder.append("\nData length: ");
        resultInfoBuilder.append(dataLength);
        resultInfoBuilder.append("\nIterations count: ");
        resultInfoBuilder.append(iterationsCount);

        resultInfoBuilder.append("\n\nMedian result (millis): ");
        resultInfoBuilder.append(df.format(medianUnitResult));
        resultInfoBuilder.append(" (benchmark is ");
        resultInfoBuilder.append(df.format(medianBenchmarkResult));
        resultInfoBuilder.append(")");

        resultInfoBuilder.append("\nTop 10% result (millis): ");
        resultInfoBuilder.append(df.format(topUnitResult));
        resultInfoBuilder.append(" (benchmark is ");
        resultInfoBuilder.append(df.format(topBenchmarkResult));
        resultInfoBuilder.append(")");

        resultInfoBuilder.append("\nBottom 10% result (millis): ");
        resultInfoBuilder.append(df.format(bottomUnitResult));
        resultInfoBuilder.append(" (benchmark is ");
        resultInfoBuilder.append(df.format(bottomBenchmarkResult));
        resultInfoBuilder.append(")");

        resultInfoBuilder.append("\n\nBenchmark efficiency is ");
        resultInfoBuilder.append(df.format(benchmarkEfficiency));
        resultInfoBuilder.append("\n----------------------------------------------------");

        return benchmarkEfficiency;
    }
}
