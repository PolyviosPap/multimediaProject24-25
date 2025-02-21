package polpapntua.multimediaproject2425.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import polpapntua.multimediaproject2425.models.Priority;

public class PrioritiesController {
    @FXML
    private TableView<Priority> prioritiesTableView;

    @FXML
    private TableColumn<Priority, String> prioritiesNameColumn;

    @FXML
    private TableColumn<Priority, String> prioritiesLevelColumn;

    @FXML
    public void initialize() {
        prioritiesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // added to remove the horizontal scrollbar

        prioritiesNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        prioritiesLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getLevel())));
    }

    public void setPriorities(ObservableList<Priority> prioritiesList) {
        prioritiesTableView.setItems(prioritiesList);
    }
}