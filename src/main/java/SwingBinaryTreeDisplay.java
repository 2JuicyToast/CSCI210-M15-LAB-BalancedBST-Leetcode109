import com.zybooks.dsaj.treebook.LinkedBinaryTree;
import com.zybooks.dsaj.treebook.Position;
import edu.desu.utilities.UtilityMethodsBinaryTreeBook;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SwingBinaryTreeDisplay {

    static class BinaryTreePanel<E> extends JPanel {
        private final LinkedBinaryTree<E> tree;
        private final int nodeRadius = 20;
        private final int vGap = 50;
        private final int minNodeDistance = 50;
        private final int marginLeft = 40;
        private final int marginTop = 40;

        public BinaryTreePanel(LinkedBinaryTree<E> tree) {
            this.tree = tree;
            setBackground(Color.WHITE);
        }

        private int getTreeHeight(Position<E> p) {
            if (p == null) return 0;
            int leftH = getTreeHeight(tree.left(p));
            int rightH = getTreeHeight(tree.right(p));
            return 1 + Math.max(leftH, rightH);
        }

        private void computeInOrderIndices(Position<E> p, Map<Position<E>, Integer> map, int[] counter) {
            if (p == null) return;
            computeInOrderIndices(tree.left(p), map, counter);
            map.put(p, counter[0]++);
            computeInOrderIndices(tree.right(p), map, counter);
        }

        @Override
        public Dimension getPreferredSize() {
            if (tree.root() == null) {
                return new Dimension(800, 600);
            }
            int h = getTreeHeight(tree.root());
            Map<Position<E>, Integer> xMap = new HashMap<>();
            computeInOrderIndices(tree.root(), xMap, new int[]{0});
            int nodeCount = xMap.size();

            int preferredWidth = Math.max(800, marginLeft + nodeCount * minNodeDistance + marginLeft);
            int preferredHeight = Math.max(600, marginTop + Math.max(1, h) * vGap + marginTop);

            return new Dimension(preferredWidth, preferredHeight);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tree.root() != null) {
                Map<Position<E>, Integer> xMap = new HashMap<>();
                computeInOrderIndices(tree.root(), xMap, new int[]{0});
                drawTree(g, tree.root(), xMap, 0);
            }
        }

        private void drawTree(Graphics g, Position<E> p, Map<Position<E>, Integer> xMap, int level) {
            if (p == null) {
                return;
            }

            int index = xMap.get(p);
            int x = marginLeft + index * minNodeDistance + nodeRadius;
            int y = marginTop + level * vGap;

            g.setColor(Color.BLACK);
            g.drawOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
            String text = p.getElement() != null ? p.getElement().toString() : "";
            
            // Handle multi-line strings for display (first line only for node label)
            String firstLine = text.split("\n")[0];
            FontMetrics fm = g.getFontMetrics();
            g.drawString(firstLine, x - fm.stringWidth(firstLine) / 2, y + fm.getAscent() / 2 - 2);

            Position<E> leftP = tree.left(p);
            if (leftP != null) {
                int leftIndex = xMap.get(leftP);
                int childX = marginLeft + leftIndex * minNodeDistance + nodeRadius;
                int childY = y + vGap;
                g.drawLine(x, y + nodeRadius, childX, childY - nodeRadius);
                drawTree(g, leftP, xMap, level + 1);
            }

            Position<E> rightP = tree.right(p);
            if (rightP != null) {
                int rightIndex = xMap.get(rightP);
                int childX = marginLeft + rightIndex * minNodeDistance + nodeRadius;
                int childY = y + vGap;
                g.drawLine(x, y + nodeRadius, childX, childY - nodeRadius);
                drawTree(g, rightP, xMap, level + 1);
            }
        }
    }

    public static <E> void showInWindow(LinkedBinaryTree<E> T) {
        JFrame frame = new JFrame("LinkedBinaryTree Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        BinaryTreePanel<E> panel = new BinaryTreePanel<>(T);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton rebalanceButton = new JButton("Rebalance Tree");
        rebalanceButton.addActionListener(e -> {
            if (T != null) {
                if (T instanceof BinarySearchTree) {
                    ((BinarySearchTree<?>) T).rebalanceTreeL109();
                } else {
                    BinarySearchTree<E> bst = new BinarySearchTree<>();
                    for (E element : UtilityMethodsBinaryTreeBook.generateArrayListFromBinaryTree(T)) {
                        bst.add(element);
                    }
                    bst.rebalanceTreeL109();
                    T.root = bst.root;
                }
                panel.revalidate();
                panel.repaint();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(rebalanceButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
