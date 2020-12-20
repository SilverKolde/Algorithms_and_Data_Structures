package HashTable;

import java.util.LinkedList;

// Separate chaining with linked lists
public class HashTable2 {

    private LinkedList[] table;
    private int comparisonsCount;
    private boolean searchSuccessful;

    public HashTable2(int n) {
        this.table = new LinkedList[n];
        for (int i = 0; i < n; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int hash(int k) {
        return k % table.length;
    }

    public void add(Person person) {
        int index = hash(person.getId());
        LinkedList<Person> chain = table[index];
        chain.add(person);
    }

    public Person search(int id) {
        comparisonsCount = 0;
        int index = hash(id);
        LinkedList<Person> chain = table[index];
        for (Person person : chain) {
            comparisonsCount++;
            if (person.getId() == id) {
                searchSuccessful = true;
                return person;
            }
        }
        searchSuccessful = false;
        return null;
    }

    public Person remove(int id) {
        int index = hash(id);
        LinkedList<Person> chain = table[index];
        for (Person person : chain) {
            if (person.getId() == id) {
                chain.remove(person);
                return person;
            }
        }
        return null;
    }

    public int getComparisonsCount() {
        return comparisonsCount;
    }

    public boolean wasSearchSuccessful() {
        return searchSuccessful;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            out.append(i);
            out.append(": ");
            out.append(table[i]);
            out.append("\n");
        }
        return out.toString();
    }
}
