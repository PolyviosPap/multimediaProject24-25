package polpapntua.multimediaproject2425.enums;

public enum TaskStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    POSTPONED("Postponed"),
    COMPLETED("Completed"),
    DELAYED("Delayed");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}