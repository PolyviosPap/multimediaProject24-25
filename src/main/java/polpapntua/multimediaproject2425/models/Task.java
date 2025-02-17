package polpapntua.multimediaproject2425.models;

import polpapntua.multimediaproject2425.enums.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {
    public Long id;
    public String title;
    public String description;
    public Category category;
    public Priority priority;
    public LocalDate dueDate;
    public TaskStatus status;
    public List<Reminder> reminders;

    public Task() { }

    public Task(String title, String description, Category category, Priority priority, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = TaskStatus.OPEN;
        this.reminders = new ArrayList<>();
    }

    public void addReminder(Reminder reminder) {
        if (this.status != TaskStatus.COMPLETED) {
            reminders.add(reminder);
        }
    }

    public void completeTask() {
        this.status = TaskStatus.COMPLETED;
        reminders.clear();
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        if (status == TaskStatus.COMPLETED) {
            reminders.clear();
        }
    }

    public String getTitle() { return title; }

    public TaskStatus getStatus() { return status; }

    public Priority getPriority() { return priority; }
}