package Utilities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Pair<K,V>
{
    private K key;
    private V value;

    public Pair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public K getKey()
    {
        return key;
    }

    public void setKey(K key)
    {
        this.key = key;
    }

    public V getValue()
    {
        return value;
    }

    public void setValue(V value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return '<' + key.toString() + ", " + value.toString() + '>';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(key, value);
    }

    public static <K,V> List<K> getKeys(Pair<K,V>[] pairs) {
        List<K> keys = new ArrayList<K>(pairs.length);
        for(Pair<K,V> pair : pairs) {
            keys.add(pair.getKey());
        }
        return keys;
    }

    public static <K,V> List<V> getValues(Pair<K,V>[] pairs) {
        List<V> values = new ArrayList<V>(pairs.length);
        for(Pair<K,V> pair : pairs) {
            values.add(pair.getValue());
        }
        return values;
    }

    public static <K,V> List<Pair<K,V>> zip(K[] keys, V[] values) {
        List<Pair<K,V>> l = new LinkedList<Pair<K,V>>();
        int length = Math.min(keys.length, values.length);
        for(int i = 0; i < length; i++) {
            l.add(new Pair<>(keys[i], values[i]));
        }
        return l;
    }
}
