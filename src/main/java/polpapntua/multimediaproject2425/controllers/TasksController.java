package polpapntua.multimediaproject2425.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;
import polpapntua.multimediaproject2425.models.Task;
import java.time.LocalDate;
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
    private TableColumn<Task, Priority> tasksPriorityColumn;

    @FXML
    private TableColumn<Task, String> tasksDueDateColumn;

    @FXML
    private TableColumn<Task, String> tasksStatusColumn;

//    @FXML
//    private TableColumn<Task, Void> tasksSaveColumn;

    @FXML
    public void initialize() {
        tasksTableView.setEditable(true);

//        tasksSaveColumn.setCellFactory(new Callback<>() {
//            @Override
//            public TableCell<Task, Void> call(TableColumn<Task, Void> param) {
//                return new TableCell<>() {
//                    private final Button saveButton = helpers.createButton("/icons/save-icon.png");
//                    {
//                        saveButton.setOnAction(event -> {
//                            Task task = getTableView().getItems().get(getIndex());
//                            String taskPath = "src/main/resources/data/tasks/task_" + task.getId().toString() + ".json";
//                            serializeObject(taskPath, task);
//                        });
//                    }
//
//                    @Override
//                    protected void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                        } else {
//                            setGraphic(saveButton);
//                        }
//                    }
//                };
//            }
//        });

        tasksTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        tasksTitleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tasksTitleColumn.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            task.setTitle(event.getNewValue());
            task.setHasBeenEdited(true);
        });

        tasksDescriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        tasksDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tasksDescriptionColumn.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            task.setDescription(event.getNewValue());
            task.setHasBeenEdited(true);
        });

        tasksCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));

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
                task.setHasBeenEdited(true);
                setGraphic(null);
            }
        });

        tasksStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
    }

    public void setNeededObjects(ObservableList<Task> tasks, ObservableList<Category> categories, ObservableList<Priority> priorities) {
        tasksTableView.setItems(tasks);

        tasksPriorityColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<Priority>() {
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
                },
                FXCollections.observableArrayList(priorities)
        ));
        tasksPriorityColumn.setOnEditCommit(event -> {
            Task task = event.getRowValue();
            task.setPriority(event.getNewValue());
        });
    }
}