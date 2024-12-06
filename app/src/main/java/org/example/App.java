/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import org.example.dataProvider.DataProvider;
import org.example.dataProvider.FragmentedIntegerDataProvider;
import org.example.dataProvider.SimpleIntegerDataProvider;
import org.example.outputGenerators.BasicOutputGenerator;
import org.example.outputGenerators.OutputGenerator;
import org.example.outputUnit.ConsoleOutputUnit;
import org.example.outputUnit.OutputUnit;
import org.example.outputUnit.TextFileOutputUnit;
import org.example.sorterUnit.BenchmarkIntegerSorter;
import org.example.sorterUnit.SorterUnit;
import org.example.testUnit.SorterUnitValidationFailedException;
import org.example.testUnit.TestItem;
import org.example.testUnit.TestUnit;
import org.example.utils.SorterClassLoader;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "test",
        description = "Test sorter unit benchmark"
)
public class App implements Runnable {
    @Parameters(index = "0", arity = "1",
                    description = "Full name of class, which will be tested. " +
                    "Class must have no args constructor and implements SorterUnit interface")
    private String sorterUnitClassName;

    @Parameters(index = "1", arity = "1",
                description = "File path if class, which will be tested (i.e. '../testedSorter.class').")
    private String sorterUnitFilePath;

    @Option(names = {"-i", "--iterations"}, description = "Test iterations count",
            defaultValue = "5000", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private Integer iterationsCount;

    @Option(names = {"-l", "--dataLength"}, description = "Test data size",
            defaultValue = "8000", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private Integer dataLength;

    @Option(names = {"-f", "--fileOutput"}, description = "Output to file flag")
    private boolean fileOutput;

    @Option(names = {"-c", "--complex"}, description = "Complex benchmark flag. Overrides -l, -d and -i options")
    private boolean complex;

    @Option(names = {"-d", "--dataProvider"}, description = "Name of class, which will be used as data provider. " +
            "Class must have no args constructor and implements DataProvider interface",
            defaultValue = "org.example.dataProvider.SimpleIntegerDataProvider",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String dataProviderClassName = "org.example.dataProvider.SimpleIntegerDataProvider";

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            runTests();
        } catch (Exception e) {
            System.out.println("Exception happened: " + e.getMessage());
        }
    }

    private void runTests() throws Exception {
        SorterUnit<Integer> sorterUnit = null;
        DataProvider<Integer> dataProvider = null;

        //TODO выделить в отдельный метод
        try {
            SorterClassLoader sorterClassLoader = new SorterClassLoader();
            Class<?> sorterUnitClass = sorterClassLoader.findClass(sorterUnitClassName, sorterUnitFilePath);
            sorterUnit = (SorterUnit<Integer>) sorterUnitClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new Exception("Sorter Class with name " + sorterUnitClassName + " don't have no args constructor!");
        } catch (NoClassDefFoundError e) {
            throw new Exception(sorterUnitClassName + " don't find in " + sorterUnitFilePath);
        }

        SorterUnit<Integer> benchmarkSorter = new BenchmarkIntegerSorter();

        var testUnit = new TestUnit<>(sorterUnit, benchmarkSorter);
        if(complex) {
            testUnit.addTestItem(new TestItem<>(new SimpleIntegerDataProvider(), 100, 1000));
            testUnit.addTestItem(new TestItem<>(new SimpleIntegerDataProvider(), 1000, 3000));
            testUnit.addTestItem(new TestItem<>(new SimpleIntegerDataProvider(), 5000, 2000));
            testUnit.addTestItem(new TestItem<>(new FragmentedIntegerDataProvider(), 100, 1000));
            testUnit.addTestItem(new TestItem<>(new FragmentedIntegerDataProvider(), 1000, 3000));
            testUnit.addTestItem(new TestItem<>(new FragmentedIntegerDataProvider(), 5000, 2000));
        } else {
            //TODO выделить в отдельный метод
            try {
                dataProvider = (DataProvider<Integer>) Class.forName(dataProviderClassName)
                        .getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException e) {
                throw new Exception("Provider Class with name " + dataProviderClassName + " not found");
            } catch (NoSuchMethodException e) {
                throw new Exception("Provider Class with name " + dataProviderClassName + " don't have no args constructor");
            }

            testUnit.addTestItem(new TestItem<>(dataProvider, dataLength, iterationsCount));
        }

        testUnit.runTest();

        if (!testUnit.isComplete()) return;

        OutputGenerator basicOutputGenerator = new BasicOutputGenerator();
        OutputUnit outputUnit = fileOutput
                ? new TextFileOutputUnit(basicOutputGenerator, testUnit)
                : new ConsoleOutputUnit(basicOutputGenerator, testUnit);
        outputUnit.writeOutput();
    }
}
