package com.engine.sorting_algorithms;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * @version 1.0
 * <p>
 */
public abstract class SortingAlgorithmsManager {

    int highestValue;
    ArrayList<ArrayList<Integer>> list;
    ArrayList<Pair<Integer, Integer>> pointers;


    /**
     * Method for calling one step of the algorithm.
     * It's consequences are based on the current state of the algorithm manager.
     * Outcomes can be either:
     * - setting pointers
     * - swap of the elements in list
     * - creation of new sublist in the list
     * - deletion of sublist in the list
     * - none if returns true
     * @return if sorting is completed
     */
    public abstract boolean sortOneStep();

    /**
     *
     * @param list Dataset of integers. Proper list will contain only Integer values in the 0 index.
     */
    public void setList(ArrayList<ArrayList<Integer>> list) {
        this.list = new ArrayList<>();
        this.list.add(new ArrayList<>());
        for (Integer x : list.get(0)) {
            this.list.get(0).add(x);

        }
    }

    /**
     *
     * @return current state of the list
     */
    public ArrayList<ArrayList<Integer>> getList() {
        return list;
    }

    /**
     *
     * @param value biggest integer of the list
     */
    public void setHighestValue(int value){
        highestValue = value;
    };

    /**
     *
     * @return biggest integer of the list
     */
    public int getHighestValue(){
        return highestValue;
    };

    /**
     *
     * @param pointers
     */
    public void setCurrentPointers(ArrayList<Pair<Integer, Integer>> pointers){
        this.pointers = pointers;
    };

    /**
     *
     * @return pointers
     */
   public ArrayList<Pair<Integer, Integer>> getCurrentPointers(){
        return pointers;
    };

    /**
     * Initialisation of the internal state of a manager.
     * Must be called after {@link #setList(ArrayList)} and before {@link #sortOneStep()} function.
     * To reset existing manager call this method after {@link #setList(ArrayList)}.
     */
   public abstract void initialise();

}
