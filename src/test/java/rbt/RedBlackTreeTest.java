package rbt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    }
}