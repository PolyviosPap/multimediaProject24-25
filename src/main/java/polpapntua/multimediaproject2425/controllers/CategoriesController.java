package polpapntua.multimediaproject2425.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import polpapntua.multimediaproject2425.models.Category;

public class CategoriesController {
    @FXML
    private TableView<Category> categoriesTableView;

    @FXML
    private TableColumn<Category, String> categoriesNameColumn;

    @FXML
    public void initialize() {
        categoriesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // added to remove the horizontal scrollbar

        categoriesNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        //categoriesNameColumn.prefWidthProperty().bind(categoriesTableView.widthProperty().multiply(1)); -- maybe it will come handy later
    }

    // Method to receive categories from MainController
    public void setCategories(ObservableList<Category> categoriesList) {
        categoriesTableView.setItems(categoriesList);
    }
}
