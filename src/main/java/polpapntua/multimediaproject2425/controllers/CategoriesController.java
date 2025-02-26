package polpapntua.multimediaproject2425.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DefaultStringConverter;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.helpers;
import polpapntua.multimediaproject2425.models.Task;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;

import static polpapntua.multimediaproject2425.helpers.createButton;

public class CategoriesController {
    private ObservableList<Category> categories;
    private ObservableList<Task> tasks;

    @FXML
    private TableView<Category> categoriesTableView;

    @FXML
    private TableColumn<Category, String> categoriesNameColumn;

    @FXML
    private TableColumn<Category, Void> actionsColumn;

    @FXML
    private AnchorPane addCategoryPane;

    @FXML
    private TextField addNewCategoryName;

    private BigInteger maxCategoryId = BigInteger.ZERO;

    @FXML
    public void initialize() {
        categoriesTableView.setEditable(true);  // Edit the values on the cell
        //categoriesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // added to remove the horizontal scrollbar -- maybe it will come handy later

        // Button for adding new category (it shows the corresponding modal)
        Button addNewCategoryButton = createButton("/icons/add-icon.png");
        addNewCategoryButton.setOnAction(event -> addCategoryPane.setVisible(true));
        actionsColumn.setGraphic(addNewCategoryButton);     // Place it in the column header.

        categoriesNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        //categoriesNameColumn.prefWidthProperty().bind(categoriesTableView.widthProperty().multiply(1)); -- maybe it will come handy later
        categoriesNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        categoriesNameColumn.setOnEditCommit(event -> {
            Category category = event.getRowValue();
            category.setName(event.getNewValue());
        });

        actionsColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button deleteButton = createButton("/icons/bin-icon.png");
            {
                deleteButton.setOnAction(event -> {
                    Category selectedCategory = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(selectedCategory);
                    categories.remove(selectedCategory);

                    deleteTasksViaCategoryId(selectedCategory.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // expand the name column in order to push the save column into the right side.
        categoriesNameColumn.prefWidthProperty().bind(
                categoriesTableView.widthProperty().subtract(actionsColumn.widthProperty())
        );
    }

    // Method to receive categories from MainController
    public void setNeededObjects(ObservableList<Category> categories, ObservableList<Task> tasks) {
        this.categories = categories;
        this.tasks = tasks;

        categoriesTableView.setItems(this.categories);

        maxCategoryId = this.categories.stream()
                .map(Category::getId)
                .max(Comparator.naturalOrder())
                .orElse(BigInteger.ZERO);
    }

    @FXML
    private void onCancel() {
        addNewCategoryName.clear();
        addCategoryPane.setVisible(false);
    }

    @FXML
    private void onCategorySave() {
        if (addNewCategoryName.getText().isEmpty()) {
            helpers.showAlert("", "You need to fill all the fields!");
            return;
        }

        maxCategoryId = maxCategoryId.add(BigInteger.ONE);
        Category newCategory = new Category(maxCategoryId, addNewCategoryName.getText());

        categories.add(newCategory);

        onCancel();
    }

    private void deleteTasksViaCategoryId(BigInteger categoryId) {
        tasks.removeIf(task -> Objects.equals(task.getCategoryId(), categoryId));
    }
}