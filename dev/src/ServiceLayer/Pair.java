package ServiceLayer;

public  class Pair<T,V> {
    T first;
    V second;

    public Pair(T first, V second){
        this.first=first;
        this.second=second;
    }

    public T getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }
}
