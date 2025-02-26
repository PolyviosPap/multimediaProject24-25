package polpapntua.multimediaproject2425.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import polpapntua.multimediaproject2425.models.Priority;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrioritiesService {
    protected static final Logger logger = LogManager.getLogger();

    private final List<Priority> priorities;

    public PrioritiesService(String jsonFilePath) {
        this.priorities = loadPrioritiesFromJson(jsonFilePath);

        // Ensure that the default priority is present.
        boolean defaultPriorityPresent = false;
        for (Priority priority : this.priorities) {
            if (Objects.equals(priority.getId(), BigInteger.ZERO) && Objects.equals(priority.getName(), "Default")) {
                defaultPriorityPresent = true;
                break;
            }
        }

        if (!defaultPriorityPresent) {
            this.priorities.addFirst(new Priority(BigInteger.ZERO, "Default", 0));
        }
    }

    private List<Priority> loadPrioritiesFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<>() {
            });
        } catch (IOException ex) {
            // In case of an exception, print it and return a null list.
            logger.error("Exception occurred while deserializing priorities.json: {}", ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    public List<Priority> getAllPriorities() {
        return priorities;
    }
}