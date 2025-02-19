package polpapntua.multimediaproject2425.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import polpapntua.multimediaproject2425.models.Category;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CategoriesService {
    private List<Category> categories;

    public CategoriesService(String jsonFilePath) {
        this.categories = loadCategoriesFromJson(jsonFilePath);
    }

    private List<Category> loadCategoriesFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<List<Category>>() {});
        } catch (IOException e) {
            // In case of an exception, print it and return a null list.
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Category> getAllCategories() {
        return categories;
    }
}