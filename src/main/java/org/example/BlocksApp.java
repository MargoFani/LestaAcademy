package org.example;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class BlocksApp extends Application {

    public static final int PREF_SIZE_WIDTH = 1000;
    public static final int PREF_SIZE_HEIGHT = 750;

    public static final int TILE_SIZE = 88;
    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;
    public static final int COUNT_OF_STONES = 15;

    public static final int RED_X = 0;
    public static final int GREEN_X = 2;
    public static final int BLUE_X = 4;

    public final Image BACK_IMG = new Image(this.getClass().getResourceAsStream("/background.jpg"));
    public final Image RESTART_IMG = new Image(this.getClass().getResourceAsStream("/restart.png"));

    private Tile[][] board = new Tile[WIDTH][HEIGHT];
    private Group tileGroup = new Group();
    private Group stoneGroup = new Group();

    private Group topColourGroup = new Group();
    private Group topTileGroup = new Group();

    private GridPane gridPane = new GridPane();
    private Pane boardPane = new Pane();

    private StoneType[] stoneTypes;

    private BooleanProperty isWin = new SimpleBooleanProperty(false);


    private Parent createContent() {


        Pane topPane = createTopPane();
        Pane bottomPane = createBottomPane();
        Pane boardPane = createBoardPane();

        gridPane.setPrefSize(PREF_SIZE_WIDTH, PREF_SIZE_HEIGHT);
        gridPane.setAlignment(Pos.CENTER);


        ColumnConstraints column1 = new ColumnConstraints();
        column1.setMinWidth(TILE_SIZE * 2.5);
        gridPane.getColumnConstraints().add(column1);

        Text winText = new Text("you win!!!");

        winText.setVisible(false);
        Font font = new Font("Irish Grover", 28);
        winText.setFont(font);
        winText.setFill(Color.valueOf("#F6E9BD"));

        GridPane.setHalignment(winText, HPos.CENTER);

        //gridPane.setGridLinesVisible(true);
        gridPane.add(topPane, 1, 0);
        gridPane.add(boardPane, 1, 1);
        gridPane.add(bottomPane, 3, 2);
        gridPane.add(winText, 1, 2);

        isWin.addListener((observable, oldValue, newValue) -> {
            // Only if completed
            if (newValue) {
                winText.setVisible(true);
                boardPane.setDisable(true);
            } else winText.setVisible(false);
        });

        return gridPane;
    }


    private Pane createTopPane() {
        Pane topPane = new Pane();
        topPane.setDisable(true);
        for (int i = 0; i < WIDTH; i = i + 2) {
            Rectangle rg = new Rectangle(TILE_SIZE, TILE_SIZE);

            rg.setFill(Color.valueOf("rgba(255,255,255,0.4)"));
            rg.setStroke(Color.valueOf("#1E4D5C"));
            rg.setArcWidth(20);
            rg.setArcHeight(20);

            rg.relocate(i * BlocksApp.TILE_SIZE, 0);
            topTileGroup.getChildren().add(rg);
        }


        Stone redStone = new Stone(StoneType.RED, 0, 0);
        topColourGroup.getChildren().add(redStone);
        Stone greenStone = new Stone(StoneType.GREEN, 2, 0);
        topColourGroup.getChildren().add(greenStone);
        Stone blueStone = new Stone(StoneType.BLUE, 4, 0);
        topColourGroup.getChildren().add(blueStone);


        topPane.getChildren().addAll(topTileGroup, topColourGroup);
        topPane.setPadding(new Insets(0, 0, 30, 0));
        return topPane;
    }

    private Pane createBoardPane() {


        boardPane.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        boardPane.getChildren().addAll(tileGroup, stoneGroup);

        //int BLOCKS_COORDINATES[] = {1, 3, 11, 13, 21, 23};
        //create field
        int tileIndex = 0;
        int stoneIndex = 0;
        stoneTypes = setStoneTypes();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                boolean val = true;
                if (tileIndex % 10 == 1 | tileIndex % 10 == 3) {
                    val = false;
                }
                Tile tile = new Tile(val, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);
                //добавить проверку на выход из границ массива
                if (x % 2 == 0) {
                    Stone stone = makeStone(stoneTypes[stoneIndex], x, y);
                    board[x][y].setStone(stone);
                    stoneGroup.getChildren().add(stone);
                    stoneIndex++;

                }
                tileIndex++;

            }
        }
        return boardPane;
    }

    private Pane createBottomPane() {
        Pane bottomPane = new Pane();
//        Button button = new Button();
//        button.setPrefSize(TILE_SIZE,TILE_SIZE);
//        button.setGraphic(new ImageView(RESTART_IMG));
        Rectangle button = new Rectangle(TILE_SIZE, TILE_SIZE);
        button.setFill(new ImagePattern(RESTART_IMG));
        button.relocate(BlocksApp.TILE_SIZE * 1.5, 0);
        bottomPane.getChildren().add(button);
        button.setOnMousePressed(e -> {
            restart();
        });

        return bottomPane;
    }

    private void restart() {
        int stoneIndex = 0;
        mix(stoneTypes);
        stoneGroup.getChildren().clear();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                board[x][y].setStone(null);
                if (x % 2 == 0) {
                    Stone stone = makeStone(stoneTypes[stoneIndex], x, y);
                    board[x][y].setStone(stone);
                    stoneGroup.getChildren().add(stone);
                    stoneIndex++;
                }
            }
        }
        boardPane.setDisable(false);
        isWin.setValue(false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("BlocksApp");
        Scene scene = new Scene(createContent());
        scene.setFill(new ImagePattern(BACK_IMG));
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private StoneType[] setStoneTypes() {
        StoneType[] types = new StoneType[COUNT_OF_STONES];
        for (int i = 0; i < 5; i++) {
            types[i] = StoneType.RED;
        }
        for (int i = 5; i < 10; i++) {
            types[i] = StoneType.GREEN;
        }
        for (int i = 10; i < 15; i++) {
            types[i] = StoneType.BLUE;
        }
        //перемешка типов
        mix(types);
        return types;
    }

    private void mix(StoneType[] a) {
        Random rnd = new Random();
        for (int i = 1; i < a.length; i++) {
            int j = rnd.nextInt(i);
            StoneType temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
    }

    private boolean tryMove(Stone stone, int newX, int newY) {

        if (newX < 0 || newX > 4 || newY < 0 || newY > 4) {
            return false;
        }

        if (board[newX][newY].hasStone() || !board[newX][newY].isFree()) {
            return false;
        }

        int x0 = toBoard(stone.getOldX());
        int y0 = toBoard(stone.getOldY());

        if (x0 < WIDTH && y0 < HEIGHT) {
            if (Math.abs(newX - x0) == 1 && Math.abs(newY - y0) == 0) {
                return true;
            }
            if (Math.abs(newX - x0) == 0 && Math.abs(newY - y0) == 1) {
                return true;
            }
        }
        return false;
    }

    private int toBoard(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    private Stone makeStone(StoneType type, int x, int y) {
        Stone stone = new Stone(type, x, y);

        stone.setOnMouseReleased(e -> {
            int newX = toBoard(stone.getLayoutX());
            int newY = toBoard(stone.getLayoutY());

            boolean result = tryMove(stone, newX, newY);

            int x0 = toBoard(stone.getOldX());
            int y0 = toBoard(stone.getOldY());


            if (result) {
                stone.move(newX, newY);
                board[x0][y0].setStone(null);
                board[newX][newY].setStone(stone);

                //win?
                isWin();

            } else {
                stone.abortMove();
            }

        });

        return stone;
    }

    private void isWin() {
        int red = 0;
        int green = 0;
        int blue = 0;


        for (int i = 0; i < WIDTH; i++) {
            if (board[RED_X][i].hasStone() && board[RED_X][i].getStone().getType() == StoneType.RED) {
                red++;
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            if (board[GREEN_X][i].hasStone() && board[GREEN_X][i].getStone().getType() == StoneType.GREEN) {
                green++;
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            if (board[BLUE_X][i].hasStone() && board[BLUE_X][i].getStone().getType() == StoneType.BLUE) {
                blue++;
            }
        }

        if (red == WIDTH && green == WIDTH && blue == WIDTH) {
            this.isWin.setValue(true);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
