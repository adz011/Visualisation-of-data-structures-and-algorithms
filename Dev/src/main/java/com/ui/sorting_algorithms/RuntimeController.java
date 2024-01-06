package com.ui.sorting_algorithms;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.lang.ref.PhantomReference;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @version 1.0
 *
 */
public class RuntimeController implements Initializable {
    /**************************************************************************

                                Global variables

     *************************************************************************/
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private BarChart Runtime1;
    @FXML
    private BarChart Runtime2;
    private ArrayList<String> list;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = new ArrayList<>();

    }

    public void show() {
        anchorPane.setVisible(true);
        anchorPane.setManaged(true);
    }

    public void hide() {
        anchorPane.setVisible(false);
        anchorPane.setManaged(false);
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void remove() {

    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void addElement(String s) {
        list.add(s);
    }

    public void removeElement(String s){
        list.remove(s);
    }


    public void setupBasicChart(ArrayList<Integer> data) {
        for (int i =0; i<list.size(); i++) {
            XYChart.Series series1 = new XYChart.Series();
            series1.setName(list.get(i));
            series1.getData().add(new XYChart.Data("runtime", data.get(i)));
            Runtime1.getData().add(series1);
        }

    }

    public void setupComplexChart(ArrayList<Integer> data) {
        XYChart.Series series1 = new XYChart.Series();
        for (int i =0; i<list.size(); i++) {
            series1.setName(list.get(i));
            series1.getData().add(new XYChart.Data("runtime", data.get(i)));
        }
        Runtime2.getData().add(series1);
    }

    public void clearChartsData(){
        Runtime1.getData().clear();
    }
}
