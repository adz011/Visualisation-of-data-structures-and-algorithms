package com.ui.game_of_life;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @version 1.0
 * Class responsible for real-time generation according to
 * rules of the Game of Life. Current version supports changing cells'
 * states while the program generates. Maximum amount of generating states
 * can be set up to 30 per second, which indicates 30 frames per second.
 */
public class GridController implements Initializable {
    /**************************************************************************

                                 Global Variables

     *************************************************************************/
    public GridPane grid;
    public Button helpAlert;
    @FXML
    private Button createGrid;
    @FXML
    private ColorPicker deadCellColor;
    @FXML
    private ColorPicker aliveCellColor;
    private ObjectProperty<Paint> deadCellPaint;
    private ObjectProperty<Paint> aliveCellPaint;
    int gridHeight, gridWidth;
    DoubleProperty height, width;
    public BorderPane borderPane;
    public Button startButton;
    public Button playButton;
    public Slider changeSpeed;
    public Button stopButton;
    int[][] helperArray, tiles;
    private GridDrawingController gridDrawingController;
    PlayHelper playHelper;
    final static double timeStamp = 1;
    final int cellWidth = 20;
    final int cellHeight = 20;
    final int hGap = 2;
    final int vGap = 2;
    final int[][] directions = {
        {-1, -1}, {-1,  0}, {-1,  1},
        { 0, -1},           { 0,  1},
        { 1, -1}, { 1,  0}, { 1,  1}
    };


    /**
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridDrawingController = new GridDrawingController();
        bindVariables();
        startButton.setOnMouseClicked(this::oneStep);
        playButton.setOnMouseClicked(this::play);
        playHelper = new PlayHelper(timeStamp);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void createGrid(ActionEvent event) {
        initialiseGridPane();
        initialiseTilesArray();
    }

    /**************************************************************************

                              Private Methods

     *************************************************************************/


    /**
     * Full initialisation of {@link #grid}.
     * This includes:
     * - Setting on mouse drag events
     * - Adding event filters to all {@link  #grid} nodes
     * - Drawing {@link #grid} on the screen.
     */
    private void initialiseGridPane() {
        // create new grid
        grid = new GridPane();
        borderPane.setCenter(grid);

        // create tiles and set a gap between them
        initialiseTiles();
        grid.setVgap(vGap);
        grid.setHgap(hGap);

        setGridConstraints();
        gridDrawingController.draw(grid, tiles, gridWidth, gridHeight, deadCellPaint, aliveCellPaint, cellWidth, cellHeight);
        setupEventHandlers();

    }

    /**
     * Method to change tile parameter to either Dead or Alive
     *
     * @param tile indicating position on the grid
     */
    private void checkTile(Rectangle tile) {
        if (tile.getFill() == deadCellColor.getValue()) {
            tile.setFill(aliveCellPaint.getValue());

        } else {
            tile.setFill(deadCellPaint.getValue());
        }
        setTile(GridPane.getColumnIndex(tile), GridPane.getRowIndex(tile));
    }

    /**
     * Sets all cells to value 0, meaning all cells are dead.
     */
    private void initialiseTilesArray() {
        for (int i = 0; i < gridWidth; i++) {
            Arrays.fill(tiles[i], 0);
        }
    }

    /**
     * @return
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }


    /**
     * Method that computes one generation based on Game of Life rules.
     * Rules are defined as follows:
     * - Population: If cell is dead and has exactly 3 neighbours then it populates (resurrects)
     * - Survival: If cell is alive and has exactly 2 or 3 neighbours then it survives (stays alive)
     * - Overpopulation: If cell is alive and has more than 3 neighbours then it dies.
     * - Solitude: If cell is alive and has less than 2 neighbours, it dies.
     *
     * @param mouseEvent
     */
    public void oneStep(MouseEvent mouseEvent) {
        // calculate number of neighbours
        int neighboursQuantity;
        helperArray = new int[gridWidth][gridHeight];
        for (int i = 0; i < gridWidth; i++) {
            Arrays.fill(helperArray[i], 0);
        }
        for (int i = 1; i < gridWidth - 1; i++) {
            for (int j = 1; j < gridHeight - 1; j++) {
                neighboursQuantity = 0;
                Rectangle rc = (Rectangle) grid.getChildren().get(i * gridHeight + j);

                for(int[] x : directions){
                    neighboursQuantity += tiles[i + x[0]][j + x[1]];
                }

                // Rules of the Game of Life:

                // Population
                if (tiles[i][j] == 0 && neighboursQuantity == 3) {
                    rc.setFill(aliveCellPaint.getValue());
                    helperArray[i][j] = 1;
                }

                // Survival
                if (tiles[i][j] == 1 && (neighboursQuantity == 3 || neighboursQuantity == 2)) {
                    helperArray[i][j] = 1;
                    rc.setFill(aliveCellPaint.getValue());
                }

                //Overpopulation
                if (tiles[i][j] == 1 && neighboursQuantity > 3) {
                    rc.setFill(deadCellPaint.getValue());
                }

                //Solitude
                if (neighboursQuantity <= 1) {
                    rc.setFill(deadCellPaint.getValue());
                }
            }
        }
        tiles = helperArray;
    }

    /**
     * Changes tile's state: from alive to dead and vice versa.
     *
     * @param columnIndex of tile
     * @param rowIndex of tile
     */
    private void setTile(int columnIndex, int rowIndex) {
        tiles[columnIndex][rowIndex] = tiles[columnIndex][rowIndex] == 1 ? 0 : 1;
    }


    /**
     * This method enables drag movement, so it's possible
     * to change state of multiple cells at once. It's IMPORTANT
     * to call it after all nodes have been added, otherwise
     * they won't behave as expected.
     */
    private void setupEventHandlers(){
        grid.addEventHandler(MouseDragEvent.DRAG_DETECTED, e -> {
            grid.startFullDrag();
        });
        for (Node x : grid.getChildren()) {
            x.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> checkTile((Rectangle) x));
            x.setOnMouseClicked(e -> checkTile((Rectangle) x));
        }
    }
    /**
     * Creates space for each individual cell.
     */
    private void setGridConstraints(){
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();

        for (int i = 0; i < gridWidth; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }
        for (int j = 0; j < gridHeight; j++) {
            grid.getRowConstraints().add(new RowConstraints(cellHeight));
        }
    }

    /**
     *
     * @param event
     */
    public void stop(ActionEvent event) {
        playHelper.stop();
    }

    public void showHelpAlert(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(
                """
                        Create Grid button creates a grid based on your screen size and chosen color palette. Click on it at the beginning to start Game of Life or whenever you need to refresh your color palette or grid size.\s
                        \n
                        One Step button computes exactly one generation based on the current state of cells. It is useful to see how Game of Life works step by step. \s
                        \n
                        Play button starts automatic generation, default speed is set to 1 per second. \s
                        \n
                        Stop button holds the automatic generation process. \s
                        \n
                        Slider changes the speed of automatic generation."""
        );
        alert.show();
    }

    /**
     * Inner class that creates new thread for generating states.
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
            oneStep(null);
        }


    }

    /**
     * Method that binds UI and Backend variables that change in real time.
     */
    private void bindVariables() {
        height = new SimpleDoubleProperty();
        width = new SimpleDoubleProperty();
        height.bind(borderPane.heightProperty());
        width.bind(borderPane.widthProperty());

        aliveCellPaint = new SimpleObjectProperty<>();
        deadCellPaint = new SimpleObjectProperty<>();

        aliveCellPaint.bind(aliveCellColor.valueProperty());
        deadCellPaint.bind(deadCellColor.valueProperty());
    }

    /**
     * Method that invokes start of the generation process.
     * Default timestamp between each is set to 1 second.
     * @param mouseEvent
     */
    private void play(MouseEvent mouseEvent) {
        playHelper.start();
        changeSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            playHelper.setTimeStamp(timeStamp, newValue.doubleValue());
        });
    }

    private void initialiseTiles(){
        gridHeight = (int) (height.get() / (cellHeight + hGap) - 4);
        gridWidth = (int) (width.get() / (cellWidth + vGap));
        tiles = new int[gridWidth][gridHeight];
    }

}

