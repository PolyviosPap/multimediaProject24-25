package polpapntua.multimediaproject2425.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import polpapntua.multimediaproject2425.models.Priority;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PrioritiesService {
    private List<Priority> priorities;

    public PrioritiesService(String jsonFilePath) {
        this.priorities = loadPrioritiesFromJson(jsonFilePath);
    }

    private List<Priority> loadPrioritiesFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<List<Priority>>() {});
        } catch (IOException e) {
            // In case of an exception, print it and return a null list.
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Priority> getAllPriorities() {
        return priorities;
    }
}