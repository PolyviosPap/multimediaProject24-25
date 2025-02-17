package polpapntua.multimediaproject2425;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Task;
import polpapntua.multimediaproject2425.services.CategoryService;

public class MainController {
    @FXML
    private TableView<Category> taskTableView;

    @FXML
    private TableColumn<Category, Long> idColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    private CategoryService categoryService;

    public void initialize() {
        categoryService = new CategoryService("src/main/resources/categories.json");

        // Ρύθμιση στηλών για το TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Φόρτωση και εμφάνιση δεδομένων
        loadTasks();
    }

    private void loadTasks() {
        ObservableList<Category> categoriesList = FXCollections.observableArrayList(categoryService.getAllCategories());
        taskTableView.setItems(categoriesList);
    }
}
