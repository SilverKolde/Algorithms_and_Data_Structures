package Recursion.AVLTrees;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>>{
    T value;
    Node<T> right;
    Node<T> left;
    private int x; // position for printing
    int height;
    // balance shows the height difference of left and right subtree
    // If -2 or 2, avl tree needs balancing. If -1, 0 or 1, everything ok.
    int balance;

    public Node(T value, Node<T> left, Node<T> right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public Node(T value) {
        this.value = value;
    }

    public T getValue(){
        return this.value;
    }

// ============================ Methods for printing ================================================================ //

    @Override
    public String toString() {
        int width = markPositions(this, 1) * 2; // *2, sest reserveerime kaks kohta igale tipule
        int height = findHeight(this);

        StringBuilder result = new StringBuilder("");
        for (int i = 1; i <= height; i++) {
            String row = printRow(this, i, new StringBuilder(" ".repeat(width)));
            result.append(row).append("\n");
        }
        return result.toString();
    }

    private String printRow(Node<T> node, int level, StringBuilder row) {
        if (node == null)
            return row.toString();
        if (level == 1) {
            String str = String.valueOf(node.value);
            str = (str.length() == 1) ? " "+str : str;
            row.replace(node.x*2, (node.x*2)+2, str);
            return row.toString();
        }
        printRow(node.left, level-1, row);
        printRow(node.right, level-1, row);

        return row.toString();
    }

    private int markPositions(Node<T> node, int position) {
        if (node == null)
            return position-1;
        int left = markPositions(node.left, position);
        node.x = left + 1;
        int right = markPositions(node.right, left + 2);
        return right;
    }

    private int findHeight(Node<T> node) {
        if (node == null)
            return 0;
        int leftHeight = 0, rightHeight = 0;
        leftHeight += 1 + findHeight(node.left);
        rightHeight += 1 + findHeight(node.right);
        return Math.max(leftHeight, rightHeight);
    }

    @Override
    public int compareTo(Node<T> o) {
        return this.value.compareTo(o.value);
    }
}
