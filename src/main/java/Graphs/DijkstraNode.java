package Graphs;


public class DijkstraNode implements Comparable<DijkstraNode> {
    int weight;
    int indexForTownname;
    String name;

    public DijkstraNode(String name, int indexForTownname, int weight) {
        this.indexForTownname = indexForTownname;
        this.weight = weight;
        this.name = name;
    }

    @Override
    public int compareTo(DijkstraNode dijkstraNode) {
        return Integer.compare(this.weight, dijkstraNode.weight);
    }

    @Override
    public String toString(){
        return name;
    }
}
