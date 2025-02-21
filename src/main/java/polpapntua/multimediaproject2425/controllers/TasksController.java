package polpapntua.multimediaproject2425.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import polpapntua.multimediaproject2425.models.Task;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class TasksController {
    @FXML
    private TableView<Task> tasksTableView;

    @FXML
    private TableColumn<Task, String> tasksTitleColumn;

    @FXML
    private TableColumn<Task, String> tasksDescriptionColumn;

    @FXML
    private TableColumn<Task, String> tasksCategoryColumn;

    @FXML
    private TableColumn<Task, String> tasksPriorityColumn;

    @FXML
    private TableColumn<Task, String> tasksDueDateColumn;

    @FXML
    private TableColumn<Task, String> tasksStatusColumn;

    @FXML
    public void initialize() {
        tasksTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // added to remove the horizontal scrollbar

        tasksTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        tasksDescriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        tasksCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));

        tasksPriorityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPriority().getName()));
        // Custom comparator for Priority column, depending on the Priority lvl.
        tasksPriorityColumn.setComparator(Comparator.comparing(s -> {
            for (Task task : tasksTableView.getItems()) {
                if (task.getPriority().getName().equals(s)) {
                    return task.getPriority().getLevel();
                }
            }
            return Integer.MAX_VALUE; // Fallback value for safety
        }));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        tasksDueDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDueDate().format(formatter)));

        tasksStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
    }

    public void setTasks(ObservableList<Task> tasksList) {
        tasksTableView.setItems(tasksList);
    }
}
