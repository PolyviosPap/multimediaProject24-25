package polpapntua.multimediaproject2425.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;
import polpapntua.multimediaproject2425.models.Task;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TasksService {
    private final List<Task> tasks;
    private final List<Category> categories;
    private final List<Priority> priorities;

    public TasksService(String jsonFilesPath, List<Category> categories, List<Priority> priorities) {
        this.categories = categories;
        this.priorities = priorities;

        this.tasks = loadTasksFromJson(jsonFilesPath);
    }

    private List<Task> loadTasksFromJson(String jsonFilesPath) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Task> allTasks = new ArrayList<>();

        File directory = new File(jsonFilesPath);
        if (!directory.isDirectory()) {
            System.err.println("The given path is not a directory.");
            return List.of();
        }

        File[] jsonFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (jsonFiles == null) {
            return List.of();
        }

        for (File jsonFile : jsonFiles) {
            try {
                Task task = objectMapper.readValue(jsonFile, new TypeReference<Task>() {});

                task.category = findCategoryById(task.categoryId);
                task.priority = findPriorityById(task.priorityId);

                allTasks.add(task);
            } catch (IOException e) {
                System.err.println("Failed to read file: " + jsonFile.getName());
                e.printStackTrace();
            }
        }

        return allTasks;
    }

    private Category findCategoryById(Long id) {
        return categories.stream()
                .filter(cat -> cat.getId() == id)
                .findFirst()
                .orElse(null); // Returns null if no match is found
    }

    private Priority findPriorityById(Long id) {
        return priorities.stream()
                .filter(pr -> pr.getId() == id)
                .findFirst()
                .orElse(null); // Returns null if no match is found
    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}
