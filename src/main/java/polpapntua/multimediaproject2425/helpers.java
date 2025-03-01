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

/** A utility class providing helper methods for general use.*/
public class helpers {
    /** Logger instance for recording events and errors. */
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Creates a button of a specified icon.
     *
     * @param iconPath the path to the desired icon image
     * @return a button with the specified icon
     * @throws NullPointerException if the icon resource is not found
     */
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

    /**
     * Serializes an object to a JSON file.
     *
     * @param filePath the file path where the object will be saved
     * @param object   the object to be serialized
     * @param <T>      the type of the object (must be serializable)
     */
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

    /**
     * Serializes a list of objects to a JSON file.
     *
     * @param filePath the file path where the objects will be saved
     * @param objects  the list of objects to be serialized
     * @param <T>      the type of objects in the list (must be serializable)
     */
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

    /**
     * A custom converter that safely converts strings to integers.
     * If the input string is null, empty, or not a valid integer, it returns null.
     */
    public static class SafeIntegerStringConverter extends IntegerStringConverter {
        /**
         * Converts a string to an integer.
         *
         * @param string the string to convert
         * @return the integer value, or {@code null} if the string is empty or invalid
         */
        @Override
        public Integer fromString(String string) {
            try {
                return (string == null || string.trim().isEmpty()) ? null : Integer.parseInt(string);
            } catch (NumberFormatException e) {
                return null; // Return null if the input is invalid
            }
        }
    }

    /**
     * Displays an alert dialog with a warning message.
     *
     * @param title   the title of the alert dialog
     * @param message the message to be displayed
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
