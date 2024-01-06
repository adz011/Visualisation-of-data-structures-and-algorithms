package com.ui.main;

import com.ui.sorting_algorithms.SortingController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import com.ui.game_of_life.GridController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    /**************************************************************************

                                Global Variables

     *************************************************************************/
    public Button gameoflifeButton;
    public Button pathfindingButton;
    public Button sortingButton;
    public BorderPane borderPane;
    GridController gridController;
    SortingController sortingController;
    final String gameOfLifePath = "grid-view.fxml";
    final String sortingPath = "sorting-view.fxml";

    /**************************************************************************

                                    Methods

     *************************************************************************/

    /**
     *
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initialiseControllers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameoflifeButton.setOnMouseClicked(this::loadGameOfLife);
        sortingButton.setOnMouseClicked(this::loadSorting);
    }

    /**
     * Loads all necessary assets for displaying Sorting algorithms
     *
     * @param mouseEvent
     */
    private void loadSorting(MouseEvent mouseEvent) {
        borderPane.setCenter(sortingController.getBorderPane());

    }

    /**
     * Loads all necessary assets for displaying Game of Life
     * @param mouseEvent
     */
    private void loadGameOfLife(MouseEvent mouseEvent) {
        borderPane.setCenter(gridController.getBorderPane());
    }


    /**
     * Initialisation of all main controllers.
     * @throws IOException
     */
    public void initialiseControllers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(gameOfLifePath));
        fxmlLoader.load();
        gridController = fxmlLoader.getController();
        fxmlLoader = new FXMLLoader(getClass().getResource(sortingPath));
        fxmlLoader.load();
        sortingController = fxmlLoader.getController();

    }
}
