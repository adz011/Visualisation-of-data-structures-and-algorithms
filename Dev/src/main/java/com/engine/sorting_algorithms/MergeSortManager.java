package com.engine.sorting_algorithms;

import javafx.util.Pair;

import java.util.*;

import static java.lang.Integer.valueOf;

/**
 * @version 1.0
 *
 * Class that implements functionality of Merge Sort algorithm
 *
 */
public class MergeSortManager extends SortingAlgorithmsManager {

    /**************************************************************************

                                 Global variables

     *************************************************************************/

    private ArrayList<Boolean> tempList;

    private int currentIndex, numOfDivision, currentNumOfDivision;

    private boolean sortStep, isMergeFinished, hasMergeStarted, showPointers;

    static final int nullPointer = 10000;


    /**************************************************************************

                                  Constructors

     *************************************************************************/

    public MergeSortManager() {

    }

    /**************************************************************************

                                Override methods

     *************************************************************************/


    /**
     *
     * @return
     */
    @Override
    public boolean sortOneStep() {
        /* start dividing */
        if (currentNumOfDivision < numOfDivision) {
            ArrayList<Pair<Integer,Integer>> tempList;
            if (showPointers) {
                tempList = new ArrayList<>();
                tempList.add(new Pair<>(currentIndex,nullPointer));
                setCurrentPointers(tempList);
                showPointers = false;
            } else {
                divide(currentIndex);
                tempList = new ArrayList<>();
                tempList.add(new Pair<>(currentIndex,nullPointer));
                tempList.add(new Pair<>(currentIndex+1,nullPointer));
                setCurrentPointers(tempList);
                currentIndex += 2;
                if (currentIndex >= list.size()) {
                    currentIndex = 0;
                    currentNumOfDivision++;
                }
                showPointers =true;
            }
            return false;
        }
        /* first iteration of sorting */
        else if (sortStep) {
            while (showPointers && list.get(currentIndex).size() == 1) {
                currentIndex++;
            }
            if (showPointers) {
                ArrayList<Pair<Integer, Integer>> tempList = new ArrayList<>();
                tempList.add(new Pair<>(currentIndex, 0));
                tempList.add(new Pair<>(currentIndex, 1));
                setCurrentPointers(tempList);
                showPointers = false;
            } else {
                showPointers = true;
                sortListOfTwo(currentIndex);
                currentIndex++;
                if (currentIndex >= list.size()) {
                    currentIndex = 0;
                    sortStep = false;

                }

            }
            return false;
        }

        /* merge */
        if (list.size() > 1) {
            if (!hasMergeStarted) {
                hasMergeStarted = true;
                list.add(currentIndex, new ArrayList<>());
            }
            if (showPointers) {
                ArrayList<Pair<Integer, Integer>> tempList = new ArrayList<>();
                if (list.get(currentIndex + 1).isEmpty()) {
                    tempList.add(new Pair<>(currentIndex + 2, 0));
                } else if (list.get(currentIndex + 2).isEmpty()) {
                    tempList.add(new Pair<>(currentIndex + 1, 0));
                } else {
                    tempList.add(new Pair<>(currentIndex + 2, 0));
                    tempList.add(new Pair<>(currentIndex + 1, 0));
                }
                setCurrentPointers(tempList);
                showPointers = false;
            } else {
                showPointers = true;
                int tempCurrentIndex = currentIndex;
                if (merge(currentIndex, currentIndex + 1, currentIndex + 2)) {
                    list.remove(currentIndex + 1);
                    list.remove(currentIndex + 1);
                    hasMergeStarted = false;
                    if (currentIndex + 1 >= list.size()) {
                        currentIndex = 0;
                    } else currentIndex++;

                }
                ArrayList<Pair<Integer, Integer>> tempList = new ArrayList<>();
                tempList.add(new Pair<>(tempCurrentIndex, list.get(tempCurrentIndex).size() - 1));
                setCurrentPointers(tempList);
            }

        } else return true;
        return false;

    }

    /**
     * Fully initialise this Algorithm Manager.
     * Previous list won't be saved after call.
     */
    @Override
    public void initialise() {
        currentIndex = 0;
        showPointers = true;
        isMergeFinished = false;
        hasMergeStarted = false;
        currentNumOfDivision = 0;
        sortStep = true;
        tempList = new ArrayList<>(Collections.singleton(false));
        highestValue = 0;
        pointers = new ArrayList<>();
        numOfDivision = (int) (Math.log(list.get(0).size()) / Math.log(2));
    }

    @Override
    public String toString() {
        return "MergeSort";
    }

    /**************************************************************************

                                Private  methods

     *************************************************************************/

    /**
     * Helper function to call inside {@link #sortOneStep()}.
     * It sorts lists of two elements.
     * @param index of the list to be sorted
     */
    private void sortListOfTwo(int index) {
        if (list.get(index).get(0) > list.get(index).get(1)) {
            list.get(index).add(0, list.get(index).remove(1));
        }
    }

    /**
     * Helper function to call inside {@link #sortOneStep()}.
     * Divides a list into two of the same length if possible, otherwise
     * left side list is longer by 1 element.
     * @param index of the list to be divided
     */
    private void divide(int index) {
        if (list.get(index).size() < 2) {
            tempList.set(index, true);
        } else {
            ArrayList<Integer> leftsideList = new ArrayList<>();
            ArrayList<Integer> rightsideList = new ArrayList<>();

            int halfSize = list.get(index).size() / 2;
            int j = 0;

            while (j < halfSize) {
                leftsideList.add(list.get(index).get(0));
                list.get(index).remove(0);
                j++;
            }
            while (0 < (list.get(index).size())) {
                rightsideList.add(list.get(index).get(0));
                list.get(index).remove(0);
            }

            tempList.remove(index);

            if (leftsideList.size() == 1) {
                tempList.add(index, true);
            } else tempList.add(index, false);

            if (rightsideList.size() == 1) {
                tempList.add(index + 1, true);
            } else tempList.add(index + 1, false);


            list.remove(index);
            list.add(index, leftsideList);
            list.add(index + 1, rightsideList);


        }
    }

    /**
     * Helper function to call inside {@link #sortOneStep()}.
     * @param index0
     * @param index1
     * @param index2
     * @return
     */
    private Boolean merge(int index0, int index1, int index2) {
        if (list.get(index1).size() == 0) {
            list.get(index0).add(list.get(index2).remove(0));
            return list.get(index2).size() == 0;
        } else if (list.get(index2).size() == 0) {
            list.get(index0).add(list.get(index1).remove(0));
            return list.get(index1).size() == 0;
        } else {
            if (list.get(index1).get(0) < list.get(index2).get(0)) {
                list.get(index0).add(list.get(index1).remove(0));

            } else list.get(index0).add(list.get(index2).remove(0));

            return false;
        }
    }

}
