package org.example;

public interface DataProvider<K> {
    K[] getData(int dataLength);
}
