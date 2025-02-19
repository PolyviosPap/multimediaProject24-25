package polpapntua.multimediaproject2425.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import polpapntua.multimediaproject2425.models.Category;

import java.io.IOException;

public class CategoriesController {
    @FXML
    private static TableView<Category> categoriesTableView;

    @FXML
    private static TableColumn<Category, Long> categoriesIdColumn;

    @FXML
    private static TableColumn<Category, String> categoriesNameColumn;

    public static void loadCategories(ObservableList<Category> categories) {
        categoriesIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        categoriesNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        categoriesTableView.setItems(categories);
    }
}
