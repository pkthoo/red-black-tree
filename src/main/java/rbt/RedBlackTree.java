package rbt;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Augustus Thoo on 9/1/2023
 */
public class RedBlackTree<V> {

    static final boolean RED = false;
    static final boolean BLACK = true;

    private final Comparator<V> comparator;
    private transient Node<V> root;
    private transient int size;

    public RedBlackTree(Comparator<V> comparator) {
        this.comparator = comparator;
    }

    public int size() {
        return size;
    }

    public void insert(V value) {
        if (root == null) {
            root = new Node<>(value);
            size = 1;
            return;
        }

        Node<V> node = root;
        Node<V> parent;
        int cmp;

        // Traverse the tree to the left or right depending on the key
        do {
            parent = node;
            cmp = comparator.compare(value, node.value);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                node.value = value;
                return;
            }
        } while (node != null);

        // Insert new node
        node = new Node<>(parent, value);
        if (cmp < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        fixAfterInsertion(node);
        size++;
    }

    public V remove(V value) {
        Node<V> p = getEntry(value);
        if (p == null)
            return null;

        V oldValue = p.value;
        deleteEntry(p);
        return oldValue;
    }

    public void clear() {
        size = 0;
        root = null;
    }

    public Collection<V> values() {
        return new Values();
    }

    Node<V> getRoot() {
        return root;
    }

    private Node<V> getEntry(V value) {
        Node<V> p = root;
        while (p != null) {
            int cmp = comparator.compare(value, p.value);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }

    private void fixAfterInsertion(Node<V> x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node<V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node<V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    private void fixAfterDeletion(Node<V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node<V> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib))  == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                Node<V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                        colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }

    private static <V> boolean colorOf(Node<V> p) {
        return (p == null ? BLACK : p.color);
    }

    private static <V> Node<V> parentOf(Node<V> p) {
        return (p == null ? null: p.parent);
    }

    private static <V> void setColor(Node<V> p, boolean c) {
        if (p != null)
            p.color = c;
    }

    private static <V> Node<V> leftOf(Node<V> p) {
        return (p == null) ? null: p.left;
    }

    private static <V> Node<V> rightOf(Node<V> p) {
        return (p == null) ? null: p.right;
    }

    private void rotateLeft(Node<V> p) {
        if (p != null) {
            Node<V> r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    private void rotateRight(Node<V> p) {
        if (p != null) {
            Node<V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    private void deleteEntry(Node<V> p) {
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            Node<V> s = successor(p);
            p.value = s.value;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        Node<V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.color == BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    private static <V> Node<V> successor(Node<V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Node<V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Node<V> p = t.parent;
            Node<V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private static <V> Node<V> predecessor(Node<V> t) {
        if (t == null)
            return null;
        else if (t.left != null) {
            Node<V> p = t.left;
            while (p.right != null)
                p = p.right;
            return p;
        } else {
            Node<V> p = t.parent;
            Node<V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private Node<V> getFirstEntry() {
        Node<V> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    private Node<V> getLastEntry() {
        Node<V> p = root;
        if (p != null)
            while (p.right != null)
                p = p.right;
        return p;
    }

    static <V> void traverse(Node<V> t, Consumer<Node<V>> c) {
        if (t.left != null) traverse(t.left, c);
        c.accept(t);
        if (t.right != null) traverse(t.right, c);
    }

    private class Values extends AbstractCollection<V> {

        public Iterator<V> iterator() {
            return new ValueIterator(getFirstEntry());
        }

        public int size() {
            return RedBlackTree.this.size();
        }
    }

    private final class ValueIterator implements Iterator<V> {

        Node<V> next;

        public ValueIterator(Node<V> first) {
            this.next = first;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public V next() {
            Node<V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            next = successor(e);
            return e.value;
        }
    }

    /**
     * Created by Augustus Thoo on 9/1/2023
     */
    static class Node<V> {
        Node<V> left;
        Node<V> right;
        Node<V> parent;
        V value;
        boolean color;
        int pos;

        Node(V value) {
            this.parent = null;
            this.value = value;
            this.color = BLACK;
        }

        Node(Node<V> parent, V value) {
            this.parent = parent;
            this.value = value;
            this.color = RED;
        }
    }
}
