package polpapntua.multimediaproject2425.controllers;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;
import polpapntua.multimediaproject2425.helpers;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrioritiesController {
    @FXML
    private TableView<Priority> prioritiesTableView;

    @FXML
    private TableColumn<Priority, String> prioritiesNameColumn;

    @FXML
    private TableColumn<Priority, Integer> prioritiesLevelColumn;

//    @FXML
//    private TableColumn<Category, Void> prioritiesSaveColumn;

    @FXML
    public void initialize() {
        prioritiesTableView.setEditable(true);  // Edit the values on the cell

//        Button saveAllButton = new Button();
//        ImageView saveIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/save-icon.png"))));
//        saveIcon.setFitWidth(20);
//        saveIcon.setFitHeight(20);
//        saveAllButton.setPrefSize(24, 24);  // Adjusting the button size (for some reason it removes the bottom - horizontal scroll bar)
//
//        saveAllButton.setGraphic(saveIcon);
//        saveAllButton.setOnAction(event -> {
//            List<Priority> priorities = new ArrayList<>(prioritiesTableView.getItems());
//            helpers.serializeObjects("src/main/resources/data/categories.json", priorities);
//            saveAllButton.setDisable(true);
//        });
//
//        saveAllButton.setDisable(true);
//
//        // Set the button as the column header
//        prioritiesSaveColumn.setGraphic(saveAllButton);

        prioritiesNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        prioritiesNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        prioritiesNameColumn.setOnEditCommit(event -> {
            Priority priority = event.getRowValue();
            priority.setName(event.getNewValue());
            priority.setHasBeenEdited(true);
            //saveAllButton.setDisable(false);
        });

        prioritiesLevelColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLevel()).asObject());
        prioritiesLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new helpers.SafeIntegerStringConverter()));
        prioritiesLevelColumn.setOnEditCommit(event -> {
            // In case of invalid input, the SafeIntegerStringConverter returns null
            if (event.getNewValue() != null) { // Only update if input is valid
                Priority priority = event.getRowValue();
                priority.setLevel(event.getNewValue());
                priority.setHasBeenEdited(true);
                //saveAllButton.setDisable(false);
            } else { helpers.showAlert("Invalid input", "Please enter a valid number."); }
        });

        // expand both the name and level columns in order to push the save column into the right side.
//        DoubleBinding availableWidth = prioritiesTableView.widthProperty()
//                .subtract(prioritiesSaveColumn.widthProperty());
//                //.divide(2); // Divide equally between two columns
//
//        prioritiesNameColumn.prefWidthProperty().bind(availableWidth.multiply(0.6));
//        prioritiesLevelColumn.prefWidthProperty().bind(availableWidth.multiply(0.4));
    }

    public void setPriorities(ObservableList<Priority> prioritiesList) {
        prioritiesTableView.setItems(prioritiesList);
    }
}