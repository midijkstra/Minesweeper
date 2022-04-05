import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Main extends Application {
    @Override
    public void start(Stage stage) {

        Pane root = new Pane();
        root.setStyle("-fx-background-color: #b0b0b0;");
        Scene scene = new Scene(root, 730, 810);
        
        Menu menu = new Menu(root);
        root.getChildren().add(menu);

        stage.setScene(scene);
        stage.setTitle("Minesweeper v1.1.9");
        stage.setResizable(false);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("resources/icon.png")));
        stage.show();

        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
}