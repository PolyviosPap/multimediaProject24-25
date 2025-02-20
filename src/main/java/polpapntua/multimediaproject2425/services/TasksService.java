package polpapntua.multimediaproject2425.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import polpapntua.multimediaproject2425.models.Task;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TasksService {
    private final List<Task> tasks;

    public TasksService(String jsonFilePath) {
        this.tasks = loadTasksFromJson(jsonFilePath);
    }

    private List<Task> loadTasksFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<List<Task>>() {});
        } catch (IOException e) {
            // In case of an exception, print it and return a null list.
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}
