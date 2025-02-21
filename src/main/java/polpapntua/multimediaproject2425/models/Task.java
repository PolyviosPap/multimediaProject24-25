package polpapntua.multimediaproject2425.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import polpapntua.multimediaproject2425.enums.TaskStatus;
import java.time.LocalDate;

public class Task {
    public Long id;
    public String title;
    public String description;
    public Long categoryId;
    public Category category;
    public Long priorityId;
    public Priority priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate dueDate;
    public TaskStatus status;
    //public List<Reminder> reminders;

    public Task() { }

    public Task(Long id, String title, String description, Long categoryId, Long priorityId, LocalDate dueDate, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.priorityId = priorityId;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Task(Long id, String title, String description, Long categoryId, Category category, Long priorityId, Priority priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.category = category;
        this.priorityId = priorityId;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public Task(Long id, String title, String description, Long categoryId, Category category, Long priorityId, Priority priority, LocalDate dueDate, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.category = category;
        this.priorityId = priorityId;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public String getCategoryName() { return category.getName(); }

    public Long getPriorityId() {
        return priorityId;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getPriorityName() { return priority.getName(); }

    public LocalDate getDueDate() { return dueDate; }

    public TaskStatus getStatus() {
        return status;
    }

    public void setId(Long id) { this.id = id; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.categoryId = category.getId();
        this.category = category;
    }

    public void setPriority(Priority priority) {
        this.priorityId = priority.getId();
        this.priority = priority;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}