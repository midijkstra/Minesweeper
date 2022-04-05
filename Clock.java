import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.concurrent.Task;
import javafx.application.Platform;

public class Clock extends Pane {

    Text text;
    boolean running;
    int time;

    Clock(double x, double y, double width, double height, Font font) {

        Rectangle rectangle = new Rectangle(0, 0, width, height);
        rectangle.setFill(Color.BLACK);

        Text text_back = new Text("8888");
        text_back.setFill(Color.rgb(100, 0, 0));
        text_back.setFont(font);
        text_back.relocate(5, 9);

        text = new Text("0000");
        text.setFill(Color.RED);
        text.setFont(font);
        text.relocate(5, 9);

        relocate(x, y);
        getChildren().addAll(rectangle, text_back, text);
    }

    void start() {
        
        time = 0;
        running = true; 

        new Thread() {
            @Override
            public void run() {
                
                while (running) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {}
                
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(String.format("%04d", time));
                        }
                    });
                    time++;
                }
            }        
        }.start();
    }

    void stop() {
        running = false;
    }
}