package com.engine.sorting_algorithms;

import javafx.util.Pair;

import java.util.ArrayList;


/**
 * @version 1.0
 *
 * Class that implements functionality of Bubble Sort algorithm
 *
 */
public class BubbleSortManager extends SortingAlgorithmsManager {

    /**************************************************************************

                                 Global variables

     *************************************************************************/

    private boolean alreadySorted;

    private int currentIndex, fullLoopCycleCount;

    private AlgorithmState state;

    /**************************************************************************

                                     Constructors

     *************************************************************************/


    /**
     * Empty constructor
     */
    public BubbleSortManager() {}

    /**************************************************************************

                                  Override methods

     *************************************************************************/


    @Override
    public boolean sortOneStep() {
        if (!alreadySorted) {
            switch (state) {
                case COMPARISON -> {
                    ArrayList<Pair<Integer, Integer>> tempList = new ArrayList<>();
                    tempList.add(new Pair<>(0, currentIndex));
                    tempList.add(new Pair<>(0, currentIndex + 1));
                    setCurrentPointers(tempList);

                    if (currentIndex + 1 < list.get(0).size() - fullLoopCycleCount) {
                        if (list.get(0).get(currentIndex) > list.get(0).get(currentIndex + 1)) {
                            state = AlgorithmState.SWAP;
                        } else {
                            currentIndex++;
                        }
                    } else {
                        fullLoopCycleCount++;
                        if (fullLoopCycleCount + 1 == list.get(0).size()) {
                            alreadySorted = true;
                            return true;
                        } else {
                            currentIndex = 0;
                        }
                    }
                }
                    case SWAP -> {
                    int currentValue = list.get(0).get(currentIndex);
                    list.get(0).set(currentIndex, list.get(0).get(currentIndex + 1));
                    list.get(0).set(currentIndex + 1, currentValue);
                    ArrayList<Pair<Integer, Integer>> tempList = new ArrayList<>();
                    tempList.add(new Pair<>(0, currentIndex));
                    tempList.add(new Pair<>(0, currentIndex + 1));
                    setCurrentPointers(tempList);
                    currentIndex++;
                    state = AlgorithmState.COMPARISON;
                }
            }
            return false;
        }
        return true;
    }


    @Override
    public void initialise() {
        pointers = new ArrayList<>();
        alreadySorted = false;
        currentIndex = 0;
        fullLoopCycleCount = 0;
        state = AlgorithmState.COMPARISON;

    }


    @Override
    public String toString() {
        return "BubbleSort";
    }

    /**************************************************************************

                                Public  methods

     *************************************************************************/

    /**
     * Function that sorts {@link #list} in one call.
     * Might be useful for testing.
     */
    public void fastSort() {
        int listLength = list.get(0).size();
        Integer currentValue = 0;

        for (int i = 0; i < listLength; i++) {
            currentValue = list.get(0).get(i);
            for (int j = i + 1; j < listLength; j++) {
                if (currentValue > list.get(0).get(j)) {
                    list.get(0).set(i, list.get(0).get(j));
                    list.get(0).set(j, currentValue);
                }
            }
        }
    }
}

