package Recursion.AVLTrees;

public class AVLTree<T extends Comparable<T>> {

    public Node<T> root;

    public AVLTree(Node<T> root) {
        if (!isAVL(root)) throw new IllegalArgumentException("Constructor takes only AVLTrees.");
        this.root = root;
    }

    public boolean isAVLTree() {
        return isAVL(root);
    }

    private boolean isAVL(Node<T> node) {
        // Base case - if we reach a leaf, everything was OK.
        if (node == null || (node.left == null && node.right == null))
            return true;
        // Assume it's not AVLTree
        boolean isAVLTree;
        if (node.left != null) {
            int compare = node.left.value.compareTo(node.value);
            isAVLTree = compare <= 0 && isAVL(node.left);
        } else {
            int compare = node.right.value.compareTo(node.value);
            isAVLTree = compare >= 0 && isAVL(node.right);
        }
        // Every leaf is reached, conditions checked at each step, return Result.
        return isAVLTree;
    }

// ================================================================================================================== //
//                                                                                                                    //
//                                                  INSERTION                                                         //
//                                                                                                                    //
// ================================================================================================================== //

    public void insertNode(T newValue) {
        if (newValue != null)
            root = insert(root, newValue);
    }

    // recursive insertion
    private Node<T> insert(Node<T> root, T newValue) {
        // Base case - if node becomes null, then this is the place to insert the value.
        if (root == null)
            return new Node<>(newValue);

        // At each level root changes,
        //  we make a comparison of current node value and new value
        //    to decide whether to place new value in the left or right branch
        int compare = newValue.compareTo(root.value);
        if (compare < 0) {
            root.left = insert(root.left, newValue);
        } else {
            root.right = insert(root.right, newValue);
        }
        // update records of current node
        updateHeightAndBalance(root);

        // balance the tree at each step when unwinding from recursion
        return balance(root);
    }

    // Balance is explained in Node class.
    // Heights start from 0.
    private void updateHeightAndBalance(Node<T> node) {
        int leftHeight = (node.left == null) ? -1 : node.left.height;
        int rightHeight = (node.right == null) ? -1 : node.right.height;
        node.height = 1 + Math.max(leftHeight, rightHeight);
        node.balance = rightHeight - leftHeight;
    }

    /**
     * Balancing can occur in 4 different occasions:
     *      LEFT subtree is unbalanced after insertion:
     *          LEFT's left branch got too deep.
     *          LEFT's right branch got too deep.
     *      RIGHT subtree is unbalanced after insertion:
     *          RIGHT's left branch got too deep.
     *          RIGHT's right branch got too deep.
     *      OTHERWISE the node is considered balanced, simply return the original.
     */
    private Node<T> balance(Node<T> node) {

        int balance = node.balance;

        // LEFT CASE - left subtree is two levels higher than right subtree
        if (balance == -2) {

            // if two subtrees are balanced, prefer leftleft case
            if (node.left.balance <= 0)
                return leftLeft(node);
            else
                return leftRight(node);

        // RIGHT CASE - right node is two levels higher
        } else  if (balance == 2) {

            // prefer rightright case
            if (node.right.balance >= 0)
                return rightRight(node);
            else
                return rightLeft(node);
        }

        // balancing not needed (balance is -1, 0 or 1)
        return node;
    }

    // Now we have 4 different cases to implement, but basically we only have to do two rotations - we either rotate left or rotate right.
    // On two occasions (leftleft case - the leftmost branch is highest || rightright case - the rightmost branch is highest)
    //     we will do just one operation (rotation)
    // The other two occasions
    //     we'll have to two operations - either rotate left sub node or right sub node.
    //         After that we end up in either leftleft or rightright case. Then Rotate the node itself.

// ===========================  FOUR CASES  ========================================================================= //

    private Node<T> leftLeft(Node<T> node) {
        return rotateRight(node);
    }

    private Node<T> leftRight(Node<T> node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    private Node<T> rightRight(Node<T> node) {
        return rotateLeft(node);
    }

    private Node<T> rightLeft(Node<T> node) {
        node.right = (rotateRight(node.right));
        return rotateLeft(node);
    }

// ===========================  TWO ROTATIONS  ====================================================================== //

    /**
     *  3 whole blocks from original node that will be moved around:
     *      * rightright branch (original root's right node + it's right subtree)
     *      * rightleft branch  (the left subtree of original root's right node)
     *      * LEFT branch       (the original root itself with the whole left subtree)
     */
    private Node<T> rotateLeft(Node<T> node) {
        // The rightmost (rightright) branch becomes new root
        Node<T> newRoot = node.right;
        // rightleft will be cut and attached as previous root's right branch.
        node.right = newRoot.left;
        // finally the old (and already modified) node becomes the new root's left branch.
        newRoot.left = node;
        // 3 whole blocks were moved, but only two blocks changed the heights
        //   (rightleft had a parent before and has right now, it's height was not changed)
        updateHeightAndBalance(node);
        updateHeightAndBalance(newRoot);
        return newRoot;
    }

    /**
     *  3 whole blocks from original node:
     *      * LEFT       - the left node of the original root PLUS it's left subtree
     *      * leftright  - the right subtree of left node
     *      * RIGHT      - all the rest: original root node PLUS it's right subtree
     */
    private Node<T> rotateRight(Node<T> node) {
        // LEFT becomes root
        Node<T> newRoot = node.left;
        // take the leftright block  (leftright == newRoot.getRight())
        //    and attach it as original root's right side
        node.left = newRoot.right;
        // now we'll merge the two trees
        newRoot.right = node;
        // again, only two blocks had their heights changed
        updateHeightAndBalance(node);
        updateHeightAndBalance(newRoot);
        return newRoot;
    }

// ================================================================================================================== //
//                                                                                                                    //
//                                                  REMOVAL                                                           //
//                                                                                                                    //
// ================================================================================================================== //

    public void removeValue(T value) {
        if (value != null) {
            root = remove(root, value);
        }
    }

    private Node<T> remove(Node<T> root, T value) {
        if (root == null)
            return null;

        int compare = value.compareTo(root.value);
        // Check recursively for the left and right subtrees until we find the element to remove (compare == 0)
        if (compare < 0) {
            root.left = remove(root.left, value);
        }
        else if (compare > 0) {
            root.right = remove(root.right, value);
        }
        else {
            // We have found our node.
            // At this point we have no knowledge of parents, this is the one and only root.
            // There are three possible options:
            //      1) only have right subtree
            //      2) only have left subtree
            //      3) have both left and right subtrees
            //      4) ( Okay, root can be a leaf, but this is already covered by 1) and 2) )

            // 1)
            if (root.left == null)
                return root.right;
            // 2)
            else if (root.right == null)
                return root.left;
            // 3)
            else {
                // We have both left and right subtrees
                // Requirements (Uni HW assignments) dictate to replace the current node with the highest value
                // from the left subtree.

                // Find the biggest value from left subtree
                T replacement = findMaxValue(root.left);
                // Change the value of current node
                root.value = replacement;
                // Remove the value we've used up, store the remainder to newLeft and
                // attach it as left subtree.
                root.left = remove(root.left, replacement);
            }
        }

        // finally update data fields
        updateHeightAndBalance(root);
        // and balance the tree at each step when unwinding from recursive calls
        return balance(root);
    }

    private T findMaxValue(Node<T> node) {
        while (node.right != null)
            node = node.right;
        return node.value;
    }

    @Override
    public String toString() {
        return (root != null) ? root.toString() : "";
    }
}
