package polpapntua.multimediaproject2425.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import polpapntua.multimediaproject2425.Main;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.services.CategoryService;

import java.io.IOException;

public class MainController {
    @FXML
    private BorderPane contentPane;

    private CategoryService categoryService = new CategoryService("src/main/resources/data/categories.json");
    private ObservableList<Category> categories = FXCollections.observableArrayList(categoryService.getAllCategories());

    public void displayTasks() {
        loadView("tasksView.fxml");
    }

    public void displayCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("categoriesView.fxml"));
            contentPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadView("categoriesView.fxml");
        CategoriesController.loadCategories(categories);
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            contentPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}