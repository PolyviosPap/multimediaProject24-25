package polpapntua.multimediaproject2425;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import polpapntua.multimediaproject2425.controllers.MainController;

public class Main extends Application {
    protected static final Logger logger = LogManager.getLogger();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void start(Stage primaryStage) throws IOException {
        logger.info("Task Manager launched!");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainView.fxml"));
        VBox root = fxmlLoader.load();

        Scene scene = new Scene(root, 1000, 500);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());
        primaryStage.setTitle("Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();

        MainController mainController = fxmlLoader.getController();

        // Schedule saving of edited files (if any) every 5 minutes.
        scheduler.scheduleAtFixedRate(mainController::checkForFilesSave, 5, 5, TimeUnit.MINUTES);

        // Right before the closing of the main window, save all edited files.
        primaryStage.setOnCloseRequest(event -> {
            mainController.checkForFilesSave();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
