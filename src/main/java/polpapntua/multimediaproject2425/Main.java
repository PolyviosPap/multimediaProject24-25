package polpapntua.multimediaproject2425;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import polpapntua.multimediaproject2425.controllers.MainController;

public class Main extends Application {
    protected static final Logger logger = LogManager.getLogger();

    @Override
    public void start(Stage primaryStage) throws IOException {
        logger.info("Task Manager launched!");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainView.fxml"));
        VBox root = fxmlLoader.load();

        Scene scene = new Scene(root, 1000, 500);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());
        primaryStage.setTitle("MediaLab Assistant");
        primaryStage.setScene(scene);
        primaryStage.show();

        MainController mainController = fxmlLoader.getController();
        mainController.displayTasks();

        // Right before the closing of the main window, save all edited files.
        primaryStage.setOnCloseRequest(event -> {
            mainController.saveFiles();
            try {
                mainController.cleanDeletedTaskFiles();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
