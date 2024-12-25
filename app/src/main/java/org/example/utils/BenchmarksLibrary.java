package org.example.utils;

import org.example.sorterUnit.BenchmarkIntegerSorter;
import org.example.sorterUnit.SorterUnit;

import java.util.HashMap;
import java.util.Map;

public class BenchmarksLibrary {
    private static Map<Class, Object> library;

    static {
        library = new HashMap<>();
        library.put(Integer.class, new BenchmarkIntegerSorter());
    }

    public static Object getBenchmarkUnit(Class classObject) {
        if (!library.containsKey(classObject)) {
            throw new RuntimeException("Benchmark Library doesn't contain benchmark unit for class " + classObject.getName());
        }

        return library.get(classObject);
    }
}
