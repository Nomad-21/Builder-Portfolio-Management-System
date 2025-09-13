package tech.zeta.util;

public class Param<K,V>{
    public K key;
    public V value;
    public String type;

    public Param(K key, V value){
        this.key = key;
        this.value = value;
        this.type = value.getClass().getSimpleName();
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
