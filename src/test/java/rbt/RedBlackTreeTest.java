package rbt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by Augustus Thoo on 9/1/2023
 */
class RedBlackTreeTest {

    @Test
    void insert() {
        Comparator<Integer> comparator = Comparator.reverseOrder();
        RedBlackTree<Integer> tree = new RedBlackTree<>(comparator);

        List<Integer> data = new ArrayList<>();
        final int N = 20;
        for (int i = 0; i < N; i++) data.add(i);
        Collections.shuffle(data);
        System.out.println("shuffled data = " + data);
        data.forEach(tree::insert);
        Assertions.assertEquals(N, tree.size());
        System.out.println("  sorted data = " + tree.values());

        int expected = N - 1;
        for (Integer value : tree.values()) {
            Assertions.assertEquals(expected, value);
            expected--;
        }

        // insert existing number
        tree.insert(10);

        // assert that nothing has changed
        Assertions.assertEquals(N, tree.size());
        expected = N - 1;
        for (Integer value : tree.values()) {
            Assertions.assertEquals(expected, value);
            expected--;
        }

        printTree(tree);
        //printTree(tree.getRoot(), 0);
    }

    private void printTree(RedBlackTree.Node<Integer> node, int indent) {
        if (node != null) {
            for (int i = 0; i < indent; i++) System.out.print("  ");
            System.out.print(" -> ");
            System.out.println(toString(node));
            if (node.left != null) printTree(node.left, indent+1);
            if (node.right != null) printTree(node.right, indent+1);
        }
    }

    private void printTree(RedBlackTree<Integer> tree) {
        RedBlackTree.Node<Integer> root = tree.getRoot();
        System.out.println("root : "+toString(root));
        System.out.println("root.count : "+count(root));
        System.out.println("left.count : "+count(root.left));
        System.out.println("right.count : "+count(root.right));
        //System.out.println("root.left : "+toStringDeep(root.left));
        //System.out.println("root.right : "+toStringDeep(root.right));
        //System.out.println("root.parent : "+toStringDeep(root.parent));

        List<RedBlackTree.Node<Integer>> nodes = getNodes(tree);
        for (int i = 0; i < nodes.size(); i++) {
            RedBlackTree.Node<Integer> node = nodes.get(i);
            System.out.printf("%2d | %s%n", i, toString(node));
        }
    }

    public List<RedBlackTree.Node<Integer>> getNodes(RedBlackTree<Integer> tree) {
        RedBlackTree.Node<Integer> root = tree.getRoot();
        if (root == null) return Collections.emptyList();
        List<RedBlackTree.Node<Integer>> list = new ArrayList<>(tree.size());
        RedBlackTree.traverse(root, list::add);
        return list;
    }

    public int count(RedBlackTree.Node<Integer> node) {
        if (node == null) return 0;
        LongAdder adder = new LongAdder();
        RedBlackTree.traverse(node, o -> adder.increment());
        return adder.intValue();
    }

    public String toString(RedBlackTree.Node<Integer> node) {
        if (node == null) return "null";
        return "{" +
                "val=" + node.value +
                ", clr=" + (node.color == RedBlackTree.RED ? "RED" : "BLACK") +
                ", pos=" + node.pos +
                '}';
    }

    public String toStringDeep(RedBlackTree.Node<Integer> node) {
        if (node == null) return "null";
        return "{" +
                "val=" + node.value +
                ", clr=" + (node.color == RedBlackTree.RED ? "RED" : "BLACK") +
                ", pos=" + node.pos +
                ", l=" + toStringDeep(node.left) +
                ", r=" + toStringDeep(node.right) +
                '}';
    }
}