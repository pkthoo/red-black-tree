package rbt;

/**
 * Created by Augustus Thoo on 9/1/2023
 */
class Node<V> {
    Node<V> left;
    Node<V> right;
    Node<V> parent;
    V value;
    boolean color;
    int pos;

    Node(V value) {
        this.parent = null;
        this.value = value;
        this.color = RedBlackTree.BLACK;
    }

    Node(Node<V> parent, V value) {
        this.parent = parent;
        this.value = value;
        this.color = RedBlackTree.RED;
    }
}
