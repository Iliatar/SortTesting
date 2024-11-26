package org.example.outputUnit;

import org.example.testUnit.TestUnit;
import org.example.outputGenerators.OutputGenerator;

public class ConsoleOutputUnit extends OutputUnit {
    public ConsoleOutputUnit(OutputGenerator outputGenerator, TestUnit testUnit) {
        super(outputGenerator, testUnit);
    }

    @Override
    public void writeOutput() {
        System.out.println(outputGenerator.generateOutput(testUnit));
    }
}
