package com.engine.sorting_algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuickSortManagerTest {
    QuickSortManager quickSortManager = new QuickSortManager();

    ArrayList<ArrayList<Integer>> tempList = new ArrayList<>();


    @BeforeEach
    void initialise(){
        tempList.add(new ArrayList<>());
    }

    /**
     * Check if:
     * sorts an array of 9 elements
     */
    @Test
    void quickSortArrayTest1(){
        tempList.set(0, new ArrayList<Integer>(Arrays.asList(9,8,7,6,5,4,3,2,1)));
        quickSortManager.setList(tempList);
        while(!quickSortManager.sortOneStep()){}
        ArrayList<Integer> sortedList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        for (int i = 0; i< 9; i++) {
            assertEquals(quickSortManager.getList().get(0).get(i),sortedList.get(i));
        }

    }

    /**
     * Check if:
     * returns a sorted array if an input contains an already sorted array
     */
    @Test
    void quickSortArrayTest2(){
        tempList.set(0, new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9)));
        quickSortManager.setList(tempList);
        while(!quickSortManager.sortOneStep());
        ArrayList<Integer> sortedList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        for (int i = 0; i< 9; i++) {
            assertEquals(quickSortManager.getList().get(0).get(i),sortedList.get(i));
        }

    }

    /**
     * Check if:
     * list of 1 element still contains that element
     */
    @Test
    void quickSortArrayTest3() {
        tempList.set(0, new ArrayList<Integer>(Arrays.asList(3)));
        quickSortManager.setList(tempList);
        while (!quickSortManager.sortOneStep());
        ArrayList<Integer> sortedList = new ArrayList<Integer>(Arrays.asList(3));
        for (int i = 0; i < 1; i++) {
            assertEquals(quickSortManager.getList().get(0).get(i), sortedList.get(i));
        }

    }


    /**
     * Check if:
     * list is sorted if contains no elements
     */
    @Test
    void quickSortArrayTest4() {
        tempList.set(0, new ArrayList<Integer>(Arrays.asList()));
        quickSortManager.setList(tempList);
        assertEquals(quickSortManager.sortOneStep(), true);


    }
}
