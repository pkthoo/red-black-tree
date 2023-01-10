package rbt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by thooaug on 10/1/2023.
 */
class RedBlackTreeLimitTest {

    @Test
    void insert() {
        Comparator<Integer> comparator = Comparator.reverseOrder();
        int limit = 6;
        RedBlackTreeLimit<Integer> tree = new RedBlackTreeLimit<>(comparator, limit);

        List<Integer> data = new ArrayList<>();
        final int N = 20;
        for (int i = 0; i < N; i++) data.add(i);
        Collections.shuffle(data);
        System.out.println("shuffled data = " + data);
        data.forEach(tree::insert);
        System.out.println("  sorted data = " + tree.values());
        Assertions.assertEquals(limit, tree.size());

        int expected = N - 1;
        for (Integer value : tree.values()) {
            Assertions.assertEquals(expected, value);
            expected--;
        }
    }
}