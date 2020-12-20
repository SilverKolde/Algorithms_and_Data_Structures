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

    public static void main(String[] args) {
        Node<Integer> falseAVL = new Node<>(10,
                new Node<>(11),
                null);
//        AVLTree<Integer> avlTree = new AVLTree<>(falseAVL);
        AVLTree<Integer> avlTree = new AVLTree<>(new Node<>(10));

        System.out.println("\n=================================================================================");
        System.out.println("                              Inserting elements                                 ");
        System.out.println("=================================================================================\n");

        // Keep in mind that print method reserves two digits for each node, above that it may look distorted.
        int[] arr = generateInts(1000, 100);

        for (int i = 0; i < arr.length; i++) {
            System.out.println("After inserting: " + arr[i] + "\n");
            avlTree.insertNode(arr[i]);
            System.out.println(avlTree.toString());
            boolean isAVL = avlTree.isAVLTree();
            System.out.println("\nNodes in tree: " + countNodes(avlTree.root));
            if (i != arr.length-1)
                System.out.println("---------------------------------------------------------------------------------");

            if (!isAVL) throw new RuntimeException("After adding element nr " + i + ", something broke.");
        }

        System.out.println("\n=================================================================================");
        System.out.println("                               Deleting elements                                 ");
        System.out.println("=================================================================================\n");

        List<Integer> elements = treeToList(avlTree.root);
        Collections.shuffle(elements);

        for (int i = 0 ; i < elements.size() ; i++) {
            System.out.println("After removing: " + elements.get(i) + "\n");
            avlTree.removeValue(elements.get(i));
            System.out.println(avlTree.toString());
            boolean isAVL = avlTree.isAVLTree();
            System.out.println("\nNodes in tree: " + countNodes(avlTree.root));
            if (i != elements.size()-1)
                System.out.println("---------------------------------------------------------------------------------");

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
