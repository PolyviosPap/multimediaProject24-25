package polpapntua.multimediaproject2425.controllers;

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
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.helpers;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriesController {
    @FXML
    private TableView<Category> categoriesTableView;

    @FXML
    private TableColumn<Category, String> categoriesNameColumn;

    @FXML
    private TableColumn<Category, Void> categoriesSaveColumn;

    @FXML
    public void initialize() {
        categoriesTableView.setEditable(true);  // Edit the values on the cell
        //categoriesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // added to remove the horizontal scrollbar -- maybe it will come handy later

        Button saveAllButton = new Button();
        ImageView saveIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/save-icon.png"))));
        saveIcon.setFitWidth(20);
        saveIcon.setFitHeight(20);
        saveAllButton.setPrefSize(24, 24);  // Adjusting the button size (for some reason it removes the bottom - horizontal scroll bar)

        saveAllButton.setGraphic(saveIcon);
        saveAllButton.setOnAction(event -> {
            List<Category> categories = new ArrayList<>(categoriesTableView.getItems());
            helpers.serializeObjects("src/main/resources/data/categories.json", categories);
            saveAllButton.setDisable(true);
        });

        saveAllButton.setDisable(true);

        // Set the button as the column header
        categoriesSaveColumn.setGraphic(saveAllButton);

        categoriesNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        //categoriesNameColumn.prefWidthProperty().bind(categoriesTableView.widthProperty().multiply(1)); -- maybe it will come handy later
        categoriesNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        categoriesNameColumn.setOnEditCommit(event -> {
            Category category = event.getRowValue();
            category.setName(event.getNewValue());
            saveAllButton.setDisable(false);
        });

//        categoriesSaveColumn.setCellFactory(new Callback<>() {
//            @Override
//            public TableCell<Category, Void> call(TableColumn<Category, Void> param) {
//                return new TableCell<>() {
//                    private final Button saveButton = createButton("/icons/save-icon.png");
//                    {
//                        saveButton.setOnAction(event -> {
//                            List<Category> categories = new ArrayList<>(getTableView().getItems());
//                            helpers.serializeObjects("src/main/resources/data/categories.json", categories);
//                        });
//                    }
//
//                    @Override
//                    protected void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                        } else {
//                            HBox buttonsBox = new HBox(5, saveButton);
//                            setGraphic(buttonsBox);
//                        }
//                    }
//                };
//            }
//        });

        // expand the name column in order to push the save column into the right side.
        categoriesNameColumn.prefWidthProperty().bind(
                categoriesTableView.widthProperty().subtract(categoriesSaveColumn.widthProperty())
        );
    }

    // Method to receive categories from MainController
    public void setCategories(ObservableList<Category> categoriesList) {
        categoriesTableView.setItems(categoriesList);
    }
}
