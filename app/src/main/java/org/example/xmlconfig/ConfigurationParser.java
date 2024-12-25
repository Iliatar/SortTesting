package org.example.xmlconfig;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.testUnit.TestItem;
import org.example.testUnit.TestUnit;
import org.example.utils.BenchmarksLibrary;
import org.example.utils.FileClassLoader;

import java.io.File;
import java.util.List;

public class ConfigurationParser {
    public static TestUnit getTestUnit(File file)  throws Exception  {
        ObjectMapper objectMapper = new ObjectMapper();
        TestConfiguration testConfiguration = objectMapper.readValue(file, TestConfiguration.class);

        Class<?> sorterUnitClass = FileClassLoader.loadClassFromFile(testConfiguration.sorterUnitClassName, testConfiguration.sorterUnitClassFilePath);
        Object sorterUnit = FileClassLoader.getClassInstance(sorterUnitClass);

        Class unitClass = Class.forName(testConfiguration.getUnitClassName());
        Object benchmarkUnit = BenchmarksLibrary.getBenchmarkUnit(unitClass);

        TestUnit testUnit = new TestUnit(unitClass, sorterUnit, benchmarkUnit);

        for(TestConfiguration.TestConfigurationItem configurationItem : testConfiguration.getTestConfigurationItems()) {
            try {
                Class dataProviderClass = Class.forName(configurationItem.getDataProviderClassName());
                Object dataProvider = FileClassLoader.getClassInstance(dataProviderClass);
                TestItem testItem = new TestItem(dataProvider,
                        configurationItem.getDataLength(),
                        configurationItem.getIterationsCount());
                testUnit.addTestItem(testItem);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Provider class " + configurationItem.getDataProviderClassName() + " not found");
            }
        }

        return testUnit;
    }

    //TODO добавить тесты на парсинг
    private static class TestConfiguration {
        @JsonProperty("unitClassName")
        String unitClassName;
        @JsonProperty("className")
        String sorterUnitClassName;
        @JsonProperty("filePath")
        String sorterUnitClassFilePath;
        @JsonProperty("testItems")
        List<TestConfigurationItem> testConfigurationItems;

        private static class TestConfigurationItem {
            @JsonProperty("dataProvider")
            String dataProviderClassName;
            @JsonProperty("dataLength")
            int dataLength;
            @JsonProperty("iterations")
            int iterationsCount;

            public String getDataProviderClassName() {
                return dataProviderClassName;
            }

            public void setDataProviderClassName(String dataProviderClassName) {
                this.dataProviderClassName = dataProviderClassName;
            }

            public int getDataLength() {
                return dataLength;
            }

            public void setDataLength(int dataLength) {
                this.dataLength = dataLength;
            }

            public int getIterationsCount() {
                return iterationsCount;
            }

            public void setIterationsCount(int iterationsCount) {
                this.iterationsCount = iterationsCount;
            }
        }

        public String getSorterUnitClassName() {
            return sorterUnitClassName;
        }

        public String getSorterUnitClassFilePath() {
            return sorterUnitClassFilePath;
        }

        public List<TestConfigurationItem> getTestConfigurationItems() {
            return testConfigurationItems;
        }

        public void setSorterUnitClassName(String sorterUnitClassName) {
            this.sorterUnitClassName = sorterUnitClassName;
        }

        public void setSorterUnitClassFilePath(String sorterUnitClassFilePath) {
            this.sorterUnitClassFilePath = sorterUnitClassFilePath;
        }

        public void setTestConfigurationItems(List<TestConfigurationItem> testConfigurationItems) {
            this.testConfigurationItems = testConfigurationItems;
        }

        public String getUnitClassName() {
            return unitClassName;
        }

        public void setUnitClassName(String unitClassName) {
            this.unitClassName = unitClassName;
        }
    }
}
