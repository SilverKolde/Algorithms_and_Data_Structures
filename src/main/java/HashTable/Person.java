package HashTable;

public class Person implements Comparable<Person> {
    private final int id;
    private final String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "Person nr. " + id + ": " + name;
    }

    @Override
    public int compareTo(Person o) {
        return Integer.compare(this.id, o.id);
    }
}