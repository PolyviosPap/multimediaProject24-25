package polpapntua.multimediaproject2425.controllers;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DefaultStringConverter;
import polpapntua.multimediaproject2425.helpers;
import polpapntua.multimediaproject2425.models.Priority;
import java.math.BigInteger;
import java.util.Comparator;

import static polpapntua.multimediaproject2425.helpers.createButton;

public class PrioritiesController {
    private ObservableList<Priority> priorities;

    @FXML
    private TableView<Priority> prioritiesTableView;

    @FXML
    private TableColumn<Priority, String> prioritiesNameColumn;

    @FXML
    private TableColumn<Priority, Integer> prioritiesLevelColumn;

    @FXML
    private TableColumn<Priority, Void> actionsColumn;

    @FXML
    private AnchorPane addPriorityPane;

    @FXML
    private TextField addNewPriorityName;

    @FXML
    private Spinner<Integer> addNewPriorityLevel;

    private BigInteger maxPriorityId = BigInteger.ZERO;

    @FXML
    public void initialize() {
        prioritiesTableView.setEditable(true);  // Edit the values on the cell

        // Button for adding new category (it shows the corresponding modal)
        Button addNewPriorityButton = helpers.createButton("/icons/add-icon.png");
        addNewPriorityButton.setOnAction(event -> addPriorityPane.setVisible(true));
        actionsColumn.setGraphic(addNewPriorityButton);     // Place it in the column header.

        prioritiesNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        prioritiesNameColumn.setCellFactory(column -> new TextFieldTableCell<>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                Priority priority = getTableView().getItems().get(getIndex());
                if (priority.getId().equals(BigInteger.ZERO)) { // Default priority check
                    return; // Prevent editing
                }
                super.startEdit();
            }

            @Override
            public void commitEdit(String newValue) {
                Priority priority = getTableView().getItems().get(getIndex());
                if (!priority.getId().equals(BigInteger.ZERO)) { // Allow changes only for non-default
                    super.commitEdit(newValue);
                    priority.setName(newValue);
                }
            }
        });

        prioritiesLevelColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLevel()).asObject());
        prioritiesLevelColumn.setCellFactory(column -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>(0, 100, 0);

            {
                spinner.setEditable(true);

                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    if (newValue != null && isEditing()) {
                        commitEdit(newValue);
                    }
                });

                spinner.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal && isEditing()) { // Commit on focus loss
                        commitEdit(spinner.getValue());
                    }
                });

                spinner.getEditor().setOnAction(event -> commitEdit(spinner.getValue()));
            }

            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);

                if (empty || value == null) { // Ensure we handle null safely
                    setGraphic(null);
                    setText(null);
                } else {
                    if (isEditing()) {
                        spinner.getValueFactory().setValue(value);
                        setGraphic(spinner);
                        setText(null);
                    } else {
                        setGraphic(null);
                        setText(String.valueOf(value)); // Handle null safely
                    }
                }
            }

            @Override
            public void startEdit() {
                if (getTableRow() != null && getTableRow().getItem() != null) {
                    super.startEdit();
                    spinner.getValueFactory().setValue(getItem());
                    setGraphic(spinner);
                    setText(null);
                    spinner.requestFocus();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setGraphic(null);
                setText(getItem().toString());
            }

            @Override
            public void commitEdit(Integer value) {
                if (getTableRow() != null && getTableRow().getItem() != null) {
                    Priority priority = getTableRow().getItem();
                    priority.setLevel(value);
                    super.commitEdit(value);
                }
            }
        });


        addNewPriorityLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));

        actionsColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button deleteButton = createButton("/icons/bin-icon.png");
            {
                deleteButton.setOnAction(event -> {
                    Priority selectedPriority = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(selectedPriority);
                    priorities.remove(selectedPriority);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Priority priority = getTableView().getItems().get(getIndex());
                    if (priority.getId().equals(BigInteger.ZERO)) {
                        setGraphic(null); // Hide delete button for default priority
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            }
        });

        DoubleBinding remainingWidth = prioritiesTableView.widthProperty()
                .subtract(actionsColumn.widthProperty());

        prioritiesNameColumn.prefWidthProperty().bind(remainingWidth.multiply(0.6));
        prioritiesLevelColumn.prefWidthProperty().bind(remainingWidth.multiply(0.4));
    }

    public void setPriorities(ObservableList<Priority> priorities) {
        this.priorities = priorities;

        prioritiesTableView.setItems(priorities);

        maxPriorityId = this.priorities.stream()
                .map(Priority::getId)
                .max(Comparator.naturalOrder())
                .orElse(BigInteger.ZERO);
    }

    @FXML
    private void onCancel() {
        addNewPriorityName.clear();
        addNewPriorityLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));

        addPriorityPane.setVisible(false);
    }

    @FXML
    private void onCategorySave() {
        if (addNewPriorityName.getText().isEmpty()) {
            helpers.showAlert("", "You need to fill all the fields!");
            return;
        }

        maxPriorityId = maxPriorityId.add(BigInteger.ONE);
        Priority newPriority = new Priority(maxPriorityId, addNewPriorityName.getText(), addNewPriorityLevel.getValue());

        priorities.add(newPriority);

        onCancel();
    }
}