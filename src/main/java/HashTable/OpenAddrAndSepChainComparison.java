package HashTable;

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
 *   Check how much does the vacancy of the hash table affects the efficiency.
 *
 *   For that we will create two arrays of hash tables (10 tables in each array):
 *      * tables1 - linear probing
 *      * tables2 - separate chaining with linked lists
 *
 *   Each hash table's size is 1000.
 *   We fill those tables with random Person objects, so that the
 *      tables[0] are 10% filled
 *      tables[1] are 20% filled
 *                ...
 *      tables[9] are 99% filled
 *
 *   Then we look up random persons from tables and
 *   see how many lookups were made to reach a Result (person exists in table or not)
 *
 *****************************************************************************/

public class OpenAddrAndSepChainComparison {
    public static void main(String[] args) {

        // Size of hash tables
        int n = 1000;
        // Number of hash tables (pairs of hash tables, actually)
        int k = 10;
        HashTable1[] tables1 = new HashTable1[k]; // Open addressing
        HashTable2[] tables2 = new HashTable2[k]; // Separate chaining

        // Filling up the hash tables 10%, 20%, ... 99%.
        for (int i = 0; i < k; i++) {
            Person[] persons;
            persons = (i < 9) ? new Person[(i+1)*100] : new Person[990];
            fillUpWithRandomIDs(persons);
            tables1[i] = fillTable1WithRandomPersons(persons, n);
            tables2[i] = fillTable2WithRandomPersons(persons, n);
        }

        // ID-s varying in the same range (from 0 to 10000)
        Person[] personsForSearching = fillUpWithRandomIDs(new Person[n]);

        // end results will be saved here
        int[] t1SuccessComparisonCounts = new int[k];
        int[] t1FailComparisonCounts = new int[k];
        int[] t2SuccessComparisonCount = new int[k];
        int[] t2FailComparisonCounts = new int[k];

        int[] t1SSCount = new int[k]; // Successful Search Count - person was found
        int[] t1FSCount = new int[k]; // Failed Search Count - person was not found
        int[] t2SSCount = new int[k];
        int[] t2FSCount = new int[k];


        for (int i = 0; i < k; i++) {
            HashTable1 table1 = tables1[i]; // persons in table  100, 200, ... , 900, 990
            HashTable2 table2 = tables2[i]; //                   i=0, i=1, ... , i=8, i=9

            int t1SuccessComparisons = 0;
            int t1FailComparisons = 0;
            int t1PersonsFound = 0;
            int t1PersonsNotFound = 0;

            int t2SuccessComparisons = 0;
            int t2FailComparisons = 0;
            int t2PersonsFound = 0;
            int t2PersonsNotFound = 0;

            for (Person person : personsForSearching) {
                int id = person.getId();

                table1.search(id);
                int operations1 = table1.getComparisonsCount();
                if (table1.wasSearchSuccessful()) {
                    t1SuccessComparisons += operations1;
                    t1PersonsFound++;
                } else {
                    t1FailComparisons += operations1;
                    t1PersonsNotFound++;
                }

                table2.search(id);
                int operations2 = table2.getComparisonsCount();
                if (table2.wasSearchSuccessful()) {
                    t2SuccessComparisons += operations2;
                    t2PersonsFound++;
                } else {
                    t2FailComparisons += operations2;
                    t2PersonsNotFound++;
                }
            }
            t1SuccessComparisonCounts[i] = t1SuccessComparisons;
            t1FailComparisonCounts[i] = t1FailComparisons;
            t2SuccessComparisonCount[i] = t2SuccessComparisons;
            t2FailComparisonCounts[i] = t2FailComparisons;
            t1SSCount[i] = t1PersonsFound;
            t1FSCount[i] = t1PersonsNotFound;
            t2SSCount[i] = t2PersonsFound;
            t2FSCount[i] = t2PersonsNotFound;
        }

        String[] percents = {"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "99%"};
        System.out.println("          Table 1 - Open addressing                   Table 2 - Separate chaining    ");
        for (int i = 0; i < 10; i++) {
            System.out.printf("%6s%12s%6s%13s%10s%12s%10s%13s%6s%n",
                    " "+percents[i]+" |", " successes: ", " "+t1SSCount[i]+" |", " comparisons: ", " "+t1SuccessComparisonCounts[i]+" ||",
                                          " successes: ", " "+t2SSCount[i]+" |", " comparisons: ", " "+t2SuccessComparisonCount[i]);
            System.out.printf("%6s%12s%6s%13s%10s%12s%10s%13s%6s%n",
                    "     |", " fails:     ", " "+t1FSCount[i]+" |", " comparisons: ", " "+t1FailComparisonCounts[i]+" ||",
                    " fails:     ", " "+t2FSCount[i]+" |", " comparisons: ", " "+t2FailComparisonCounts[i]);
            System.out.println("------------------------------------------------------------------------------------------");
        }

        System.out.println("\nFrom the table above we can conclude, " +
                "that when using open addressing we should never fill up the hashtable more than 50%. " +
                "Ideally maybe 20-30%. Because it gets too slow. " +
                "\nUsing separate chaining we make less comparisons, but memory use is inefficient due to use of linked lists.");

    }

// ======================================= Helper Methods =========================================================== //

    private static HashTable2 fillTable2WithRandomPersons(Person[] persons, int hashTableSize) {
        HashTable2 table = new HashTable2(hashTableSize);
        for (Person person : persons) {
            table.add(person);
        }
        return table;
    }

    private static HashTable1 fillTable1WithRandomPersons(Person[] persons, int hashTableSize) {
        HashTable1 table = new HashTable1(hashTableSize);
        for (Person person : persons) {
            table.add(person);
        }
        return table;
    }

    private static Person[] fillUpWithRandomIDs(Person[] persons) {
        for (int j = 0; j < persons.length; j++) {
            persons[j] = new Person(new Random().nextInt(10000), "John Doe");
        }
        return persons;
    }
}
