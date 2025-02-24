package polpapntua.multimediaproject2425.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Task;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

import static polpapntua.multimediaproject2425.helpers.createButton;
import static polpapntua.multimediaproject2425.helpers.serializeObject;

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
    private TableColumn<Task, Void> tasksSaveColumn;

    @FXML
    public void initialize() {
        tasksTableView.setEditable(true);

        tasksSaveColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Task, Void> call(TableColumn<Task, Void> param) {
                return new TableCell<>() {
                    private final Button saveButton = new Button();

                    {
                        // Set the save icon
                        Image saveImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/save-icon.png")));
                        ImageView imageView = new ImageView(saveImage);
                        imageView.setFitWidth(20);
                        imageView.setFitHeight(20);
                        saveButton.setPrefSize(24, 24);
                        saveButton.setGraphic(imageView);

                        // Handle save click
                        saveButton.setOnAction(event -> {
                            Task task = getTableView().getItems().get(getIndex());
                            String taskPath = "src/main/resources/data/task_" + task.getId().toString();
                            serializeObject(taskPath, task);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(saveButton);
                        }
                    }
                };
            }
        });

        tasksTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        tasksTitleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tasksTitleColumn.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            task.setTitle(event.getNewValue());
        });

        tasksDescriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        tasksDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tasksDescriptionColumn.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            task.setDescription(event.getNewValue());
        });

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
        tasksDueDateColumn.setCellFactory(column -> new TableCell<Task, String>() {
            private final DatePicker datePicker = new DatePicker();
            {
                datePicker.setOnAction(event -> {
                    LocalDate selectedDate = datePicker.getValue();
                    if (selectedDate != null) {
                        commitEdit(selectedDate.format(formatter));
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        datePicker.setValue(LocalDate.parse(item, formatter));
                        setGraphic(datePicker);
                        setText(null);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                if (getItem() != null) {
                    datePicker.setValue(LocalDate.parse(getItem(), formatter));
                }
                setGraphic(datePicker);
                setText(null);
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem());
                setGraphic(null);
            }

            @Override
            public void commitEdit(String newDate) {
                super.commitEdit(newDate);
                Task task = getTableView().getItems().get(getIndex());
                task.setDueDate(LocalDate.parse(newDate, formatter));
                setText(newDate);
                setGraphic(null);
            }
        });

        tasksStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
    }

    public void setTasks(ObservableList<Task> tasksList) {
        tasksTableView.setItems(tasksList);
    }
}