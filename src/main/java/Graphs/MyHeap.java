package Graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MyHeap<T extends Comparable<T>> {

    ArrayList<T> array; // stores elements
    HashMap<T,Integer> locations; // for updating elements


    public MyHeap() {
        this.array = new ArrayList<>();
        this.locations = new HashMap<>();
    }

    public MyHeap(ArrayList<T> array) {
        this.array = new ArrayList<>();
        this.locations = new HashMap<>();
        for(int i = 0; i< array.size(); i++){
            this.array.add(array.get(i));
            locations.put(array.get(i),i);
        }
    }

    private int left(int k){
        if (2*k + 1 < array.size())
            return 2*k+1;
        return -1;
    }

    private int right(int k){
        if (2*k + 2 < array.size())
            return 2*k+2;
        return -1;
    }

    private int parent(int k){
        if (k <= 0 || k >= array.size())
            return -1;
        return (k-1) / 2;
    }

    private void bubbleUp(int i){
        if (i==0)
            return;
        int parentIndex = parent(i);
        if (array.get(parentIndex).compareTo(array.get(i)) > 0) {
            T temp = array.get(parentIndex);
            array.set(parentIndex, array.get(i));
            array.set(i, temp);
            bubbleUp(parentIndex);
            locations.put(array.get(parentIndex),parentIndex);
            locations.put(array.get(i),i);
        }
    }

    private void bubbleDown(int i){
        int currentIndex = i;
        int leftIndex = left(i);
        int rightIndex = right(i);
        //  left exists    and is smaller than current
        if (leftIndex >= 0 && array.get(leftIndex).compareTo(array.get(i)) < 0)
            currentIndex = leftIndex;
        //  right exists    and  is smaller than parent and left sibling
        if (rightIndex >= 0 && array.get(i).compareTo(array.get(rightIndex)) > 0)
            currentIndex = rightIndex;
        // If changes were made
        if (i != currentIndex) {
            Collections.swap(array, i, currentIndex);
            bubbleDown(currentIndex);
        }
    }

    public void add(T kirje){
        array.add(kirje);
        bubbleUp(array.size() - 1);
    }

    public T takeRoot(){
        // if there are elements
        if (array.size() > 0) {
            if (array.size() == 1) return array.remove(0);
            // put last to first, removing the first
            T first = array.get(0);
            T last = array.remove(array.size()-1);
            array.set(0, last);
            // bubble down the new root
            bubbleDown(0);
            return first;
        }
        return null;
    }

    public void update(T value, int newDistance){
        int valueIndex = findFromHeap(value);
        if (value instanceof DijkstraNode) {
            ((DijkstraNode) value).weight = newDistance;
            bubbleUp(valueIndex);
        }
        else throw new RuntimeException("update works with DijkstraNode only :)))) ");
    }

    private int findFromHeap(T value) {
        for (int i = 0; i < array.size(); i++)
            if (array.get(i).equals(value))
                return i;
        throw new RuntimeException("Did not find the value from heap.");
    }

    public void heapify(){
        MyHeap<T> temp = new MyHeap<>();
        for (T element : array)
            temp.add(element);
        array = temp.array;
    }

    public <T extends Comparable<T>> ArrayList<T> sort(ArrayList<T> sisend){
        MyHeap<T> heap = new MyHeap<>(sisend);
        ArrayList<T> sorted = new ArrayList<>();
        // keep taking and adding roots, result is sorted list
        for (int i = 0; i < sisend.size(); i++) {
            sorted.add(heap.takeRoot());
        }
        return sorted;
    }

    @Override
    public String toString() {
        return "MyHeap{array=" + array + '}';
    }

    public boolean isEmpty(){
        return this.array.isEmpty();
    }

    public int size() {
        return this.array.size();
    }
}
