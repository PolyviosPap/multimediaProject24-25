package polpapntua.multimediaproject2425.models;

import java.time.LocalDate;

public class Reminder {
    public Long id;
    public Task task;
    public LocalDate reminderDate;

    public Reminder(Task task, LocalDate reminderDate) {
        this.task = task;
        this.reminderDate = reminderDate;
    }

    public LocalDate getReminderDate() { return reminderDate; }
}