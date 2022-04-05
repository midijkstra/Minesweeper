import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomButton extends StackPane {
    
    ImageView picture;
    int width;
    int height;

    CustomButton(String path, int w, int h) {
        
        width = w;
        height = h;
        
        setWidth(width);
        setHeight(height);
        
        picture = new ImageView(new Image(path, width, height, true, true));
        getChildren().add(picture);
    }

    void setPicture(String path) {
        picture = new ImageView(new Image(path, width, height, true, true));
        getChildren().clear();
        getChildren().add(picture);
    }
}