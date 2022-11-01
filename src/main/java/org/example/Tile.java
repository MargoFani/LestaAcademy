package org.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tile extends Rectangle {

    private boolean isFree;

    private Stone stone;

    public boolean hasStone() {
        return stone != null;
    }

    public Stone getStone() {
        return stone;
    }

    public void setStone(Stone s) {
        this.stone = s;
    }

    public boolean isFree() {
        return isFree;
    }

    public Tile(boolean isFree, int x, int y) {

        this.isFree = isFree;
        setWidth(BlocksApp.TILE_SIZE);
        setHeight(BlocksApp.TILE_SIZE);
        relocate(x * BlocksApp.TILE_SIZE, y * BlocksApp.TILE_SIZE);

        setFill(isFree ? Color.valueOf("rgba(255,255,255,0.3)") : Color.valueOf("#47797E"));
        //setOpacity(isFree ? 0.3 : 1);
        setStroke(Color.valueOf("#1E4D5C"));
        setArcWidth(20);
        setArcHeight(20);
    }
}
