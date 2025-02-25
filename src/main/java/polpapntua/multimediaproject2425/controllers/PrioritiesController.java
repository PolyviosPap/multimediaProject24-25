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
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;

import java.math.BigInteger;
import java.util.Comparator;

public class PrioritiesController {
    private ObservableList<Priority> priorities;

    @FXML
    private TableView<Priority> prioritiesTableView;

    @FXML
    private TableColumn<Priority, String> prioritiesNameColumn;

    @FXML
    private TableColumn<Priority, Integer> prioritiesLevelColumn;

    @FXML
    private TableColumn<Category, Void> addPriorityColumn;

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
        addPriorityColumn.setGraphic(addNewPriorityButton);     // Place it in the column header.

        prioritiesNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        prioritiesNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        prioritiesNameColumn.setOnEditCommit(event -> {
            Priority priority = event.getRowValue();
            priority.setName(event.getNewValue());
        });

        prioritiesLevelColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLevel()).asObject());
        prioritiesLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new helpers.SafeIntegerStringConverter()));
        prioritiesLevelColumn.setOnEditCommit(event -> {
            // In case of invalid input, the SafeIntegerStringConverter returns null
            if (event.getNewValue() != null) { // Only update if input is valid
                Priority priority = event.getRowValue();
                priority.setLevel(event.getNewValue());
            } else { helpers.showAlert("Invalid input", "Please enter a valid number."); }
        });

        addNewPriorityLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));

        DoubleBinding remainingWidth = prioritiesTableView.widthProperty()
                .subtract(addPriorityColumn.widthProperty());

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