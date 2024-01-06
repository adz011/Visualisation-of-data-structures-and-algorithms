package com.engine.sorting_algorithms;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @version 1.0
 * <p>
 * Class that implements functionality of Quick Sort algorithm
 */
public class QuickSortManager extends SortingAlgorithmsManager {

    /**************************************************************************

                                Global variables

     *************************************************************************/
    private boolean partitioning;

    private Stack<Integer> stack;

    private int pivot, start, end, index, pivotIndex, jindex;

    private AlgorithmState state;


    /**************************************************************************

                                     Constructors

     *************************************************************************/

    /**
     * Constructor
     */
    public QuickSortManager() {
    }


    /**************************************************************************

                                  Override methods

     *************************************************************************/


    /**
     * Sorts one step based on the current state of the manager
     *
     * @return true if sorting is completed
     */
    @Override
    public boolean sortOneStep() {
        ArrayList<Pair<Integer, Integer>> tempList = new ArrayList<>();
        if (!partitioning) {
            if (stack.isEmpty()) {
                return true;
            } else {
                end = stack.pop();
                start = stack.pop();
                initialiseVariables();
                partitioning = true;
            }
        } else {
            if (jindex < end) {
                switch (state) {
                    case COMPARISON -> {
                        // show pivot and compared element
                        tempList.add(new Pair<>(0, jindex));
                        tempList.add(new Pair<>(0, end));

                        // check if next step is swap or comparison
                        if (list.get(0).get(jindex) < pivot) {
                            // show swapping
                            state = AlgorithmState.SWAP;

                        } else {
                            jindex++;
                        }


                    }
                    case SWAP -> {
                        tempList.add(new Pair<>(0, index));
                        tempList.add(new Pair<>(0, jindex));

                        int temp = list.get(0).get(index);
                        list.get(0).set(index, list.get(0).get(jindex));
                        list.get(0).set(jindex, temp);

                        index++;
                        jindex++;
                        state = AlgorithmState.COMPARISON;
                    }
                }
                setCurrentPointers(tempList);
                return false;
            } else {

                int temp = list.get(0).get(index);
                list.get(0).set(index, list.get(0).get(end));
                list.get(0).set(end, temp);

                tempList.add(new Pair<>(0, index));
                tempList.add(new Pair<>(0, end));
                setCurrentPointers(tempList);

                pivotIndex = index;

                if (pivotIndex - 1 > start) {
                    stack.push(start);
                    stack.push(pivotIndex - 1);
                }
                if (pivotIndex + 1 < end) {
                    stack.push(pivotIndex + 1);
                    stack.push(end);

                }
                partitioning = false;

            }
        }
        return false;
    }

    @Override
    public void initialise() {
        state = AlgorithmState.COMPARISON;
        stack = new Stack<>();
        partitioning = true;
        start = 0;
        pointers = new ArrayList<>();
        end = list.get(0).size() - 1;
        initialiseVariables();
        
    }

    @Override
    public String toString() {
        return "QuickSort";
    }

    /**************************************************************************

                                Private  methods

     *************************************************************************/

    /**
     * Helper function to call inside {@link #sortOneStep()}.
     * Initialises variables after the end of partitioning.
     */
     private void initialiseVariables() {
        pivot = list.get(0).get(end);
        index = start;
        jindex = start;
    }
}
