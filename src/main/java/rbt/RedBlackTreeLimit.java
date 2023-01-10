package rbt;

import java.util.Comparator;

/**
 * Created by thooaug on 10/1/2023.
 */
public class RedBlackTreeLimit<V> extends RedBlackTree<V> {

    private final int limit;
    private transient Node<V> last;

    public RedBlackTreeLimit(Comparator<V> comparator, int limit) {
        super(comparator);
        this.limit = limit;
        if (limit <= 0) throw new IllegalArgumentException("Limit must be 1 or greater");
    }

    public int limit() {
        return limit;
    }

    @Override
    public void clear() {
        super.clear();
        last = null;
    }

    @Override
    public boolean insert(V value) {
        if (last != null) {
            int cmp = comparator().compare(last.value, value);
            if (cmp < 0) {
                return false;
            }
        }
        boolean added = super.insert(value);
        if (added) {
            while (size() > limit) {
                removeLastNode();
            }
            if (size() == limit) {
                last = lastNode();
            }
        }
        return added;
    }
}
