package com.ui.sorting_algorithms;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import com.engine.sorting_algorithms.BubbleSortManager;
import com.engine.sorting_algorithms.MergeSortManager;
import com.engine.sorting_algorithms.QuickSortManager;
import com.engine.sorting_algorithms.SortingAlgorithmsManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortingController implements Initializable {

    public Button helpAlert;
    /**************************************************************************

                                Global variables
     *************************************************************************/
    @FXML
    private BorderPane borderPane;

    @FXML
    private Button submitButton;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private CheckBox quickSortCheckBox;

    @FXML
    private CheckBox bubbleSortCheckBox;

    @FXML
    private CheckBox mergeSortCheckBox;

    @FXML
    private Slider changeSpeed;

    @FXML
    private TextField inputText;

    @FXML
    private GridPane grid;

    private RuntimeController runtimeController;

    private CheckBox[] algorithmsCheckBoxes;

    private ArrayList<Integer> runtimeCounter;

    private DrawingController drawingController;

    private PlayHelper playHelper;

    private Label[] algorithmsNames;

    private ArrayList<GridPane> subGrids;

    private ArrayList<SortingAlgorithmsManager> algorithmManagers;

    private ArrayList<ArrayList<Integer>> temporaryList;

    private int rowCount;

    private static boolean SORTSTATE = false;

    final int maxRunningAlgorithms = 4;

    final int listSize = 20;

    private static final int timeStamp = 1;

    private static final String runtimeView = "runtime-view.fxml";

    private SimpleDoubleProperty height;

    private SimpleDoubleProperty width;

    /**************************************************************************

     FXML Methods
     *************************************************************************/

    /**
     * @param url            The location used to resolve relative paths for the root object, or
     *                       {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if
     *                       the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            createControllers();
            runtimeController.hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialiseAlgorithmsNames();
        initialiseAlgorithmManagers();
        initialiseTempArray();
        initialiseSubGrids();
        initialiseCheckBoxes();
        initialiseDrawingController();
        bindVariables();

    }

    /**
     * @param event
     */
    public void submit(ActionEvent event) {
        int currentInt = 0;
        rowCount = 0;
        String input;


        initialiseTempArray();

        // get string from text input
        //TODO add more ways to detect values from string
        input = inputText.getText();
        Pattern pattern = Pattern.compile("\\d{1,2}");
        ArrayList<String> filteredString = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String value = matcher.group();
            // TODO introduce global variable for a maximum value in a list
            filteredString.add(Integer.parseInt(value) > 20 ? "20" : value);

        }


        for (String x : filteredString) {
            currentInt = Integer.parseInt(x);
            temporaryList.get(0).add(currentInt);
            if (currentInt > rowCount) {
                rowCount = currentInt;

            }
        }

        // Create grid
        System.out.println(rowCount);
        setNamesVisibleProperty(false);
        for (GridPane subGrid : subGrids) {
            drawingController.clearGrid(subGrid);
        }
        drawingController.showGridSingleRectangle(subGrids.get(0), temporaryList, rowCount, new ArrayList<>(), colorPicker.getValue(), height.get(), width.get());

    }

    public void randomize() {
        rowCount = 0;
        int currentValue;
        initialiseTempArray();
        clearGrid();


        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        for (int i = 0; i < listSize; i++) {
            currentValue = random.nextInt(1, 20);
            if (currentValue > rowCount) rowCount = currentValue;
            temporaryList.get(0).add(currentValue);

        }

        setNamesVisibleProperty(false);
        drawingController.showGridSingleRectangle(subGrids.get(0), temporaryList, rowCount, new ArrayList<>(), colorPicker.getValue(), height.get(), width.get());

    }

    public void play() throws InterruptedException, IOException {
        setup();
        if (algorithmManagers.isEmpty() || temporaryList.get(0).isEmpty()) {
            System.out.println("No algorithms chosen");
        } else {

            SORTSTATE = true;
            playHelper = new PlayHelper(timeStamp);
            playHelper.start();
            changeSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
                        System.out.println(newValue);
                        inputText.setText("test");
                        if (SORTSTATE) {
                            playHelper.setTimeStamp(timeStamp, newValue.doubleValue());
                        }
                    }
            );
        }
    }


    public Boolean playOne() {
        boolean hasFinished = true;
        if (SORTSTATE) {
            for (int i = 0; i < algorithmManagers.size(); i++) {
                if (!algorithmManagers.get(i).sortOneStep()) {
                    runtimeCounter.set(i, runtimeCounter.get(i) + 1);
                    hasFinished = false;
                }
                ;

                drawingController.showGridSingleRectangle(subGrids.get(i), algorithmManagers.get(i).getList(), rowCount, algorithmManagers.get(i).getCurrentPointers(), colorPicker.getValue(), height.get(), width.get());
                System.out.println(height.get());
                System.out.println(width.get());
            }

            if (hasFinished) {
                SORTSTATE = false;
                showDataCharts();

            }

        }
        return hasFinished;
    }

    public void stop() {
        playHelper.stop();
    }

    public void shuffle() {
        Collections.shuffle(temporaryList.get(0));
        for (SortingAlgorithmsManager am : algorithmManagers) {
            am.setList(temporaryList);
            am.initialise();
        }
        drawingController.showGridSingleRectangle(subGrids.get(0), temporaryList, rowCount, new ArrayList<>(), colorPicker.getValue(), height.get(), width.get());

    }

    public void skip(ActionEvent event) {
        playHelper.setTimeStamp(timeStamp, 10000000);
    }

    public void resume(ActionEvent event) {
        if (SORTSTATE) {
            playHelper.start();
        }
    }

    /**************************************************************************

     Private Methods
     *************************************************************************/

    private void bindVariables() {
        height = new SimpleDoubleProperty();
        width = new SimpleDoubleProperty();
        height.bind(borderPane.heightProperty());
        width.bind(borderPane.widthProperty());

    }

    /**
     * Method to handle algorithm managers instances inside {@link #algorithmManagers}
     *
     * @param event
     */
    private void chooseMultipleAlgorithmBox(Event event) {
        for (CheckBox checkBox : algorithmsCheckBoxes) {
            boolean doesExist = false;

            String chosenAlgorithm = checkBox.getText();
            switch (chosenAlgorithm) {
                case "BubbleSort" -> {
                    if (checkBox.isSelected()) {
                        for (SortingAlgorithmsManager am : algorithmManagers) {
                            if (am instanceof BubbleSortManager) {
                                doesExist = true;
                                break;
                            }
                        }
                        if (!doesExist) {
                            algorithmManagers.add(new BubbleSortManager());
                            runtimeController.addElement("BubbleSort");
                        }
                    } else {
                        algorithmManagers.removeIf(am -> am instanceof BubbleSortManager);
                        runtimeController.removeElement("BubbleSort");
                    }

                }
                case "MergeSort" -> {
                    if (checkBox.isSelected()) {
                        for (SortingAlgorithmsManager am : algorithmManagers) {
                            if (am instanceof MergeSortManager) {
                                doesExist = true;
                                break;
                            }

                        }
                        if (!doesExist) {
                            algorithmManagers.add(new MergeSortManager());
                            runtimeController.addElement("MergeSort");
                        }
                    } else {
                        algorithmManagers.removeIf(am -> am instanceof MergeSortManager);
                        runtimeController.removeElement("MergeSort");
                    }
                }
                case "QuickSort" -> {
                    if (checkBox.isSelected()) {
                        for (SortingAlgorithmsManager am : algorithmManagers) {
                            if (am instanceof QuickSortManager) {
                                doesExist = true;
                                break;
                            }
                        }
                        if (!doesExist) {
                            algorithmManagers.add(new QuickSortManager());
                            runtimeController.addElement("QuickSort");
                        }
                    } else {
                        algorithmManagers.removeIf(am -> am instanceof QuickSortManager);
                        runtimeController.removeElement("QuickSort");
                    }
                }
            }

        }
    }


    /**
     * Initialises all necessary sub-controllers of this class.
     * Current version only links {@link RuntimeController}
     *
     * @throws IOException if resource not found
     */
    private void createControllers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(runtimeView));
        Parent root = fxmlLoader.load();
        runtimeController = fxmlLoader.getController();
        borderPane.setRight(runtimeController.getAnchorPane());

    }


    /**
     * Initialisation of all sub-grids of {@link #grid} to display
     * active {@link SortingAlgorithmsManager}.
     */
    private void initialiseSubGrids() {
        subGrids = new ArrayList<>(maxRunningAlgorithms);

        int x = (int) Math.ceil(Math.sqrt(maxRunningAlgorithms));
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                subGrids.add(new GridPane());
                grid.add(subGrids.get(i * 2 + j), j, i * 2);
                grid.add(algorithmsNames[(i * 2) + j], j, (i * 2) + 1);
                grid.setMaxHeight(10000);
                grid.setMaxHeight(10000);
            }

        }

        grid.setGridLinesVisible(false);
        grid.setHgap(20);

    }

    /**
     * Method to reset {@link #temporaryList}
     */
    private void initialiseTempArray() {
        temporaryList = new ArrayList<>();
        temporaryList.add(new ArrayList<>());
        rowCount = 0;
    }

    /**
     * Sets all names of active algorithm managers.
     */
    private void initialiseAlgorithmsNames() {
        Label label1 = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        Label label4 = new Label();
        algorithmsNames = new Label[]{label1, label2, label3, label4};
        setNamesVisibleProperty(false);
    }

    /**
     * Clears all names of algorithm managers.
     */
    private void clearAlgorithmsNames() {
        for (int i = 0; i < maxRunningAlgorithms; i++) {
            algorithmsNames[i].setText("");
        }
    }

    /**
     * Initialisation of {@link #algorithmManagers}.
     */
    private void initialiseAlgorithmManagers() {
        algorithmManagers = new ArrayList<>();
    }

    private void initialiseCheckBoxes() {
        algorithmsCheckBoxes = new CheckBox[]{bubbleSortCheckBox, mergeSortCheckBox, quickSortCheckBox};
        for (CheckBox checkBox : algorithmsCheckBoxes) {
            checkBox.setOnAction(this::chooseMultipleAlgorithmBox);

        }
    }

    /**
     * Initialisation of {@link #drawingController}.
     */
    private void initialiseDrawingController() {
        drawingController = new DrawingController();
    }

    /**
     * Sets names of the {@link #algorithmManagers} to be active or inactive.
     *
     * @param isVisible set to true displays all names of active {@link #algorithmManagers}
     */
    private void setNamesVisibleProperty(boolean isVisible) {
        for (int i = 0; i < maxRunningAlgorithms; i++) {
            algorithmsNames[i].setVisible(isVisible);
        }
    }


    /**
     * Methode to be invoked before setting {@link #SORTSTATE} to true.
     */
    private void setup() {
        for (GridPane subGrid : subGrids) {
            drawingController.clearGrid(subGrid);
        }
        SORTSTATE = true;
        runtimeController.hide();
        runtimeCounter = new ArrayList<>();
        clearAlgorithmsNames();
        for (int i = 0; i < algorithmManagers.size(); i++) {
            runtimeCounter.add(0);
            algorithmManagers.get(i).setList(temporaryList);
            algorithmManagers.get(i).initialise();
            algorithmsNames[i].setText(algorithmManagers.get(i).toString());
        }
        setNamesVisibleProperty(true);

    }


    /**
     * @return {@link #borderPane}
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void showHelpAlert(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(
                """
                        One step button computes exactly one sorting operation. \s
                        \n
                        Play button starts sorting process, default speed is set to 1 operation per second. If clicked during sorting process, it resets. \s
                        \n
                        Skip button increases the speed of sorting to maximum, which is 30 operations per second. \s
                        \n
                        Stop button halts the automatic sorting process. \s
                        \n
                        Resume button resumes :') \s
                        \n
                        Slider changes the speed of automatic generation. \s
                        \n
                        Submit button takes whatever has been inputted, and tries to convert it to list of values.\s
                        \n
                        Randomise generates random sequence of 20 values from 0 to 20. \s
                        \n
                        Shuffle button randomly switches places of values of current list. It does nothing if list is empty."""
        );
        alert.show();
    }

    /**
     * Inner Helper class to help threads management.
     */

    private class PlayHelper extends AnimationTimer {
        long multiplier = 1000000000L;
        private double timeStamp;

        PlayHelper(double timeStamp) {

            this.timeStamp = timeStamp * multiplier;
        }

        private long prevTime = 0;

        public void setTimeStamp(double timeStamp, double inputMultiplier) {
            this.timeStamp = timeStamp * multiplier / inputMultiplier;
        }

        @Override
        public void handle(long now) {
            try {
                if (now - prevTime > timeStamp) {
                    prevTime = now;
                    doHandle();
                    //
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void doHandle() throws InterruptedException {

            if (playOne()) {
                stop();
            }
            ;

        }

    }

    /**
     * Method that groups all necessary calls to display runtime data.
     */
    private void showDataCharts() {
        runtimeController.clearChartsData();
        runtimeController.setupBasicChart(runtimeCounter);
        runtimeController.show();
    }


    /**
     * Method to call all sub-grids of {@link #grid}.
     */
    public void clearGrid() {
        for (GridPane subGrid : subGrids) {
            drawingController.clearGrid(subGrid);
        }
    }
}