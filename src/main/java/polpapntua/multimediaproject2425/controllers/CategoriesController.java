package polpapntua.multimediaproject2425.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DefaultStringConverter;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.helpers;
import java.math.BigInteger;
import java.util.Comparator;

public class CategoriesController {
    private ObservableList<Category> categories;

    @FXML
    private TableView<Category> categoriesTableView;

    @FXML
    private TableColumn<Category, String> categoriesNameColumn;

    @FXML
    private TableColumn<Category, Void> addCategoryColumn;

    @FXML
    private AnchorPane addCategoryPane;

    @FXML
    private TextField addNewCategoryName;

    private BigInteger maxCategoryId = BigInteger.valueOf(0);

    @FXML
    public void initialize() {
        categoriesTableView.setEditable(true);  // Edit the values on the cell
        //categoriesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // added to remove the horizontal scrollbar -- maybe it will come handy later

        // Button for adding new category (it shows the corresponding modal)
        Button addNewCategoryButton = helpers.createButton("/icons/add-icon.png");
        addNewCategoryButton.setOnAction(event -> addCategoryPane.setVisible(true));
        addCategoryColumn.setGraphic(addNewCategoryButton);     // Place it in the column header.

        categoriesNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        //categoriesNameColumn.prefWidthProperty().bind(categoriesTableView.widthProperty().multiply(1)); -- maybe it will come handy later
        categoriesNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        categoriesNameColumn.setOnEditCommit(event -> {
            Category category = event.getRowValue();
            category.setName(event.getNewValue());
            category.setHasBeenEdited(true);
        });

        // expand the name column in order to push the save column into the right side.
        categoriesNameColumn.prefWidthProperty().bind(
                categoriesTableView.widthProperty().subtract(addCategoryColumn.widthProperty())
        );
    }

    // Method to receive categories from MainController
    public void setCategories(ObservableList<Category> categories) {
        this.categories = categories;

        categoriesTableView.setItems(this.categories);

        maxCategoryId = this.categories.stream()
                .map(Category::getId)
                .max(Comparator.naturalOrder())
                .orElse(BigInteger.valueOf(0));
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

        Category newCategory = new Category(maxCategoryId.add(BigInteger.valueOf(1)), addNewCategoryName.getText(), true);

        categories.add(newCategory);
        addNewCategoryName.clear();
        addCategoryPane.setVisible(false);
    }
}