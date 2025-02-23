package polpapntua.multimediaproject2425;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.List;
import java.util.Objects;

public class helpers {
    protected static final Logger logger = LogManager.getLogger();

    public static Button createButton(String iconPath) {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(helpers.class.getResourceAsStream(iconPath))));
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        Button button = new Button();
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;");
        return button;
    }

    public static <T extends Serializable> void serializeObjects(String filePath, List<T> objects) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty-print JSON
        try {
            objectMapper.writeValue(new File(filePath), objects);
            System.out.println("File saved successfully: " + filePath);
        } catch (IOException ex) {
            logger.error("Exception occurred while serializing {}: {}", filePath, ex.getMessage(), ex);
        }
    }
}
