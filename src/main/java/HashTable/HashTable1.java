package HashTable;

// Open addressing with linear probing
public class HashTable1 {
    private Person[] table;
    private final int step = 1; // step by which to move, if desired place is not vacant
    private int comparisonsCount;
    private boolean searchSuccessful;

    public HashTable1(int n) {
        this.table = new Person[n];
    }

    private int hashFunction(int k) {
        int m = table.length;
        double T = (Math.sqrt(5) - 1) / 2;
        return (int) Math.floor(m*(k*T - Math.floor(k*T)));
    }

    public void add(Person person) {
        int index = hashFunction(person.getId()); // find where to place a person
        int indexBefore = index; // remember it, to check if table's full

        // loop until we find a free spot
        while (table[index] != null) {
            index += step;
            index %= table.length;
            if (index == indexBefore)
                throw new RuntimeException("Hashtable if full!");
        }
        table[index] = person;
    }

    public Person search(int id) {
        comparisonsCount = 0;
        int index = hashFunction(id); // find the initial index
        int indexBefore = index; // remember it

        while (table[index] != null) {
            Person person = table[index];
            comparisonsCount++;
            if (person.getId() == id) {
                searchSuccessful = true;
                return person;
            }
            index += step;
            index %= table.length;
            if (index == indexBefore) break;
        }
        searchSuccessful = false;
        return null; // looped the whole round, didn't find id
    }

    public Person remove(int id) {
        // We can't be sure on which index the Person is, so we have to rewrite search
        int index = hashFunction(id);
        int indexBefore = index;
        Person person = null;
        while (table[index] != null) {
            person = table[index];
            if (person.getId() == id)
                break; // move on to removing
            index += step;
            index %= table.length;
            if (index == indexBefore)
                return null; // person not in table.
        }
        if (person != null) { // if person found
            table[index] = null; // delete
            index += step; // check next person
            index %= table.length;
            while (table[index] != null) { // move others if needed
                Person personForMoving = table[index];
                table[index] = null;
                this.add(personForMoving);
                index += step;
                index %= table.length;
            }
        }
        // finally return the person who was removed.
        return person;
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