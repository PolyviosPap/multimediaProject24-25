package polpapntua.multimediaproject2425.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Task;
import polpapntua.multimediaproject2425.enums.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryService {
    private List<Category> categories;

    public CategoryService(String jsonFilePath) {
        this.categories = loadCategoriesFromJson(jsonFilePath);
    }

    private List<Category> loadCategoriesFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<List<Category>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return List.of(); // Αν αποτύχει, επιστρέφει κενή λίστα
        }
    }

    public List<Category> getAllCategories() {
        return categories;
    }

    //public List<Task> getTasksSortedByPriority() {
    //    return tasks.stream()
    //            .sorted((t1, t2) -> Integer.compare(t2.getPriority().getLevel(), t1.getPriority().getLevel()))
    //            .collect(Collectors.toList());
    //}

    //public List<Task> getPendingTasks() {
    //    return tasks.stream()
    //            .filter(task -> task.getStatus() != TaskStatus.COMPLETED)
    //            .collect(Collectors.toList());
    //}
}