package Recursion.AVLTrees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*****************************************************************************
 * Algorithms and Data Structures. LTAT.03.005
 * 2020/2021 autumn
 *
 * Subject:
 *      AVL Trees
 *
 * Author:
 *      Silver Kolde
 *
 * Task:
 *      Create and test a self-balanced AVL tree.
 *
 *****************************************************************************/

public class TestAVLTree {

    public static void main(String[] args) throws InterruptedException {
        Node<Integer> falseAVL = new Node<>(10,
                new Node<>(11),
                null);
//        AVLTree<Integer> avlTree = new AVLTree<>(falseAVL);
        AVLTree<Integer> avlTree = new AVLTree<>(new Node<>(10));

        System.out.println("\n=================================================================================");
        System.out.println("                              Inserting elements                                 ");
        System.out.println("=================================================================================\n");
        Thread.sleep(1000);

        // Keep in mind that print method reserves two digits for each node, above that it may look distorted.
        int[] arr = generateInts(80, 100);

        for (int i = 0; i < arr.length; i++) {
            avlTree.insertNode(arr[i]);
            boolean isAVL = avlTree.isAVLTree();
            System.out.println("Inserted: " + arr[i] +
                    "       Nodes in tree: " + countNodes(avlTree.root) +
                    "       is AVL? " + isAVL + "\n");
            System.out.println(avlTree.toString());
            if (i != arr.length-1)
                System.out.println("-------------------------------------------------------------------------------\n");

            if (i > 9 && i < 77) Thread.sleep(100);
            else Thread.sleep(1000);

            if (!isAVL) throw new RuntimeException("After adding element nr " + i + ", something broke.");
        }

        System.out.println("\n\n=================================================================================");
        System.out.println("                               Deleting elements                                 ");
        System.out.println("=================================================================================\n\n");
        Thread.sleep(1000);

        List<Integer> elements = treeToList(avlTree.root);
        Collections.shuffle(elements);

        for (int i = 0 ; i < elements.size() ; i++) {
            avlTree.removeValue(elements.get(i));
            boolean isAVL = avlTree.isAVLTree();
            System.out.println("Removed: " + elements.get(i) +
                    "       Nodes in tree: " + countNodes(avlTree.root) +
                    "       is AVL? " + isAVL + "\n");
            System.out.println(avlTree.toString());
            if (i != elements.size()-1) {
                System.out.println("\n-------------------------------------------------------------------------------");
                if (i > 10 && i < 78) Thread.sleep(100);
                else Thread.sleep(1000);
            }

            if (!isAVL) throw new RuntimeException("After removing element nr " + i + ", something broke.");
        }
        System.out.println("=================================================================================");
    }

// ===================================== Helper methods ============================================================= //

    
    private static List<Integer> treeToList(Node<Integer> root) {
        return toList(root, new ArrayList<>());
    }

    private static ArrayList<Integer> toList(Node<Integer> root, ArrayList<Integer> current) {
        if (root == null)
            return current;
        current.add(root.getValue());
        current = toList(root.left, current);
        current = toList(root.right, current);
        return current;
    }

    private static int countNodes(Node<Integer> node) {
        if (node == null)
            return 0;
        return 1 + countNodes(node.right) + countNodes(node.left);
    }

    public static int[] generateInts(int arrayLength, int elementRange) {
        int[] arr = new int[arrayLength];
        for (int i = 0; i < arrayLength; i++)
            arr[i] = (int) Math.round(Math.random()*elementRange);
        return arr;
    }
}
