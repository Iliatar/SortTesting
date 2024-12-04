package org.example.outputGenerators;

import org.example.testUnit.TestUnit;

public interface OutputGenerator {
    <K> String generateOutput(TestUnit<K> testUnit);
}
