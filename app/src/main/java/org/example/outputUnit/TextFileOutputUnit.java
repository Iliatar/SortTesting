package org.example.outputUnit;

import org.example.TestUnit;
import org.example.outputGenerators.OutputGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.time.LocalDate;

public class TextFileOutputUnit  extends OutputUnit {
    public TextFileOutputUnit(OutputGenerator outputGenerator, TestUnit testUnit) {
        super(outputGenerator, testUnit);
    }

    @Override
    public void writeOutput() {
        String fileNameBase = testUnit.getSorterUnit().getClass().getName() + LocalDate.now() + "_";
        String fileExtension = ".txt";
        int fileSuffix = 1;
        while (new File(fileNameBase + fileSuffix + fileExtension).exists()) {
            fileSuffix++;
        }

        String fileName = fileNameBase + fileSuffix + fileExtension;

        String output = outputGenerator.generateOutput(testUnit);
        try {
            FileChannel fc = new FileOutputStream(fileName).getChannel();
            fc.write(ByteBuffer.wrap(output.getBytes()));
            fc.close();
        } catch (Exception e) {
            System.out.println("Exception! " + e.getMessage());
        }
        System.out.println("Test output written to file " + fileName);
    }
}
