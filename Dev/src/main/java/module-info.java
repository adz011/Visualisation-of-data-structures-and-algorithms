module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.base;




    opens com.engine.sorting_algorithms to javafx.fxml;
    exports com.ui.sorting_algorithms;
    opens com.ui.sorting_algorithms to javafx.fxml;
    exports com.ui.main;
    opens com.ui.main to javafx.fxml;
    exports com.ui.game_of_life;
    opens com.ui.game_of_life to javafx.fxml;

}