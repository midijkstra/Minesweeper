import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.event.ActionEvent;
import javafx.event.Event;

public class Menu extends Pane {

    CustomButton start;
    CustomButton easy;
    CustomButton medium;
    CustomButton hard;
    char difficulty;

    Menu(Pane parent) {
        
        start = new CustomButton("resources/start.png", 360, 120);
        easy = new CustomButton("resources/easy.png", 80, 30);
        medium = new CustomButton("resources/medium_pressed.png", 80, 30);
        hard = new CustomButton("resources/hard.png", 80, 30);
        difficulty = 'm';

        Text t = new Text("Choose the difficulty");
        t.setFont(Font.font("Verdana", FontWeight.LIGHT, 15));
        t.relocate(295, 330);

        start.relocate(190, 250);
        easy.relocate(230, 360);
        medium.relocate(325, 360);
        hard.relocate(420, 360);

        getChildren().addAll(start, easy, medium, hard, t);

        start.setOnMouseClicked(e -> {

            start.setPicture("resources/start_pressed.png");
            new Thread() {
                public void run() {
                    
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {}

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run () {
                            parent.getChildren().clear();
                            parent.getChildren().add(new Map(difficulty, parent));
                        }
                    });
                }
            }.start();
        });

        easy.setOnMouseClicked(e -> {
            easy.setPicture("resources/easy_pressed.png");
            medium.setPicture("resources/medium.png");
            hard.setPicture("resources/hard.png");
            difficulty = 'e';
        });

        medium.setOnMouseClicked(e -> {
            medium.setPicture("resources/medium_pressed.png");
            easy.setPicture("resources/easy.png");
            hard.setPicture("resources/hard.png");
            difficulty = 'm'; 
        });

        hard.setOnMouseClicked(e -> {
            hard.setPicture("resources/hard_pressed.png");
            medium.setPicture("resources/medium.png");
            easy.setPicture("resources/easy.png");
            difficulty = 'h';
        });
    }
}