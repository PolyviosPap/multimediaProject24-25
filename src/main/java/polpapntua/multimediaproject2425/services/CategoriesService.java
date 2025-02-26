package polpapntua.multimediaproject2425.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import polpapntua.multimediaproject2425.models.Category;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoriesService {
    protected static final Logger logger = LogManager.getLogger();

    private final List<Category> categories;

    public CategoriesService(String jsonFilePath) {
        this.categories = loadCategoriesFromJson(jsonFilePath);
    }

    private List<Category> loadCategoriesFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<>() {
            });
        } catch (IOException ex) {
            // In case of an exception, print it and return a null list.
            logger.error("Exception occurred while deserializing categories.json: {}", ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    public List<Category> getAllCategories() {
        return categories;
    }
}