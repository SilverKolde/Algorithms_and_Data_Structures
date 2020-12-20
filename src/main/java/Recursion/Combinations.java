package Recursion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/*****************************************************************************
 * Algorithms and Data Structures. LTAT.03.005
 * 2020/2021 autumn
 *
 * Subject:
 *      Recursive combinations.
 *
 * Author: Silver Kolde
 *
 * Task:
 *  Array consists of 26 floats that represent the weights of tools.
 *  Write a program that
 *      a) checks that if there's a combination of no more than 13 tools that have a combined weight of exactly 10 kg;
 *      b) finds a set of tools of which combined weight exceeds 10kg as little as possible
 *
 *****************************************************************************/

public class Combinations {
    public static void main(String[] args) {

//        double[] tools = generateToolWeights(26, 10); // only light tools (less than 10kg)
//        double[] tools = {35.0, 34.0, 33.0, 32.0, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 10.0, 3.0};
//        double[] tools = {9.1, 0.9, 3, 4, 5, 1};
        double[] tools = generateToolWeights(26, 35);
        System.out.println("The initial toolset:                             " + Arrays.toString(tools));
        tools = eliminateHeavierTools(tools);
        System.out.println("Toolset after elimination of too heavy elements: " + Arrays.toString(tools) + "\n");

        // part a)
        ArrayList<Double> lightestSet = findLightestA(tools, 0, new ArrayList<>());
        if (lightestSet.size() > 0) {
            System.out.println("We have found a set of tools with a combined weight of 10kg: " + lightestSet + "\n");
        }
        // part b)
        else {
            System.out.println("Couldn't find a toolset with a combined weight of 10kg.");
            System.out.println("Searching for a set that is closest to 10kg (but still more than 10kg)...\n");
            lightestSet = findLightestB(tools);
            System.out.println("The lightest set we found: " + lightestSet);
            System.out.println("It's weight: " + sum(lightestSet));
        }
    }

    /**
     *
     * @param tools - from here we pick single tools
     * @param index - from this index
     * @param toolset - add them here
     * @return A first <toolset> that is found to have combined weight of 10kg
     */
    private static ArrayList<Double> findLightestA(double[] tools, int index, ArrayList<Double> toolset) {

        // base case
        if (toolset.size() > 13 || sum(toolset) == 10 || index == tools.length)
            return (sum(toolset) == 10) ? toolset : new ArrayList<>();

        ArrayList<Double> result = new ArrayList<>();
        for (int i = index; i < tools.length; i++) {
            toolset.add(tools[i]);
            result = findLightestA(tools, i + 1, toolset);

            // if Result is not empty, we have found a 10kg set
            if (result.size() != 0)
                return result;
            // otherwise remove the last element and keep looking
            toolset.remove(toolset.size() - 1); // keeping duplicates away
        }

        return result;
    }

    /**
     * Eliminates too heavy tools and directs workload to another method, adding parameters
     */
    private static ArrayList<Double> findLightestB(double[] tools) {
        tools = eliminateHeavierTools(tools);
        return workerMethodB(eliminateHeavierTools(tools), 0, new ArrayList<>(), new ArrayList<>());
    }

    /**
     *
     * @param tools array of tools
     * @param index to keep track which tools we have already put in current subset
     * @param current the set we are observing right now
     * @param minToolset the best suitable set we have found so far
     * @return the best possible set according to requirements
     */
    private static ArrayList<Double> workerMethodB(double[] tools, int index, ArrayList<Double> current, ArrayList<Double> minToolset) {
        // First, we sum arrays
        double currentSum = sum(current),
                minSum = sum(minToolset);
        // If we're about to exceed the number of tools allowed
        if (current.size() >= 13) {
            // we return minimal we've found so far (current, if minimal not initialized)
            return (minSum != 0) ? new ArrayList<>(minToolset) : new ArrayList<>(current);
        }
        // If the set weighs enough
        //                  and min not found yet or current is smaller than min
        if (currentSum > 10 && (minSum == 0 || currentSum < minSum)) {
            return new ArrayList<>(current);
        }
        // Loop through the initial set of tools
        for (int i = index; i < tools.length; i++) {
            // adding a tool
            current.add(tools[i]);
            // recursive call to look for a better set
            minToolset = workerMethodB(tools, i+1, current, minToolset);
            // removing the tool with which we have already tried all combinations
            current.remove(current.size()-1);
        }
        return minToolset;
    }

//==================================== Helper methods ==================================================================


    /**
     * This method finds lightest tool that is >=10 and removes heavier tools.
     * @return An array with elements from 0 to 10, one element is bigger.
     */
    private static double[] eliminateHeavierTools(double[] tools) {
        ArrayList<Double> lightertools = new ArrayList<>();
        double min = Integer.MAX_VALUE;

        // Only take elements that are small enough
        for (double tool : tools) {
            if (tool < 10) lightertools.add(tool);
            else if (tool < min) {
                lightertools.add(tool);
                min = tool;
            }
        }
        // Now there may still be too heavy elements in 'lightertools' that can be removed,
        //                                                              (e.g the last element of 'tools' was 'min')
        for (Iterator<Double> iterator = lightertools.iterator(); iterator.hasNext();) {
            Double tool = iterator.next();
            if (tool > min) iterator.remove();
        }
        // copy to an array
        double[] smallerToolSet = new double[lightertools.size()];
        for (int i = 0; i < lightertools.size(); i++)
            smallerToolSet[i] = lightertools.get(i);

        return smallerToolSet;
    }

    /**
     * Generates n tools from 0 kg to maxToolWeight kg.
     */
    private static double[] generateToolWeights(int numberOfTools, double maxToolWeight) {
        double[] weights = new double[numberOfTools];
        for (int i = 0; i < numberOfTools; i++) {
            double weight = Math.round(Math.random()* maxToolWeight * 10) / 10.0;
            if (weight == 0) continue;
            weights[i] = weight;
        }
        return weights;
    }

    private static double sum(ArrayList<Double> tools){
        double sum = 0;
        for (Double tool : tools) sum += tool;
        return sum;
    }
}
