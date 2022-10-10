package me.spthiel.util.data;

import java.util.Objects;

public class Entry<K,V> {

    private K key;
    private V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Entry() {
    }

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entry<?,?> entry = (Entry<?,?>) o;
        return Objects.equals(key, entry.key) &&
                Objects.equals(value, entry.value);
    }
}
