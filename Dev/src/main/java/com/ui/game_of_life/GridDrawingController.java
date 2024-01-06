package com.ui.game_of_life;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GridDrawingController {

    public void draw(GridPane grid, int[][] tiles, int gridWidth, int gridHeight, ObjectProperty<Paint> deadCellPaint, ObjectProperty<Paint> aliveCellPaint, int cellWidth, int cellHeight) {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Rectangle rc = new Rectangle(cellWidth, cellHeight);
                rc.setStroke(deadCellPaint.get());
                if (tiles[i][j] == 0) {
                    rc.setFill(deadCellPaint.get());
                } else rc.setFill(aliveCellPaint.get());

                GridPane.setConstraints(rc, i, j);
                grid.getChildren().add(rc);
            }
        }
    }
}
