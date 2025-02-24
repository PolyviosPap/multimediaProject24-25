package polpapntua.multimediaproject2425;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.IntegerStringConverter;
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
        button.setPrefSize(24, 24);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;");
        return button;
    }

    public static <T extends Serializable> void serializeObject(String filePath, T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty-print JSON
        try {
            objectMapper.writeValue(new File(filePath), object);
            System.out.println("File saved successfully: " + filePath);
        } catch (IOException ex) {
            logger.error("Exception occurred while serializing {}: {}", filePath, ex.getMessage(), ex);
        }
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

    public static class SafeIntegerStringConverter extends IntegerStringConverter {
        @Override
        public Integer fromString(String string) {
            try {
                return (string == null || string.trim().isEmpty()) ? null : Integer.parseInt(string);
            } catch (NumberFormatException e) {
                return null; // Return null if the input is invalid
            }
        }
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
