package org.example;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import static org.example.BlocksApp.TILE_SIZE;

public class Stone extends StackPane {
    private StoneType type;

    //private final int moveDir;
    Image IMG_RED = new Image(this.getClass().getResourceAsStream("/red.png"));
    Image IMG_GREEN = new Image(this.getClass().getResourceAsStream("/green.png"));
    Image IMG_BLUE = new Image(this.getClass().getResourceAsStream("/blue.png"));

    private double mouseX, mouseY;
    private double oldX, oldY;

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public StoneType getType() {
        return type;
    }

    public Stone(StoneType type, int x, int y) {
        this.type = type;
        move(x, y);

        Rectangle bg = new Rectangle(TILE_SIZE, TILE_SIZE);
        if (this.type == StoneType.RED) {
            bg.setFill(new ImagePattern(IMG_RED));
        } else if (this.type == StoneType.GREEN) {
            bg.setFill(new ImagePattern(IMG_GREEN));
        } else if (this.type == StoneType.BLUE) {
            bg.setFill(new ImagePattern(IMG_BLUE));
        }

        getChildren().addAll(bg);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void move(int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

    public void abortMove(){
        relocate(oldX, oldY);
    }
}
