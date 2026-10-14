import com.zybooks.dsaj.treebook.Position;
import edu.desu.utilities.UtilityMethodsBinaryTreeBook;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class treesappTest {

    private <E> int getNodeHeight(BinarySearchTree<E> tree, Position<E> p) {
        if (p == null) {
            return -1;
        }
        int leftH = getNodeHeight(tree, tree.left(p));
        int rightH = getNodeHeight(tree, tree.right(p));
        return 1 + Math.max(leftH, rightH);
    }

    private <E> boolean isHeightBalanced(BinarySearchTree<E> tree, Position<E> p) {
        if (p == null) {
            return true;
        }
        int leftH = getNodeHeight(tree, tree.left(p));
        int rightH = getNodeHeight(tree, tree.right(p));
        if (Math.abs(leftH - rightH) > 1) {
            return false;
        }
        return isHeightBalanced(tree, tree.left(p)) && isHeightBalanced(tree, tree.right(p));
    }

    private <E extends Comparable<E>> boolean isBSTValid(BinarySearchTree<E> tree, Position<E> p, E min, E max) {
        if (p == null) {
            return true;
        }
        E val = p.getElement();
        if (min != null && val.compareTo(min) <= 0) {
            return false;
        }
        if (max != null && val.compareTo(max) >= 0) {
            return false;
        }
        return isBSTValid(tree, tree.left(p), min, val) && isBSTValid(tree, tree.right(p), val, max);
    }

    @Test
    void testRebalanceTreeL109_emptyTree() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.isEmpty(), "Tree should initially be empty");
        assertNull(bst.root(), "Root should initially be null");

        bst.rebalanceTreeL109();

        assertTrue(bst.isEmpty(), "Tree should remain empty after rebalancing");
        assertNull(bst.root(), "Root should remain null after rebalancing empty tree");
        assertEquals(0, bst.size(), "Size should be 0");
    }

    @Test
    void testRebalanceTreeL109_singleElement() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.put(42);

        bst.rebalanceTreeL109();

        assertNotNull(bst.root(), "Root should not be null");
        assertEquals(42, bst.root().getElement());
        assertNull(bst.left(bst.root()), "Left child of single node should be null");
        assertNull(bst.right(bst.root()), "Right child of single node should be null");
        assertEquals(0, getNodeHeight(bst, bst.root()), "Height of single node tree should be 0");
        assertTrue(isHeightBalanced(bst, bst.root()), "Single node tree is balanced");
    }

    @Test
    void testRebalanceTreeL109_twoElements() {
        // Right-leaning two-element tree
        BinarySearchTree<Integer> bst1 = new BinarySearchTree<>();
        bst1.put(10);
        bst1.put(20);
        bst1.rebalanceTreeL109();

        assertEquals(10, bst1.root().getElement(), "Root should be the median/first element");
        assertNull(bst1.left(bst1.root()));
        assertNotNull(bst1.right(bst1.root()));
        assertEquals(20, bst1.right(bst1.root()).getElement());
        assertTrue(isHeightBalanced(bst1, bst1.root()));

        // Left-leaning two-element tree
        BinarySearchTree<Integer> bst2 = new BinarySearchTree<>();
        bst2.put(20);
        bst2.put(10);
        bst2.rebalanceTreeL109();

        assertEquals(10, bst2.root().getElement(), "Rebalanced tree should choose index 0 as root");
        assertNull(bst2.left(bst2.root()));
        assertNotNull(bst2.right(bst2.root()));
        assertEquals(20, bst2.right(bst2.root()).getElement());
        assertTrue(isHeightBalanced(bst2, bst2.root()));
    }

    @Test
    void testRebalanceTreeL109_threeElementsSkewedToBalanced() {
        // Right-skewed chain: 10 -> 20 -> 30 (height 2)
        BinarySearchTree<Integer> rightSkewed = new BinarySearchTree<>();
        rightSkewed.put(10);
        rightSkewed.put(20);
        rightSkewed.put(30);
        assertEquals(2, getNodeHeight(rightSkewed, rightSkewed.root()));

        rightSkewed.rebalanceTreeL109();

        assertEquals(1, getNodeHeight(rightSkewed, rightSkewed.root()), "Height should be reduced from 2 to 1");
        assertEquals(20, rightSkewed.root().getElement(), "Root should be median element 20");
        assertEquals(10, rightSkewed.left(rightSkewed.root()).getElement(), "Left child should be 10");
        assertEquals(30, rightSkewed.right(rightSkewed.root()).getElement(), "Right child should be 30");
        assertTrue(isHeightBalanced(rightSkewed, rightSkewed.root()));

        // Left-skewed chain: 30 -> 20 -> 10 (height 2)
        BinarySearchTree<Integer> leftSkewed = new BinarySearchTree<>();
        leftSkewed.put(30);
        leftSkewed.put(20);
        leftSkewed.put(10);
        assertEquals(2, getNodeHeight(leftSkewed, leftSkewed.root()));

        leftSkewed.rebalanceTreeL109();

        assertEquals(1, getNodeHeight(leftSkewed, leftSkewed.root()), "Height should be reduced from 2 to 1");
        assertEquals(20, leftSkewed.root().getElement(), "Root should be median element 20");
        assertEquals(10, leftSkewed.left(leftSkewed.root()).getElement(), "Left child should be 10");
        assertEquals(30, leftSkewed.right(leftSkewed.root()).getElement(), "Right child should be 30");
        assertTrue(isHeightBalanced(leftSkewed, leftSkewed.root()));
    }

    @Test
    void testRebalanceTreeL109_oddNumberOfElementsFullTree() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 1; i <= 7; i++) {
            bst.put(i);
        }
        assertEquals(6, getNodeHeight(bst, bst.root()), "Initial right-skewed tree should have height 6");

        bst.rebalanceTreeL109();

        // 7 elements rebalance into a perfect binary tree of height 2
        assertEquals(2, getNodeHeight(bst, bst.root()), "Rebalanced 7-node tree should have height 2");
        assertEquals(4, bst.root().getElement(), "Root should be 4");
        assertEquals(2, bst.left(bst.root()).getElement());
        assertEquals(6, bst.right(bst.root()).getElement());
        assertEquals(1, bst.left(bst.left(bst.root())).getElement());
        assertEquals(3, bst.right(bst.left(bst.root())).getElement());
        assertEquals(5, bst.left(bst.right(bst.root())).getElement());
        assertEquals(7, bst.right(bst.right(bst.root())).getElement());

        assertTrue(isHeightBalanced(bst, bst.root()), "Tree should be height-balanced");
        assertTrue(isBSTValid(bst, bst.root(), null, null), "Tree must satisfy BST property");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7),
                UtilityMethodsBinaryTreeBook.generateArrayListFromBinaryTree(bst),
                "In-order traversal must preserve sorted order");
    }

    @Test
    void testRebalanceTreeL109_unbalancedBSTFromBSTBuilder() {
        BinarySearchTree<Integer> unbalanced = new BinarySearchTree<>();
        BSTBuilder.buildUnbalancedBST(unbalanced);

        assertEquals(15, getNodeHeight(unbalanced, unbalanced.root()), "Unbalanced tree height should be 15");
        assertEquals(2, unbalanced.root().getElement(), "Initial root is 2");

        unbalanced.rebalanceTreeL109();

        // Check height reduction to 4
        assertEquals(4, getNodeHeight(unbalanced, unbalanced.root()), "Rebalanced tree height should be 4");
        assertEquals(14, unbalanced.root().getElement(), "Rebalanced root should be 14");
        assertTrue(isHeightBalanced(unbalanced, unbalanced.root()), "Rebalanced tree should satisfy AVL balance condition");
        assertTrue(isBSTValid(unbalanced, unbalanced.root(), null, null), "Rebalanced tree must be a valid BST");

        // Verify key nodes of the expected balanced structure
        Position<Integer> root = unbalanced.root();
        assertEquals(14, root.getElement());
        assertEquals(7, unbalanced.left(root).getElement());
        assertEquals(25, unbalanced.right(root).getElement());

        Position<Integer> node7 = unbalanced.left(root);
        assertEquals(4, unbalanced.left(node7).getElement());
        assertEquals(9, unbalanced.right(node7).getElement());

        Position<Integer> node4 = unbalanced.left(node7);
        assertEquals(2, unbalanced.left(node4).getElement());
        assertEquals(5, unbalanced.right(node4).getElement());

        Position<Integer> node9 = unbalanced.right(node7);
        assertEquals(8, unbalanced.left(node9).getElement());
        assertEquals(12, unbalanced.right(node9).getElement());

        Position<Integer> node25 = unbalanced.right(root);
        assertEquals(19, unbalanced.left(node25).getElement());
        assertEquals(28, unbalanced.right(node25).getElement());

        Position<Integer> node19 = unbalanced.left(node25);
        assertEquals(17, unbalanced.left(node19).getElement());
        assertEquals(22, unbalanced.right(node19).getElement());

        Position<Integer> node28 = unbalanced.right(node25);
        assertEquals(27, unbalanced.left(node28).getElement());
        assertEquals(33, unbalanced.right(node28).getElement());

        Position<Integer> node33 = unbalanced.right(node28);
        assertNull(unbalanced.left(node33));
        assertEquals(37, unbalanced.right(node33).getElement());

        // In-order traversal should preserve all 16 sorted elements
        List<Integer> expectedSorted = Arrays.asList(
                2, 4, 5, 7, 8, 9, 12, 14, 17, 19, 22, 25, 27, 28, 33, 37
        );
        assertEquals(expectedSorted, UtilityMethodsBinaryTreeBook.generateArrayListFromBinaryTree(unbalanced));
    }

    @Test
    void testRebalanceTreeL109_alreadyBalancedBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        BSTBuilder.buildUnbalancedBST(bst);
        bst.rebalanceTreeL109(); // now balanced

        List<Integer> firstPreorder = new ArrayList<>();
        for (Position<Integer> p : bst.preorder()) {
            firstPreorder.add(p.getElement());
        }
        int firstHeight = getNodeHeight(bst, bst.root());

        // Rebalance again - should be idempotent
        bst.rebalanceTreeL109();

        List<Integer> secondPreorder = new ArrayList<>();
        for (Position<Integer> p : bst.preorder()) {
            secondPreorder.add(p.getElement());
        }
        int secondHeight = getNodeHeight(bst, bst.root());

        assertEquals(firstHeight, secondHeight);
        assertEquals(firstPreorder, secondPreorder);
        assertTrue(isHeightBalanced(bst, bst.root()));
        assertTrue(isBSTValid(bst, bst.root(), null, null));
    }

    @Test
    void testRebalanceTreeL109_preservesBSTInvariantsAndSearchability() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        List<Integer> values = Arrays.asList(100, 90, 80, 70, 60, 50, 40, 30, 20, 10);
        for (int v : values) {
            bst.put(v);
        }
        assertEquals(9, getNodeHeight(bst, bst.root()), "Initial left-skewed tree should have height 9");

        bst.rebalanceTreeL109();

        assertEquals(3, getNodeHeight(bst, bst.root()), "10 nodes balanced tree should have height 3");
        assertTrue(isHeightBalanced(bst, bst.root()));
        assertTrue(isBSTValid(bst, bst.root(), null, null));

        // All elements are searchable via get()
        for (int v : values) {
            Position<Integer> pos = bst.get(v);
            assertNotNull(pos, "Search for " + v + " should return a position");
            assertEquals(v, pos.getElement());
        }

        // In-order traversal remains sorted
        List<Integer> expectedSorted = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
        assertEquals(expectedSorted, UtilityMethodsBinaryTreeBook.generateArrayListFromBinaryTree(bst));
    }

    @Test
    void testRebalanceTreeL109_preorderTraversalOrder() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        BSTBuilder.buildUnbalancedBST(bst);
        bst.rebalanceTreeL109();

        List<Integer> preorderElements = new ArrayList<>();
        for (Position<Integer> p : bst.preorder()) {
            preorderElements.add(p.getElement());
        }

        List<Integer> expectedPreorder = Arrays.asList(
                14, 7, 4, 2, 5, 9, 8, 12, 25, 19, 17, 22, 28, 27, 33, 37
        );
        assertEquals(expectedPreorder, preorderElements, "Preorder traversal should match LeetCode 109 recursive array order");
    }
}
