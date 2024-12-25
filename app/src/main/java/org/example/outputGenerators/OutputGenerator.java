package org.example.outputGenerators;

import org.example.testUnit.TestUnit;

public interface OutputGenerator {
    String generateOutput(TestUnit testUnit) throws Exception;
}
