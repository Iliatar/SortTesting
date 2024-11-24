package org.example.outputUnit;

import org.example.TestUnit;
import org.example.outputGenerators.OutputGenerator;

public abstract class OutputUnit {
    protected final OutputGenerator outputGenerator;
    protected final TestUnit testUnit;

    public OutputUnit(OutputGenerator outputGenerator, TestUnit testUnit) {
        this.outputGenerator = outputGenerator;
        this.testUnit = testUnit;
    }

    public abstract void writeOutput();
}
