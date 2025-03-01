package polpapntua.multimediaproject2425.controllers;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import polpapntua.multimediaproject2425.enums.TaskStatus;
import polpapntua.multimediaproject2425.helpers;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;
import polpapntua.multimediaproject2425.models.Task;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static polpapntua.multimediaproject2425.enums.TaskStatus.*;
import static polpapntua.multimediaproject2425.helpers.createButton;
import static polpapntua.multimediaproject2425.helpers.showAlert;

public class TasksController {
    private ObservableList<Task> tasks;

    @FXML
    private TableView<Task> tasksTableView;

    @FXML
    private TableColumn<Task, String> tasksTitleColumn;

    @FXML
    private TableColumn<Task, String> tasksDescriptionColumn;

    @FXML
    private TableColumn<Task, Category> tasksCategoryColumn;

    @FXML
    private TableColumn<Task, Priority> tasksPriorityColumn;

    @FXML
    private TableColumn<Task, String> tasksDueDateColumn;

    @FXML
    private TableColumn<Task, String> tasksStatusColumn;

    @FXML
    private TableColumn<Task, Void> actionsColumn;

    @FXML
    private AnchorPane addTaskPane;

    @FXML
    private HBox addTaskFirstRow;

    @FXML
    private TextField addNewTaskName;

    @FXML
    private TextField addNewTaskDescription;

    @FXML
    private HBox addTaskSecondRow;

    @FXML
    private ComboBox<Category> addNewTaskCategory;

    @FXML
    private ComboBox<Priority> addNewTaskPriority;

    @FXML
    private DatePicker addNewTaskDueDate;

    private BigInteger maxPriorityId = BigInteger.ZERO;

    @FXML
    public void initialize() {
        tasksTableView.setEditable(true);

        // Button for adding new category (it shows the corresponding modal)
        Button addNewTaskButton = helpers.createButton("/icons/add-icon.png");
        addNewTaskButton.setOnAction(event -> addTaskPane.setVisible(true));
        actionsColumn.setGraphic(addNewTaskButton);     // Place it in the column header.

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

        tasksCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        tasksPriorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        // Custom comparator for Priority column, depending on the Priority lvl.
        tasksPriorityColumn.setComparator(Comparator.comparing(s -> {
            for (Task task : tasksTableView.getItems()) {
                if (task.getPriority().equals(s)) {
                    return task.getPriority().getLevel();
                }
            }
            return Integer.MAX_VALUE; // Fallback value for safety
        }));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        tasksDueDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDueDate().format(formatter)));
        tasksDueDateColumn.setCellFactory(column -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                // Restrict past dates
                datePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (date.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #d3d3d3;"); // Gray out past dates
                        }
                    }
                });

                datePicker.setOnAction(event -> {
                    LocalDate selectedDate = datePicker.getValue();
                    if (selectedDate != null && !selectedDate.isBefore(LocalDate.now())) {
                        commitEdit(selectedDate.format(formatter));
                    } else {
                        datePicker.setValue(null); // Reset invalid selection
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
                LocalDate newLocalDate = LocalDate.parse(newDate, formatter);
                if (newLocalDate.isBefore(LocalDate.now())) {
                    cancelEdit(); // Reject invalid past dates
                    return;
                }
                super.commitEdit(newDate);
                Task task = getTableView().getItems().get(getIndex());
                task.setDueDate(newLocalDate);
                setText(newDate);
                checkForDelayedTasks();
                MainController.updateCounters();
                setGraphic(null);
            }
        });

        tasksStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
        tasksStatusColumn.setCellFactory(column -> new TableCell<>() {
            private final ComboBox<TaskStatus> comboBox = new ComboBox<>();
            {
                // Exclude the DELAYED status, the app sets it.
                List<TaskStatus> filteredStatuses = Arrays.stream(TaskStatus.values())
                        .filter(status -> status != DELAYED)
                        .toList();

                comboBox.getItems().addAll(filteredStatuses); // Add only the allowed statuses

                comboBox.setOnAction(event -> {
                    if (comboBox.getValue() != null) {
                        commitEdit(comboBox.getValue().toString());
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
                        // Show ComboBox in edit mode
                        comboBox.setValue(TaskStatus.valueOf(getItem()));
                        setGraphic(comboBox);
                        setText(null);
                    } else {
                        // Show the current status
                        setText(item);
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    String itemName = getItem().toUpperCase();

                    if (itemName.equals("DELAYED")) {
                        cancelEdit(); // Prevent editing for DELAYED tasks
                        return;
                    }

                    super.startEdit();

                    comboBox.setValue(TaskStatus.valueOf(itemName));
                    setGraphic(comboBox);
                    setText(null);
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setGraphic(null);
                setText(getItem()); // Display the current item
            }

            @Override
            public void commitEdit(String newValue) {
                if (newValue != null) {
                    String enumName = newValue.toUpperCase(); // Convert to uppercase
                    getTableView().getItems().get(getIndex()).setStatus(TaskStatus.valueOf(enumName)); // Update the model
                    MainController.updateCounters();
                    super.commitEdit(newValue);
                }
            }
        });

        addTaskFirstRow.widthProperty().addListener((obs, oldVal, newVal) -> {
            double totalWidth = newVal.doubleValue();
            addNewTaskName.setPrefWidth(totalWidth * 0.4);
            addNewTaskDescription.setPrefWidth(totalWidth * 0.6);
        });

        addTaskSecondRow.widthProperty().addListener((obs, oldVal, newVal) -> {
            double totalWidth = newVal.doubleValue();
            addNewTaskCategory.setPrefWidth(totalWidth * 0.3);
            addNewTaskPriority.setPrefWidth(totalWidth * 0.3);
            addNewTaskDueDate.setPrefWidth(totalWidth * 0.4);
        });

        actionsColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button deleteButton = createButton("/icons/bin-icon.png");
            {
                deleteButton.setOnAction(event -> {
                    Task selectedTask = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(selectedTask);
                    tasks.remove(selectedTask);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        DoubleBinding remainingWidth = tasksTableView.widthProperty()
                .subtract(actionsColumn.widthProperty());

        tasksTitleColumn.prefWidthProperty().bind(remainingWidth.multiply(0.2));
        tasksDescriptionColumn.prefWidthProperty().bind(remainingWidth.multiply(0.35));
        tasksCategoryColumn.prefWidthProperty().bind(remainingWidth.multiply(0.15));
        tasksPriorityColumn.prefWidthProperty().bind(remainingWidth.multiply(0.09));
        tasksDueDateColumn.prefWidthProperty().bind(remainingWidth.multiply(0.1));
        tasksStatusColumn.prefWidthProperty().bind(remainingWidth.multiply(0.1));
    }

    public void setNeededObjects(ObservableList<Task> tasks, ObservableList<Category> categories, ObservableList<Priority> priorities, boolean firstRun) {
        this.tasks = tasks;

        if (!firstRun) checkForDelayedTasks();

        tasksTableView.setItems(tasks);

        maxPriorityId = this.tasks.stream()
                .map(Task::getId)
                .max(Comparator.naturalOrder())
                .orElse(BigInteger.ZERO);

        // String converter for category objects
        StringConverter<Category> categoryStringConverter = new StringConverter<>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getName() : "";
            }

            @Override
            public Category fromString(String string) {
                return categories.stream()
                        .filter(p -> p.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        };

        ObservableList<Category> categoryOptions = FXCollections.observableArrayList(categories);

        tasksCategoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(categoryStringConverter, categoryOptions));
        tasksCategoryColumn.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            task.setCategory(event.getNewValue());
        });

        addNewTaskCategory.setItems(categoryOptions);
        addNewTaskCategory.setConverter(categoryStringConverter);

        // String converter for priority objects
        StringConverter<Priority> priorityStringConverter = new StringConverter<>() {
            @Override
            public String toString(Priority priority) {
                return priority != null ? priority.getName() : "";
            }

            @Override
            public Priority fromString(String string) {
                return priorities.stream()
                        .filter(p -> p.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        };

        ObservableList<Priority> priorityOptions = FXCollections.observableArrayList(priorities);

        tasksPriorityColumn.setCellFactory(ComboBoxTableCell.forTableColumn(priorityStringConverter, priorityOptions));
        tasksPriorityColumn.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            task.setPriority(event.getNewValue());
        });

        addNewTaskPriority.setItems(priorityOptions);
        addNewTaskPriority.setConverter(priorityStringConverter);

        addNewTaskDueDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;"); // Gray out past dates
                }
            }
        });
    }

    @FXML
    private void onCancel() {
        addNewTaskName.clear();
        addNewTaskDescription.clear();
        addNewTaskCategory.getSelectionModel().clearSelection();
        addNewTaskPriority.getSelectionModel().clearSelection();
        addNewTaskDueDate.setValue(null);

        addTaskPane.setVisible(false);
    }

    @FXML
    private void onTaskSave() {
        if (addNewTaskName.getText().isEmpty() || addNewTaskDescription.getText().isEmpty() || addNewTaskCategory.getSelectionModel().isEmpty() || addNewTaskPriority.getSelectionModel().isEmpty() || addNewTaskDueDate.getValue() == null) {
            helpers.showAlert("", "You need to fill all the fields!");
            return;
        }

        maxPriorityId = maxPriorityId.add(BigInteger.ONE);
        Task newTask = new Task(
                maxPriorityId,
                addNewTaskName.getText(),
                addNewTaskDescription.getText(),
                addNewTaskCategory.getSelectionModel().getSelectedItem().getId(),
                addNewTaskCategory.getSelectionModel().getSelectedItem(),
                addNewTaskPriority.getSelectionModel().getSelectedItem().getId(),
                addNewTaskPriority.getSelectionModel().getSelectedItem(),
                addNewTaskDueDate.getValue(),
                OPEN
        );

        tasks.add(newTask);

        onCancel();
    }

    private void checkForDelayedTasks() {
        LocalDate today = LocalDate.now();

        List<String> delayedTasksTitles = new java.util.ArrayList<>(Collections.emptyList());

        for (Task task : tasks) {
            if (today.isAfter(task.getDueDate()) && task.getStatus() != COMPLETED) {
                task.setStatus(DELAYED);
                delayedTasksTitles.add(task.getTitle());
            }
            else if (task.getStatus() == DELAYED) task.setStatus(OPEN);
        }

        if (!delayedTasksTitles.isEmpty()) {
            String title;
            String message = String.join(", ", delayedTasksTitles);

            if (delayedTasksTitles.size() == 1) {
                title = "1 delayed task!";
                message += " is delayed!";
            }
            else {
                title = delayedTasksTitles.size() + " delayed tasks!";
                message += " are delayed!";
            }

            showAlert(title, message);
            tasksTableView.refresh();   // If a task from DELAYED to OPEN (because of a manual DueDate change) we need to refresh the table.
        }
    }
}