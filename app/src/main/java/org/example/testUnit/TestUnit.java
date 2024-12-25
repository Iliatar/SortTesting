package org.example.testUnit;

import org.example.utils.SorterValidator;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TestUnit {
    private static final int VALIDATOR_ITERATIONS_COUNT = 10;
    private static final double ONE_POINT_PROGRESS = 4f;
    private static final int MILESTONE_POINTS_COUNT = 5;

    private final Class<?> unitClass;
    private final Object sorterUnit;
    private final Object benchmarkUnit;
    private final List<TestItem> testItemsList;
    private double iterationProgress;
    private double currentPercentProgress = 0;
    private double totalProgress = 0;
    private boolean completeFlag = false;

    public TestUnit(Class<?> unitClass, Object sorterUnit, Object benchmarkUnit) {
        this.sorterUnit = sorterUnit;
        this.benchmarkUnit = benchmarkUnit;
        this.unitClass = unitClass;
        testItemsList = new ArrayList<>();
    }

    public void addTestItem(TestItem testItem) {
        testItemsList.add(testItem);
    }

    public void runTest() throws Exception {
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

    public void processTestItem(TestItem testItem) throws Exception{

        checkTestItem(testItem);

        testItem.initialize();

        var dataProvider = testItem.getDataProvider();
        var dataLength = testItem.getDataLength();
        var iterationsCount = testItem.getIterationsCount();

        DecimalFormat df = new DecimalFormat("#");

        for (int iterationNum = 0; iterationNum < iterationsCount; iterationNum++) {
            Method getDataMethod = dataProvider.getClass().getMethod("getData", int.class);
            Object[] data = (Object[]) getDataMethod.invoke(dataProvider, dataLength);
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

    private long runTest(Object[] data, Object sorterUnit) throws Exception {
        Method m = sorterUnit.getClass().getMethod("sort", unitClass.arrayType());
        Instant startTime = Instant.now();
        m.invoke(sorterUnit, new Object[] {unitClass.arrayType().cast(data)});
        Instant endTime = Instant.now();
        return Duration.between(startTime, endTime).toNanos();
    }

    private void checkTestItem(TestItem testItem) throws Exception {
        Class sorterUnitClass = sorterUnit.getClass();
        checkSorterUnitClass(sorterUnitClass);

        Class benchmarkUnitClass = benchmarkUnit.getClass();
        checkSorterUnitClass(benchmarkUnitClass);

        Class providerUnitClass = testItem.getDataProvider().getClass();
        checkProviderUnitClass(providerUnitClass);


        if(!SorterValidator.checkSorterUnitsResultEquals(unitClass,
                sorterUnit,
                benchmarkUnit,
                testItem.getDataProvider(),
                VALIDATOR_ITERATIONS_COUNT,
                testItem.getDataLength())) {
            //TODO если тесты не пройдены, то сообщение появляется на той же сторке, что и "Progress: 0%"
            throw new SorterUnitValidationFailedException("Tested sorter unit (" + sorterUnit.getClass().getName()
                    + ") validation failed! Result don't match benchmark unit result!");
        }
    }

    //TODO вынести в отдельный класс
    //TODO сделать код более гибким - проверять метод через подачу на вход параметров
    private void checkSorterUnitClass(Class sorterUnitClass) throws NoSuchMethodException,
            SorterUnitClassValidationFailedException {
        Method sortMethod = sorterUnitClass.getMethod("sort", unitClass.arrayType());

        if (sortMethod.getParameterCount() != 1) {
            throw new SorterUnitClassValidationFailedException("Sorter class " + sorterUnitClass.getName()
                    + " sort method parameters count doesn't equals 1");
        }
        if (!sortMethod.getParameterTypes()[0].isArray() ||
                sortMethod.getParameterTypes()[0].getComponentType() != unitClass) {
            throw new SorterUnitClassValidationFailedException("Sorter class " + sorterUnitClass.getName()
                    + " sort method argument type doesn't " + unitClass.getName() + "[]");
        }
        if (!sortMethod.getReturnType().isArray() ||
                sortMethod.getReturnType().getComponentType() != unitClass) {
            throw new SorterUnitClassValidationFailedException("Sorter class " + sorterUnitClass.getName()
                    + " sort method return type doesn't " + unitClass.getName() + "[]");
        }

        try {
            sorterUnitClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new SorterUnitClassValidationFailedException("Sorter class " + sorterUnitClass.getName()
                    + " doesn't have no args constructor");
        }

        //TODO дописать проверку методов String getDescription() и String getVersion()
    }

    private void checkProviderUnitClass(Class providerUnitClass) throws NoSuchMethodException,
            SorterUnitClassValidationFailedException {
        Method getDataMethod = providerUnitClass.getMethod("getData", int.class);

        if (getDataMethod.getParameterCount() != 1) {
            throw new SorterUnitClassValidationFailedException("Provider class " + providerUnitClass.getName()
                    + " getData method parameters count doesn't equals 1");
        }
        if (getDataMethod.getParameterTypes()[0] != int.class) {
            throw new SorterUnitClassValidationFailedException("Provider class " + providerUnitClass.getName()
                    + " getData method argument type doesn't int");
        }
        if (!getDataMethod.getReturnType().isArray() ||
                getDataMethod.getReturnType().getComponentType() != unitClass) {
            throw new SorterUnitClassValidationFailedException("Provider class " + providerUnitClass.getName()
                    + " getData method return type doesn't " + unitClass.getName() + "[]");
        }

        try {
            providerUnitClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new SorterUnitClassValidationFailedException("Sorter class " + providerUnitClass.getName()
                    + " doesn't have no args constructor");
        }
    }

    public Object getSorterUnit() {
        return sorterUnit;
    }

    public Object getBenchmarkUnit() {
        return benchmarkUnit;
    }

    public List<TestItem> getTestItemsList() { return testItemsList; }

    public boolean isComplete() { return completeFlag; }
}
