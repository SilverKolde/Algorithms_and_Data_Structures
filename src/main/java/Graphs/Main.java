package Graphs;

import java.io.FileNotFoundException;
import java.util.*;

/*****************************************************************************
 * Algorithms and Data Structures. LTAT.03.005
 * 2020/2021 autumn
 *
 * Subject:
 *      Dijkstra's algorithm
 *
 * Author:
 *      Silver Kolde
 *
 * Task:
 *      Find shortest paths from one Estonian city to another
 *      with specified max range.
 *
 *****************************************************************************/

public class Main {

    public static void main(String[] args) {
        try {
            String fileName = "direct_paths.txt";
            List<String> starts = Arrays.asList(new Dijkstra(fileName).townNames);
            List<String> destinations = new ArrayList<>(starts);
            Collections.shuffle(starts);
            Collections.shuffle(destinations);

            System.out.println("---------------------------------------------------------------------------------");
            for (int i = 0; i < 10; i++) {
                String destination = destinations.get(i);
                String start = starts.get(i);
                int rangeLimit = 30 + new Random().nextInt(100);
                Dijkstra dijkstra = new Dijkstra(fileName);

                System.out.println("From:         " + start);
                System.out.println("To:           " + destination);
                System.out.println("Direct path:  " + dijkstra.directDistance(start, destination) + " km");
                System.out.println("Max range:    " + rangeLimit + " km");

                System.out.println("\nThe path you want to take is: ");
                System.out.println(dijkstra.findShortestPath(start, destination, rangeLimit));
                System.out.println("\n---------------------------------------------------------------------------------\n");
                Thread.sleep(1000);
            }
        } catch (FileNotFoundException | InterruptedException e) {
            System.out.println("Linnade kauguste faili ei leitud!");
        }
    }

}
