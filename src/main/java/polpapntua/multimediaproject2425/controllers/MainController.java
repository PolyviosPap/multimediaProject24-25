package polpapntua.multimediaproject2425.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import polpapntua.multimediaproject2425.Main;
import polpapntua.multimediaproject2425.enums.TaskStatus;
import polpapntua.multimediaproject2425.helpers;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;
import polpapntua.multimediaproject2425.models.Task;
import polpapntua.multimediaproject2425.services.CategoriesService;
import polpapntua.multimediaproject2425.services.PrioritiesService;
import polpapntua.multimediaproject2425.services.TasksService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static polpapntua.multimediaproject2425.helpers.serializeObject;

public class MainController {
    protected static final Logger logger = LogManager.getLogger();

    private boolean firstRun = false;

    @FXML
    private BorderPane contentPane;

    private static final CategoriesService categoriesService = new CategoriesService("src/main/resources/medialab/categories.json");
    private static final ObservableList<Category> categories = FXCollections.observableArrayList(categoriesService.getAllCategories());

    private static final PrioritiesService prioritiesService = new PrioritiesService("src/main/resources/medialab/priorities.json");
    private static final ObservableList<Priority> priorities = FXCollections.observableArrayList(prioritiesService.getAllPriorities());

    private static final TasksService tasksService = new TasksService("src/main/resources/medialab/tasks", categories, priorities);
    private static final ObservableList<Task> tasks = FXCollections.observableArrayList(tasksService.getAllTasks());

    @FXML
    private TextField allTasks;
    private static final StringProperty allTasksText = new SimpleStringProperty();

    @FXML
    private TextField completedTasks;
    private static final StringProperty completedTasksText = new SimpleStringProperty();

    @FXML
    private TextField delayedTasks;
    private static final StringProperty delayedTasksText = new SimpleStringProperty();


    @FXML
    private TextField soonDueDateTasks;
    private static final StringProperty soonDueDateTasksText = new SimpleStringProperty();

    @FXML
    public void initialize() {
        allTasks.textProperty().bind(allTasksText);
        completedTasks.textProperty().bind(completedTasksText);
        delayedTasks.textProperty().bind(delayedTasksText);
        soonDueDateTasks.textProperty().bind(soonDueDateTasksText);

        updateCounters();
        tasks.addListener((ListChangeListener<Task>) change -> updateCounters());
    }

    public static void updateCounters() {
        allTasksText.set(tasks.size() + " tasks total");

        long completedTasksCount = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .count();
        completedTasksText.set(completedTasksCount + " completed tasks");

        long delayedTasksCount = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.DELAYED)
                .count();
        delayedTasksText.set(delayedTasksCount + " delayed tasks");

        LocalDate dayAfterSevenDays = LocalDate.now().plusDays(7);
        long soonDueDateTasksCount = tasks.stream()
                .filter(task -> (task.getStatus() != TaskStatus.DELAYED && dayAfterSevenDays.isAfter(task.getDueDate())))
                .count();
        soonDueDateTasksText.set(soonDueDateTasksCount + " tasks are due to less than 7 days");
    }

    public void displayTasks() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("tasksView.fxml"));  // 1. load the view
            Parent tasksView = loader.load();
            TasksController controller = loader.getController();   // 2. get its controller's instance
            controller.setNeededObjects(tasks, categories, priorities, firstRun); // 3. pass the data to the controller

            firstRun = true;

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
            controller.setNeededObjects(categories, tasks);   // 3. pass the data to the controller

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

    public void saveFiles() {
        helpers.serializeObjects("src/main/resources/medialab/categories.json", categories);

        helpers.serializeObjects("src/main/resources/medialab/priorities.json", priorities);

        for (Task task : tasks) {
            String taskPath = "src/main/resources/medialab/tasks/task_" + task.getId().toString() + ".json";
            serializeObject(taskPath, task);
        }
    }

    public void cleanDeletedTaskFiles() throws IOException {
        Path dir = Paths.get("src/main/resources/medialab/tasks/");

        // Create a set of valid task IDs
        Set<String> validIds = tasks.stream()
                .map(task -> String.valueOf(task.getId())) // Assuming getId() returns an int/long
                .collect(Collectors.toSet());

        // Define regex pattern for extracting IDs from file names
        Pattern pattern = Pattern.compile("task_(\\d+)\\.json");

        try (var stream = Files.list(dir)) {
            stream.filter(Files::isRegularFile) // Only consider files
                .filter(path -> path.getFileName().toString().matches("task_\\d+\\.json")) // Match file format
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        Matcher matcher = pattern.matcher(fileName);
                        if (matcher.matches()) {
                            String fileId = matcher.group(1);
                            if (!validIds.contains(fileId)) {
                                try {
                                    Files.delete(path);
                                    logger.info("Deleted: {}", fileName);
                                } catch (IOException e) {
                                    logger.error("Failed to delete: {}", fileName);
                                }
                            }
                        }
                    });
        }
    }
}