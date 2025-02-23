package polpapntua.multimediaproject2425.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import polpapntua.multimediaproject2425.Main;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;
import polpapntua.multimediaproject2425.models.Task;
import polpapntua.multimediaproject2425.services.CategoriesService;
import polpapntua.multimediaproject2425.services.PrioritiesService;
import polpapntua.multimediaproject2425.services.TasksService;

import java.io.IOException;

public class MainController {
    protected static final Logger logger = LogManager.getLogger();

    @FXML
    private HBox navbar;

    @FXML
    private BorderPane contentPane;

    private final CategoriesService categoriesService = new CategoriesService("src/main/resources/data/categories.json");
    private final ObservableList<Category> categories = FXCollections.observableArrayList(categoriesService.getAllCategories());

    private final PrioritiesService prioritiesService = new PrioritiesService("src/main/resources/data/priorities.json");
    private final ObservableList<Priority> priorities = FXCollections.observableArrayList(prioritiesService.getAllPriorities());

    private final TasksService tasksService = new TasksService("src/main/resources/data/tasks", categories, priorities);
    private final ObservableList<Task> tasks = FXCollections.observableArrayList(tasksService.getAllTasks());


    public void displayTasks() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("tasksView.fxml"));  // 1. load the view
            Parent tasksView = loader.load();
            TasksController controller = loader.getController();   // 2. get its controller's instance
            controller.setTasks(tasks);   // 3. pass the data to the controller

            contentPane.setCenter(tasksView);
        } catch (IOException ex) {
            logger.error("Exception occurred while trying to display the tasks view: {}", ex.getMessage(), ex);
        }
    }

    public void displayCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("categoriesView.fxml"));  // 1. load the view
            Parent categoriesView = loader.load();
            CategoriesController controller = loader.getController();   // 2. get its controller's instance
            controller.setCategories(categories);   // 3. pass the data to the controller

            contentPane.setCenter(categoriesView);
        } catch (IOException ex) {
            logger.error("Exception occurred while trying to display the categories view: {}", ex.getMessage(), ex);
        }
    }

    public void displayPriorities() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("prioritiesView.fxml"));  // 1. load the view
            Parent prioritiesView = loader.load();
            PrioritiesController controller = loader.getController();   // 2. get its controller's instance
            controller.setPriorities(priorities);   // 3. pass the data to the controller

            contentPane.setCenter(prioritiesView);
        } catch (IOException ex) {
            logger.error("Exception occurred while trying to display the priorities view: {}", ex.getMessage(), ex);
        }
    }
}