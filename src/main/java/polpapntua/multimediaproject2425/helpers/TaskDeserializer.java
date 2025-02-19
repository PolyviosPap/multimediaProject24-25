package polpapntua.multimediaproject2425.helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import polpapntua.multimediaproject2425.enums.TaskStatus;
import polpapntua.multimediaproject2425.models.Category;
import polpapntua.multimediaproject2425.models.Priority;
import polpapntua.multimediaproject2425.models.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class TaskDeserializer extends JsonDeserializer<Task> {
    private final List<Priority> priorities;
    private final List<Category> categories;

    public TaskDeserializer(List<Priority> priorities, List<Category> categories) {
        this.priorities = priorities;
        this.categories = categories;
    }

    @Override
    public Task deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        Long id = node.has("id") ? node.get("id").asLong() : null;
        String title = node.get("title").asText();
        String description = node.get("description").asText();
        Long categoryId = node.get("categoryId").asLong();
        Long priorityId = node.get("priorityId").asLong();
        LocalDate dueDate = LocalDate.parse(node.get("dueDate").asText());
        TaskStatus status = TaskStatus.valueOf(node.get("status").asText().toUpperCase());

        // Find the matching category object
        Category category = categories.stream()
                .filter(c -> Objects.equals(c.getId(), categoryId))
                .findFirst()
                .orElse(null);

        // Find the matching Priority object
        Priority priority = priorities.stream()
                .filter(p -> Objects.equals(p.getId(), priorityId))
                .findFirst()
                .orElse(null);

        // Create the task object
        Task task = new Task(title, description, category, priority, dueDate);
        task.id = id;
        task.status = status;

        return task;
    }
}
