package org.example.utils;

import java.io.*;

public class SorterClassLoader extends ClassLoader {
    @Override
    public Class findClass(String className, String filePath) {
        var byteArray = loadClassFromFile(filePath);
        return defineClass(className, byteArray, 0, byteArray.length);
    }

    private byte[] loadClassFromFile(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (IOException e) {
            System.out.println(e.fillInStackTrace());
        }

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        int nextValue = 0;
        try {
            while((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        var buffer = byteStream.toByteArray();
        return buffer;
    }
}
