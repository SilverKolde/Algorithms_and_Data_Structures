package Recursion;

public class MyMergeSort {

    /**
     * The algorithm splits the original array in two recursively until there's only one element left (base).
     * Then merges the two arrays together (in the correct order), ultimately returning the sorted list.
     *
     * @param arr the original array.
     * @return the sorted array.
     */
    public static int[] sort(int[] arr) {
        //base
        if (arr.length == 1) return arr;

        // splitting the array
        int[] firstHalf = new int[arr.length/2];
        int[] secondHalf = new int[arr.length - firstHalf.length];
        System.arraycopy(arr, 0, firstHalf, 0, arr.length/2);
        System.arraycopy(arr, arr.length/2, secondHalf, 0, arr.length - firstHalf.length);

        // recursive calls
        firstHalf = sort(firstHalf);
        secondHalf = sort(secondHalf);

        return merge(firstHalf, secondHalf);
    }

    /**
     * This method takes two sorted lists (they are sorted because the calling of this method starts at the recursion base level)
     * and puts them together with a for-cycle.
     *
     * @param firstHalf .
     * @param secondHalf .
     * @return combined and sorted array of firstHalf and secondHalf.
     */
    public static int[] merge(int[] firstHalf, int[] secondHalf) {

        int[] sorted = new int[firstHalf.length + secondHalf.length];

        // indexes of firstHalf and secondHalf
        int a = 0, b = 0;

        for (int i = 0; i < sorted.length; i++) {

            // If we reach the end of one array
            if (a == firstHalf.length) {
                sorted[i] = secondHalf[b];
                b++;
                continue; // move on to the next 'i'
            }
            if (b == secondHalf.length) {
                sorted[i] = firstHalf[a];
                a++;
                continue;
            }

            // We still have elements in both arrays
            if (firstHalf[a] < secondHalf[b]) {
                sorted[i] = firstHalf[a];
                a++;
            }
            else {
                sorted[i] = secondHalf[b];
                b++;
            }
        }
        return sorted;
    }
}
