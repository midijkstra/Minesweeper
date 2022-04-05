import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import java.net.URL;

public class Map extends Pane { 

    String[] paths = {
        "resources/empty_cell.png",
        "resources/one.png",
        "resources/two.png",
        "resources/three.png",
        "resources/four.png",
        "resources/five.png",
        "resources/six.png",
        "resources/seven.png",
        "resources/eight.png",
        "resources/bomb-2.png"
    };

    int width;      
    int height;     
    int cellSize;   
    int mines;
    int flagsLeft;
    int numberOfOpen;
    boolean clockRunning;

    int[][] grid;
    boolean[][] flags;
    boolean[][] open;
    ImageView[][] map;
    
    Pane info;
    Pane mapFrame;
    CustomButton back;
    CustomButton restart;
    Clock clock;
    Text t1;

    String soundPath_1 = getClass().getClassLoader().getResource("resources/button_sound.mp3").toString();
    AudioClip click = new AudioClip(soundPath_1);
 
    Map(char difficulty, Pane parent) {

        switch (difficulty) {
            
            case 'e': 
                cellSize = 60;
                mines = 15;
            break;
            case 'm': 
                cellSize = 40;
                mines = 50;
            break;
            case 'h': 
                cellSize = 30;
                mines = 120;
            break;
        }
         
        width = 720 / cellSize;
        height = 720 / cellSize;
        clockRunning = false;
        flagsLeft = mines; 
        numberOfOpen = 0;
        
        grid = new int[height][width];
        flags = new boolean[height][width];
        open = new boolean[height][width];
        map = new ImageView[height][width];
        
        mapFrame = new Pane();
        mapFrame.setPrefSize(720, 360);
        mapFrame.relocate(10, 90);

        draw();
        
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                
                int x = (int)e.getX();
                int y = (int)e.getY();
                int row = y / cellSize;
                int col = x / cellSize;
                
                if (!clockRunning) {
                    clockRunning = true;
                    clock.start();
                }

                if (e.isPrimaryButtonDown() && !flags[row][col] && !open[row][col]) {                    
                    try {
                        int index = grid[row][col];
                        Image img = new Image(paths[index], cellSize, cellSize, true, true);
                        map[row][col].setImage(img);

                        if (grid[row][col] == 0) {
                            openEmpty(row, col);
                        }
                        
                        else if (grid[row][col] == 9) {
                            map[row][col].setImage(new Image("resources/explosion.png", 
                                                    cellSize, cellSize, true, true));
                            mapFrame.removeEventHandler(MouseEvent.MOUSE_PRESSED, this);
                            clock.stop();
                            gameOver(row, col);
                        }
                        
                        if (!open[row][col]) {
                            open[row][col] = true;
                            numberOfOpen++;
                        }
                        click.play();
                    }
                    catch(ArrayIndexOutOfBoundsException ex) {}
                }
                else if (e.isSecondaryButtonDown() && !open[row][col]) {
                    if (!flags[row][col] && flagsLeft > 0) {        
                        Image img = new Image("resources/flag.png", cellSize, cellSize, true, true);
                        map[row][col].setImage(img);
                        flags[row][col] = true;
                        flagsLeft--;
                        t1.setText(String.format("%04d", flagsLeft));
                    }
                    else if (flags[row][col]) {
                        Image img = new Image("resources/cell.png", cellSize, cellSize, true, true);
                        map[row][col].setImage(img);
                        flags[row][col] = false;
                        flagsLeft++;
                        t1.setText(String.format("%04d", flagsLeft));
                    }
                    click.play();
                }

                if (numberOfOpen == width * height - mines && flagsLeft == 0) {
                    mapFrame.removeEventHandler(MouseEvent.MOUSE_PRESSED, this);
                    clock.stop();
                }
            }
        };
        mapFrame.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler);

        back.setOnMouseClicked(e -> {
            
            back.setPicture("resources/back_pressed.png");
            new Thread() {
                public void run() {
                    
                    try {
                        Thread.sleep(200);
                    } catch (Exception ex) {}

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run () {
                            back.setPicture("resources/back_pressed.png");
                            terminateSounds();
                            parent.getChildren().clear();
                            parent.getChildren().add(new Menu(parent));
                        }
                    });
                }
            }.start();
        });

        restart.setOnMouseClicked(e -> {
            
            restart.setPicture("resources/restart_pressed.png");
            new Thread() {
                public void run() {
                    
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {}

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run () {
                            restart.setPicture("resources/restart.png");
                            terminateSounds();
                            parent.getChildren().clear();
                            parent.getChildren().add(new Map(difficulty, parent));
                        }
                    });
                }
            }.start();
        });
    }

    void draw() {

        restart = new CustomButton("resources/restart.png", 75, 38);
        back = new CustomButton("resources/back.png", 75, 38);
        back.relocate(279, 21);
        restart.relocate(367, 21);
        
        String font_path = getClass().getClassLoader().getResource("resources/digital_font.ttf").toString();
        Font font = Font.loadFont(font_path, 45);
        Rectangle r1 = new Rectangle(26, 10, 100, 60);
        
        clock = new Clock(594, 10, 100, 60, font);
        Text t1_back = new Text("8888");
        
        t1_back.setFill(Color.rgb(100, 0, 0));
        t1_back.setFont(font);
        t1_back.relocate(31, 18);

        t1 = new Text(String.format("%04d", flagsLeft));
        t1.setFill(Color.RED);
        t1.setFont(font);
        t1.relocate(31, 18);

        info = new Pane(new ImageView(new Image(
                        "resources/bar.png", 720, 80, true, true)));
        info.setPrefSize(720, 80);
        info.relocate(10, 0);
        info.getChildren().addAll(restart, back, r1, t1_back, t1, clock);

        getChildren().add(info);
        generateMines();


        double x = 0;
        double y = 0;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                int index = grid[row][col];
                Image img = new Image("resources/cell.png", cellSize, cellSize, true, true);
                map[row][col] = new ImageView(img);
                map[row][col].setX(x);
                map[row][col].setY(y);

                Rectangle rectangle = new Rectangle(x, y, cellSize, cellSize);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);

                mapFrame.getChildren().addAll(rectangle, map[row][col]);
                x += cellSize;
            }
            x = 0;
            y += cellSize;
        }
        getChildren().add(mapFrame);
    }

    void generateMines() {

        int minesLeft = mines;
                
        while (minesLeft > 0) {

            int row = (int)(Math.random() * height);
            int col = (int)(Math.random() * width);

            if (grid[row][col] != 9) {
                grid[row][col] = 9;
                minesLeft--;
            }
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                if (grid[row][col] != 9) {
                    continue;
                }

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (row + i >= 0 && row + i < height && col + j >= 0 && col + j < width) {
                            grid[row + i][col + j] += (grid[row + i][col + j] != 9) ? 1 : 0;
                        }
                    }
                }
            }
        }
    }

    void openEmpty(int row, int col) {

        if (grid[row][col] == 9 || open[row][col] || flags[row][col]) {
            return;
        }
        open[row][col] = true;
        numberOfOpen++;

        if (grid[row][col] != 0) { 
            int index = grid[row][col];
            Image img = new Image(paths[index], cellSize, cellSize, true, true);
            map[row][col].setImage(img);
            return;
        }

        else {
            Image img = new Image(paths[0], cellSize, cellSize, true, true);
            map[row][col].setImage(img);   
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (row + i >= 0 && row + i < height && col + j >= 0 && col + j < width) {
                    openEmpty(row + i, col + j);
                }
            }
        }
    }

    void gameOver(int r, int c) {

        open[r][c] = true;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (!open[row][col]) {
                    int index = grid[row][col];
                    Image img = new Image(paths[index], cellSize, cellSize, true, true);
                    map[row][col].setImage(img);
                }
            }
        }
    }

    void terminateSounds() {

        click.stop();
    }
}