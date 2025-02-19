package polpapntua.multimediaproject2425.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;

import java.io.IOException;
import java.util.List;

public class PrioritiesController {
    @FXML
    private TableView<Priority> prioritiesTableView;

    @FXML
    private TableColumn<Category, String> prioritiesNameColumn;

    @FXML
    public void initialize() {
        prioritiesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // added to remove the horizontal scrollbar

        prioritiesNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        prioritiesNameColumn.prefWidthProperty().bind(prioritiesTableView.widthProperty().multiply(1));
    }

    // Method to receive categories from MainController
    public void setPriorities(ObservableList<Priority> prioritiesList) {
        prioritiesTableView.setItems(prioritiesList);
    }
}
