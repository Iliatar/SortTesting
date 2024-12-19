package org.example.utils;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileClassLoader extends ClassLoader {
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
            throw new RuntimeException("Class " + className + " don't find in " + filePath);
        }
    }

    public static <K> K getClassInstance(Class<K> classObject) throws Exception {
        try {
            return classObject.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new Exception("Class " + classObject.getName() + " don't have no args constructor!");
        }
    }

    public static Class<?> loadClassFromFile(String className, String filePath) throws Exception {
        FileClassLoader fileClassLoader = new FileClassLoader();
        Class<?> classReference = fileClassLoader.findClass(className, filePath);
        return classReference;
    }
}
