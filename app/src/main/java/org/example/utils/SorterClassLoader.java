package org.example.utils;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SorterClassLoader extends ClassLoader {
    @Override
    public Class findClass(String className, String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + filePath + " not found");
        }

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        int nextValue = 0;
        try {
            while((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        var buffer = byteStream.toByteArray();
        try {
            return defineClass(className, buffer, 0, buffer.length);
        } catch (NoClassDefFoundError e) {
            throw new RuntimeException(className + " don't find in " + filePath);
        }
    }
}
