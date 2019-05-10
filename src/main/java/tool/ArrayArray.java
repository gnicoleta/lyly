package tool;

import java.util.ArrayList;

public class ArrayArray<T> {
    private ArrayList<T> array = new ArrayList<T>();

    public ArrayArray(ArrayList<T> arr) {
        this.array = arr;
    }

    public ArrayArray() {

    }

    public ArrayList<T> getArray() {
        return this.array;
    }

    public void setArray(ArrayList<T> arr) {
        for(T t: arr) {
            this.array.add(t);
        }
    }

    public void addElement(T t) {
        array.add(t);
    }

    public void clearArray() {
        this.array.clear();
    }

}
