package org.example.dataProvider;

public interface DataProvider<K> {
    K[] getData(int dataLength);
}
