package Varia;

import HashTable.Person;

import java.util.ArrayList;
import java.util.Random;

/*****************************************************************************
 * Algorithms and Data Structures. LTAT.03.005
 * 2020/2021 autumn
 *
 * Subject:
 *      Hash tables
 *
 * Author:
 *      Silver Kolde
 *
 * Task:
 *      Compare merge sort and hash table sort.
 *
 *****************************************************************************/

public class MergeVsHashTableSort {

    public static void main(String[] args) {
        Person[][] personMatrice = personMatrice();

        System.out.println("              Average speeds in ms");
        System.out.println("Array length   Merge   Hashtable");
        for (Person[] people : personMatrice) {
            long avgSpeedMerge = run5timesReturnAverage(people, "merge");
            long avgSpeedHash = run5timesReturnAverage(people, "hashtable");
            System.out.printf("%14s%8s%10s%n", people.length+" |", " "+avgSpeedMerge+" |", " "+avgSpeedHash);
        }
    }

// ====================================== Hashtable Sort ============================================================ //

    private static void hashtableSort(Person[] persons) {
        // Find the maximum element
        int max = Integer.MIN_VALUE;
        for (Person person : persons) {
            if (person.getId() > max)
                max = person.getId();
        }

        // Now we'll make new array that is big enough to place each Person to index which is equal to person's id
        // E.g. if Person's id == 25378, we'll place him/her to ArrayList which is located at hash[25378]

        // Firstly, make empty 2-dimensional array
        ArrayList[] hash = new ArrayList[max + 1];
        for (int i = 0; i < hash.length; i++) {
            hash[i] = new ArrayList<Person>();
        }

        // Then place the person to correct ArrayList
        for (Person person : persons) {
            hash[person.getId()].add(person);
        }

        // Now when we have persons placed on the correct positions, we'll move them out of ArrayLists,
        // into a new array of Persons
        Person[] ppl = new Person[persons.length];
        int index = 0;

        for (int i = 0; i <= max; i++) {

            // if present
            if (!hash[i].isEmpty()) {

                // copy all the persons from this ArrayList to newly created array
                for (int j = 0; j < hash[i].size(); j++) {
                    ppl[index] = (Person) hash[i].get(j);
                    index++;
                }
            }
        }
    }

// ======================================== Merge Sort ============================================================== //

    public static Person[] mergesort(Person[] persons) {
        if (persons.length == 1) return persons;
        Person[] firstHalf = new Person[persons.length/2];
        Person[] secondHalf = new Person[persons.length - firstHalf.length];
        System.arraycopy(persons, 0, firstHalf, 0, persons.length/2);
        System.arraycopy(persons, persons.length/2, secondHalf, 0, persons.length - firstHalf.length);
        firstHalf = mergesort(firstHalf);
        secondHalf = mergesort(secondHalf);
        return merge(firstHalf, secondHalf);
    }

    public static Person[] merge(Person[] firstHalf, Person[] secondHalf) {
        Person[] sorted = new Person[firstHalf.length + secondHalf.length];
        int a = 0, b = 0;
        for (int i = 0; i < sorted.length; i++) {
            if (a == firstHalf.length) {
                sorted[i] = secondHalf[b];
                b++;
                continue;
            }
            if (b == secondHalf.length) {
                sorted[i] = firstHalf[a];
                a++;
                continue;
            }
            if (firstHalf[a].getId() < secondHalf[b].getId()) {
                sorted[i] = firstHalf[a];
                a++;
            } else {
                sorted[i] = secondHalf[b];
                b++;
            }
        }
        return sorted;
    }

// ================================= Helper Methods ================================================================= //

    private static Person[][] personMatrice() {
        Person[][] m = new Person[6][];
        m[0] = randomPersonList(250000);
        m[1] = randomPersonList(500000);
        m[2] = randomPersonList(1000000);
        m[3] = randomPersonList(2500000);
        m[4] = randomPersonList(5000000);
        m[5] = randomPersonList(10000000);
        return m;
    }

    private static Person[] randomPersonList(int length) {
        Person[] persons = new Person[length];
        for (int i = 0; i < length; i++) {
            persons[i] = new Person(new Random().nextInt(99999)+1, "John Doe");
        }
        return persons;
    }

    private static long run5timesReturnAverage(Person[] persons, String algorithm) {
        long sum = 0, n=5;
        for (int i = 0; i < n; i++) {
            long start = System.currentTimeMillis();
            switch (algorithm) {
                case "merge":
                    mergesort(persons);
                    break;
                case "hashtable":
                    hashtableSort(persons);
                    break;
            }
            long stop = System.currentTimeMillis();
            sum += stop - start;
        }
        return sum/n;
    }
}
