package com.engine.sorting_algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BubbleSortManagerTest {

    BubbleSortManager bubbleSortManager = new BubbleSortManager();

    ArrayList<ArrayList<Integer>> tempList = new ArrayList<>();


    @BeforeEach
    void initialise() {
        tempList.add(new ArrayList<>());
    }

    /**
     * Check if:
     * sorts array of 9 elements
     */
    @Test
    void bubbleSortArrayTest1() {
        tempList.set(0, new ArrayList<Integer>(Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1)));
        bubbleSortManager.setList(tempList);
        while (!bubbleSortManager.sortOneStep());
        ArrayList<Integer> sortedList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (int i = 0; i < 9; i++) {
            assertEquals(bubbleSortManager.getList().get(0).get(i), sortedList.get(i));
        }

    }

    /**
     * Check if:
     * returns a sorted array if an input contains an already sorted array
     */
    @Test
    void bubbleSortArrayTest2() {
        tempList.set(0, new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        bubbleSortManager.setList(tempList);
        while (!bubbleSortManager.sortOneStep());
        ArrayList<Integer> sortedList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (int i = 0; i < 9; i++) {
            assertEquals(bubbleSortManager.getList().get(0).get(i), sortedList.get(i));
        }

    }

    /**
     * Check if:
     * list of 1 element still contains that element
     */
    @Test
    void bubbleSortArrayTest3() {
        tempList.set(0, new ArrayList<Integer>(Arrays.asList(3)));
        bubbleSortManager.setList(tempList);
        while (!bubbleSortManager.sortOneStep());
        ArrayList<Integer> sortedList = new ArrayList<Integer>(Arrays.asList(3));
        for (int i = 0; i < 1; i++) {
            assertEquals(bubbleSortManager.getList().get(0).get(i), sortedList.get(i));
        }

    }

    /**
     * Check if:
     * list is sorted if contains no elements
     */
    @Test
    void bubbleSortArrayTest4() {
        tempList.set(0, new ArrayList<Integer>(Arrays.asList()));
        bubbleSortManager.setList(tempList);
        assertTrue(bubbleSortManager.sortOneStep());


    }
}
