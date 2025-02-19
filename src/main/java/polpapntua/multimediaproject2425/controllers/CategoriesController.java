package polpapntua.multimediaproject2425.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import polpapntua.multimediaproject2425.models.Category;

import java.io.IOException;
import java.util.List;

public class CategoriesController {
    @FXML
    private TableView<Category> categoriesTableView;

    //@FXML
    //private TableColumn<Category, Long> categoriesIdColumn;

    @FXML
    private TableColumn<Category, String> categoriesNameColumn;

    @FXML
    public void initialize() {
        categoriesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // added to remove the horizontal scrollbar

        //categoriesIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));

        categoriesNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        categoriesNameColumn.prefWidthProperty().bind(categoriesTableView.widthProperty().multiply(1));
    }

    // Method to receive categories from MainController
    public void setCategories(ObservableList<Category> categoriesList) {
        categoriesTableView.setItems(categoriesList);
    }
}
