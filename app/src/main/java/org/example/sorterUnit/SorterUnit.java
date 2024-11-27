package org.example.sorterUnit;

public interface SorterUnit<K> {
    K[] sort(K[] arrayToSort);
    String getDescription();
    String getVersion();
}
