package polpapntua.multimediaproject2425.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import polpapntua.multimediaproject2425.enums.TaskStatus;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

public class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private BigInteger id;
    private String title;
    private String description;
    private BigInteger categoryId;
    @JsonIgnore
    private Category category;
    private BigInteger priorityId;
    @JsonIgnore
    private Priority priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private TaskStatus status;
    //public List<Reminder> reminders;
    @JsonIgnore
    private boolean hasBeenEdited;

    public Task() { }

    public Task(BigInteger id, String title, String description, BigInteger categoryId, BigInteger priorityId, LocalDate dueDate, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.priorityId = priorityId;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Task(BigInteger id, String title, String description, BigInteger categoryId, Category category, BigInteger priorityId, Priority priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.category = category;
        this.priorityId = priorityId;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public Task(BigInteger id, String title, String description, BigInteger categoryId, Category category, BigInteger priorityId, Priority priority, LocalDate dueDate, TaskStatus status) {
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

    public BigInteger getId() {
        return id;
    }
    public void setId(BigInteger id) { this.id = id; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) { this.description = description; }

    public BigInteger getCategoryId() { return categoryId; }
    public void setCategoryId(BigInteger categoryId) { this.categoryId = categoryId; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) {
        this.categoryId = category.getId();
        this.category = category;
    }

    public BigInteger getPriorityId() {
        return priorityId;
    }
    public void setPriorityId(BigInteger priorityId) { this.priorityId = priorityId; }

    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priorityId = priority.getId();
        this.priority = priority;
    }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public boolean getHasBeenEdited() { return this.hasBeenEdited; }
    public void setHasBeenEdited(boolean hasBeenEdited) { this.hasBeenEdited = hasBeenEdited; }
}