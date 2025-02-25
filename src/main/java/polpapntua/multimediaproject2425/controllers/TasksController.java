package polpapntua.multimediaproject2425.controllers;

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
import java.util.Comparator;

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
    private TableColumn<Task, Void> addTaskColumn;

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
        addTaskColumn.setGraphic(addNewTaskButton);     // Place it in the column header.

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
    }

    public void setNeededObjects(ObservableList<Task> tasks, ObservableList<Category> categories, ObservableList<Priority> priorities) {
        this.tasks = tasks;

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
                TaskStatus.OPEN
        );

        tasks.add(newTask);

        onCancel();
    }
}