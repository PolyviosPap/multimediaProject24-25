package polpapntua.multimediaproject2425.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;
import polpapntua.multimediaproject2425.models.Task;

import java.time.LocalDate;

public class TasksController {
    @FXML
    private TableView<Task> tasksTableView;

    //@FXML
    //private TableColumn<Task, Long> tasksIdColumn;

    @FXML
    private TableColumn<Task, String> tasksTitleColumn;

    @FXML
    private TableColumn<Task, String> tasksDescriptionColumn;

    @FXML
    private TableColumn<Task, String> tasksCategoryColumn;

    @FXML
    private TableColumn<Priority, String> tasksPriorityColumn;

    @FXML
    private TableColumn<Task, LocalDate> tasksDueDateColumn;

    @FXML
    private TableColumn<Task, String> tasksStatusColumn;

    @FXML
    public void initialize() {
        tasksTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // added to remove the horizontal scrollbar

        //categoriesIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));

        tasksTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        tasksDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tasksCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("CategoryName"));
        tasksPriorityColumn.setCellValueFactory(new PropertyValueFactory<>("PriorityName"));
        tasksDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("DueDate"));
        tasksStatusColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));
    }

    // Method to receive categories from MainController
    public void setTasks(ObservableList<Task> tasksList) {
        tasksTableView.setItems(tasksList);
    }
}
