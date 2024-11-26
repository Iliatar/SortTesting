package org.example.outputGenerators;

import org.example.testUnit.TestUnit;

import java.text.DecimalFormat;

public class BasicOutputGenerator implements OutputGenerator {
    private static final double MEDIAN_COEFF = 0.5;
    private static final double TOP_COEFF = 0.1;
    private static final double BOTTOM_COEFF = 0.9;
    private static final double MEDIAN_WEIGHT = 0.8;
    private static final double TOP_WEIGHT = 0.1;
    private static final double BOTTOM_WEIGHT = 0.1;
    private static final double NANOS_TO_MILLIS_COEFF = 1000000;

    @Override
    public String generateOutput(TestUnit testUnit) {
        var sorterUnit = testUnit.getSorterUnit();
        var benchmarkUnit = testUnit.getBenchmarkUnit();
        var dataProvider = testUnit.getDataProvider();
        var dataLength = testUnit.getDataLength();
        var sorterUnitResultsArray = testUnit.getSorterUnitResultsArray();
        var benchmarkResultsArray = testUnit.getBenchmarkResultsArray();
        var iterationsCount = testUnit.getIterationsCount();

        StringBuilder resultInfoBuilder = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.###");
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

        resultInfoBuilder.append("TEST RESULT INFO\nSorter unit: ");
        resultInfoBuilder.append(sorterUnit.getClass().getName());
        resultInfoBuilder.append("\nBenchmark unit: ");
        resultInfoBuilder.append(benchmarkUnit.getClass().getName());
        resultInfoBuilder.append("\nData Provider Unit: ");
        resultInfoBuilder.append(dataProvider.getClass().getName());
        resultInfoBuilder.append("\nData length: ");
        resultInfoBuilder.append(dataLength);
        resultInfoBuilder.append("\nIterations count: ");
        resultInfoBuilder.append(iterationsCount);

        resultInfoBuilder.append("\nMedian result (millis): ");
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

        resultInfoBuilder.append("\nBenchmark efficiency is ");
        resultInfoBuilder.append(df.format(benchmarkEfficiency));
        resultInfoBuilder.append("\n");

        return resultInfoBuilder.toString();
    }
}
