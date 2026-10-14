public class BSTBuilder {

    private BSTBuilder() {
        // Private constructor to prevent instantiation of utility class
    }

    public static BinarySearchTree<Integer>
    buildUnbalancedBST(BinarySearchTree<Integer> bst) {
        bst.put(2);
        bst.put(4);
        bst.put(5);
        bst.put(7);
        bst.put(8);
        bst.put(9);
        bst.put(12);
        bst.put(14);
        bst.put(17);
        bst.put(19);
        bst.put(22);
        bst.put(25);
        bst.put(27);
        bst.put(28);
        bst.put(33);
        bst.put(37);
        return bst;
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> bst1 = new BinarySearchTree<>();
        buildUnbalancedBST(bst1);
        SwingBinaryTreeDisplay.showInWindow(bst1);
    }
}
