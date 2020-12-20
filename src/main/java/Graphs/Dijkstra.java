package Graphs;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dijkstra {

    // the shortest paths (if there is no range limit)
    public int[][] distances;
    public String[] townNames;


    public Dijkstra(String file) throws FileNotFoundException {
        readData(file);
    }

    /**
     * Finds shortest path from start to destination within given range limit.
     * @return Result object that contains route length and all the stops on the route
     */
    public Result findShortestPath(String start, String destination, int rangeLimit) {

        int[] distanceFromStart = new int[distances.length];
        int[] cameFrom = new int[distances.length];
        // Initially the distance is extremely high
        Arrays.fill(distanceFromStart, Integer.MAX_VALUE / 2);
        // We don't know where to go initially, so indexes will be negative
        Arrays.fill(cameFrom, -1);

        // We do know that it costs nothing to get to "start"
        distanceFromStart[index(start)] = 0;

        // Convert each unvisited town to separate Node
        MyHeap<DijkstraNode> unvisited = new MyHeap<>();
        for (int i = 0; i < townNames.length; i++) {
            DijkstraNode townNode =
                    new DijkstraNode(townNames[i], i, distanceFromStart[i]); // name is just for debug
            unvisited.add(townNode);
        }

        while (!unvisited.isEmpty()) {
            // Take the node with lowest weight
            DijkstraNode a = unvisited.takeRoot();

            // At each step we'll loop through the unvisited nodes
            // and mark up new shorter distances, if they're discovered

            List<DijkstraNode> townsNotChecked = unvisited.array;
            for (DijkstraNode b : townsNotChecked) {
                int distBetweenAandB = distances[a.indexForTownname][b.indexForTownname];
                int newDistance = distanceFromStart[a.indexForTownname] + distBetweenAandB;

                //    found shorter distance                            that is not above range limit
                if (newDistance < distanceFromStart[b.indexForTownname] && distBetweenAandB <= rangeLimit) {
                    distanceFromStart[b.indexForTownname] = newDistance; // remember dist from start
                    cameFrom[b.indexForTownname] = a.indexForTownname; // and where we came from
                    unvisited.update(b, newDistance); // update the heap
                }
            }
        }

        // Did not find a way
        if (cameFrom[index(destination)] < 0)
            return new Result(Arrays.asList(start, destination), -1);

        // Found a way
        int shortestDistance = distanceFromStart[index(destination)];
        return new Result(townListToMarkPath(cameFrom, destination), shortestDistance);
    }

    private List<String> townListToMarkPath(int[] cameFrom, String destination) {
        int previousTownIndex = cameFrom[index(destination)];
        List<String> path = new ArrayList<>();
        path.add(destination);
        while (previousTownIndex != -1) {
            String previousTown = townNames[previousTownIndex];
            path.add(0, previousTown);
            previousTownIndex = cameFrom[previousTownIndex];
        }
        return path;
    }

    // It's a bit ugly to throw exceptions, but I'm tired as ...
    private int index(String town) {
        for (int i = 0; i < townNames.length; i++) {
            if (townNames[i].equals(town))
                return i;
        }
        throw new RuntimeException("I don't know this town...");
    }

    private void readData(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            townNames = br.readLine().split("\t");
            distances = new int[townNames.length][townNames.length];
            String in = br.readLine();
            int i = 0;
            while (in != null) {
                String[] linePieces = in.split("\t");
                for (int j = 1; j < linePieces.length; j++) {
                    if (!linePieces[j].equals("")) {
                        distances[i][j - 1] = Integer.parseInt(linePieces[j]);
                    } else {
                        distances[i][j - 1] = 0;
                    }
                }
                i++;
                in = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Result {

    List<String> path;
    int pathLength;

    public Result(List<String> path, int pathLength) {
        this.path = path;
        this.pathLength = pathLength;
    }

    public String toString() {
        if(pathLength != -1) {
            return String.join(" > ", path) + "\nTotal distance: " + pathLength;
        } else {
            return ("Towns " + path.get(0) + " and " + path.get(path.size() - 1)
                    + " are not reachable within specified range.");
        }
    }
}