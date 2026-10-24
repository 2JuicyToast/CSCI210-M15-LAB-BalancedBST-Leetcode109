import com.zybooks.dsaj.treebook.LinkedBinaryTree;
import com.zybooks.dsaj.treebook.Position;
import edu.desu.utilities.UtilityMethodsBinaryTreeBook;
import java.util.ArrayList;

public class BinarySearchTree<E> extends LinkedBinaryTree<E> {

    /**
     * Default constructor that initializes a BinarySearchTree as a LinkedBinaryTree with no nodes.
     */
    public BinarySearchTree() {
        super();
    }

    /**
     * Supporting method
     * Compares two elements.
     *
     * @param a first element
     * @param b second element
     * @return a negative integer, zero, or a positive integer as a is less than, equal to, or greater than b
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected int compare(Object a, Object b) {
        if (a == b) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        if (a instanceof Comparable && b instanceof Comparable) {
            try {
                return ((Comparable) a).compareTo(b);
            } catch (ClassCastException e) {
                // Ignore and fall through to reflection / toString comparison
            }
        }
        try {
            java.lang.reflect.Method m = a.getClass().getMethod("compareTo", b.getClass());
            return (Integer) m.invoke(a, b);
        } catch (Exception e) {
            return a.toString().compareTo(b.toString());
        }
    }

    /**
     * Searches for a target object in the binary get tree.
     * Finds the associated node (position) if it exists, or the insertion point node if it does not.
     *
     * @param target the target object to get for
     * @return the Position containing the target element if found, or the Position of the last node visited, or null if empty
     */
    public Position<E> get(E target) {
        return get(root(), target);
    }

    /**
     * Recursively searches the subtree rooted at position p for the target object.
     *
     * @param p the root of the subtree to get
     * @param target the target object to get for
     * @return the Position containing target if found, or the last visited node Position
     */
    public Position<E> get(Position<E> p, E target) {
        if (p == null || target == null) {
            return p;
        }
        int comp = compare(target, p.getElement());
        if (comp == 0) {
            return p;
        } else if (comp < 0) {
            Position<E> leftChild = left(p);
            if (leftChild == null) {
                return p;
            }
            return get(leftChild, target);
        } else {
            Position<E> rightChild = right(p);
            if (rightChild == null) {
                return p;
            }
            return get(rightChild, target);
        }
    }

    /**
     * Adds a node to the tree.
     * If the tree does not have a root node, adds the root.
     * If the tree has a root, adds the new node using standard binary tree get methodology.
     *
     * @param e the element to add
     * @return the Position of the added or updated node
     */
    public Position<E> put(E e) {
        if (isEmpty()) {
            return addRoot(e);
        }
        Position<E> p = get(e);
        int comp = compare(e, p.getElement());
        if (comp < 0) {
            return addLeft(p, e);
        } else if (comp > 0) {
            return addRight(p, e);
        } else { // Default condition when comp == 0 - implied
            set(p, e);
            return p;
        }
    }

    /**
     * Removes the node storing element e from the binary search tree, if present.
     *
     * @param e the element to delete
     * @return the element that was removed, or null if not found
     */
    public E delete(E e) {
        if (isEmpty() || e == null) {
            return null;
        }
        Position<E> p = get(e);
        if (p == null || compare(e, p.getElement()) != 0) {
            return null;
        }
        return delete(p);
    }

    /**
     * Removes the node at Position p from the binary search tree while preserving search tree properties.
     *
     * @param p the Position to remove
     * @return the element that was stored at Position p prior to deletion
     * @throws IllegalArgumentException if p is not a valid position
     */
    public E delete(Position<E> p) throws IllegalArgumentException {
        if (p == null) {
            return null;
        }
        if (numChildren(p) == 2) {
            Position<E> successor = right(p);
            while (left(successor) != null) {
                successor = left(successor);
            }
            E deletedElement = p.getElement();
            set(p, successor.getElement());
            remove(successor);
            return deletedElement;
        } else {
            return remove(p);
        }
    }

    /**
     * Adds an element to the tree by delegating to put.
     *
     * @param e element to add
     * @return Position of added element
     */
    public Position<E> add(E e) {
        return put(e);
    }

    /**
     * Rebalances the current binary search tree object using LeetCode 109 logic.
     */
    public void rebalanceTreeL109() {
        if (isEmpty() || root == null) {
            return;
        }
        ArrayList<E> elements = UtilityMethodsBinaryTreeBook.generateArrayListFromBinaryTree(this);
        this.root = buildBalancedBST(elements, 0, elements.size() - 1, null);
    }

    private Node<E> buildBalancedBST(ArrayList<E> elements, int start, int end, Node<E> parent) {
        if (start > end) {
            return null;
        }
        int mid = start + (end - start) / 2;
        Node<E> node = createNode(elements.get(mid), parent, null, null);
        node.setLeft(buildBalancedBST(elements, start, mid - 1, node));
        node.setRight(buildBalancedBST(elements, mid + 1, end, node));
        return node;
    }

}
