package com.ui.sorting_algorithms;

import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import com.engine.sorting_algorithms.SortingAlgorithmsManager;

import java.util.ArrayList;

/**
 * @version 1.0
 * <p>
 */
public class DrawingController {

    @FXML
    ColorPicker colorPicker;
    Color baseColor;


    public DrawingController() {
        colorPicker = new ColorPicker();
    }

    /**
     * Returns predefined set of colors. Default value is set to return
     * color black
     * @param value integer from 1 to 10.
     * @return {@link Color}
     */
    private Color getColor(int value) {
        return switch (value) {
            case 1 -> Color.web("#347B98");
            case 2 -> Color.web("#66B032");
            case 3 -> Color.web("#B2D732");
            case 4 -> Color.web("#FEFE33");
            case 5 -> Color.web("#FCCB1A");
            case 6 -> Color.web("#FB9902");
            case 7 -> Color.web("#FC600A");
            case 0 -> Color.web("#0247FE");
            default -> Color.BLACK;
        };
    }

    /**
     * Method that draws a single rectangle for each value in a list.
     *
     * @param grid Grid to display on
     * @param arrayList List of values
     * @param rowCount This value is equal to the biggest integer in the arrayList
     * @param pointers
     * @param color
     */
    public void showGridSingleRectangle(GridPane grid, ArrayList<ArrayList<Integer>> arrayList, int rowCount, ArrayList<Pair<Integer, Integer>> pointers, Color color, double screenHeight, double screenWidth) {
        clearGrid(grid);
        int tileWidth = (int) (screenWidth / 20 / 6);
        int tileHeight = (int) (screenHeight / 20 / 2.5);
        int size = 0;
        for (int j = 0; j < arrayList.size(); j++) {
            for (int i = 0; i < arrayList.get(j).size(); i++) {
                Rectangle rectangle = new Rectangle(tileWidth, tileHeight * arrayList.get(j).get(i));
                Stop[] greyStops = new Stop[]{
                        new Stop(0, Color.DARKGRAY),
                        new Stop(1, Color.LIGHTGRAY)
                };
                LinearGradient linearGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, greyStops);
                rectangle.setStroke(color.brighter().brighter().brighter());
                rectangle.setFill(linearGradient);
                for (Pair<Integer, Integer> pointer : pointers) {
                    if (pointer.getKey() == j) {
                        if (pointer.getValue() == i) {
                            Stop[] redStops = new Stop[]{
                                    new Stop(0, Color.DARKRED),
                                    new Stop(1, Color.RED)
                            };
                            LinearGradient linearGradientRed = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, redStops);
                            rectangle.setFill(linearGradientRed);
                            break;
                        } else {
                            Stop[] diffStops = new Stop[]{
                                    new Stop(0, getColor(j%7).darker()),
                                    new Stop(1, getColor(j%7).brighter())
                            };
                            LinearGradient linearGradientDiff= new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, diffStops);
                            rectangle.setFill(linearGradientDiff);
                        }
                    }
                }
                grid.add(rectangle, size, 0);
                GridPane.setValignment(rectangle, VPos.BOTTOM);
                Button label = new Button();

                // put an empty space if value is less than 0, to make it look more consistent
                if(arrayList.get(j).get(i)<10) label.setText(arrayList.get(j).get(i).toString() + " ");
                else label.setText(arrayList.get(j).get(i).toString());

                grid.add(label, size, 1);
                size++;
            }
        }


        /* Add array inputs to the grid */

        for (int i = 0; i < arrayList.size(); i++) {

        }
    }

    /**
     * Optional function to {@link #showGridSingleRectangle(GridPane, ArrayList, int, ArrayList, Color)}.
     * It draws squares on top of each other, each column of the length equal to rowCount.
     * @param grid Grid to display on
     * @param arrayList List of values
     * @param rowCount This value is equal to the biggest integer in the arrayList
     * @param pointers
     */
    public void showGrid(GridPane grid, ArrayList<ArrayList<Integer>> arrayList, int rowCount, ArrayList<Pair<Integer, Integer>> pointers) {
        double maxRectangleSize = 20;

        baseColor = Color.hsb(colorPicker.getValue().getHue(), 1.0, 1.0);
        clearGrid(grid);
        /*  Pointers **/
        int size = 0;
        /* iterate through list of lists */
        for (int j = 0; j < arrayList.size(); j++) {
            /* proceed only if current list is not empty*/
            if (arrayList.get(j) != null) {
                /* iterate through all elements in a current list */
                for (int k = 0; k < arrayList.get(j).size(); k++) {
                    /* iterate the highest element of the list times */
                    for (int i = 0; i < rowCount; i++) {
                        double x = i / 100;
                        // rectangle.setStroke(Color.BLACK);
                        Rectangle rectangle = new Rectangle(maxRectangleSize, maxRectangleSize);
                        rectangle.setStroke(Color.BLACK);
                        rectangle.setFill(Color.GREY);
                        for (Pair<Integer, Integer> pointer : pointers) {
                            if (pointer.getKey() == j) {
                                if (pointer.getValue() == k) {
                                    rectangle.setFill(Color.RED);
                                    break;
                                } else rectangle.setFill(getColor(j % 7));
                            }
                        }
                        if (rowCount - i > arrayList.get(j).get(k)) {
                            rectangle.setFill(Color.WHITE);
                        }

                        grid.add(rectangle, size, i);
                    }
                    TextField label = new TextField();
                    label.prefHeight(maxRectangleSize);
                    label.prefWidth(maxRectangleSize);
                    label.setText(arrayList.get(j).get(k).toString());
                    grid.add(label, size, rowCount);
                    size++;
                }
            }
        }

    }


    public void clearGrid(GridPane grid) {
        grid.getChildren().clear();
    }
}
