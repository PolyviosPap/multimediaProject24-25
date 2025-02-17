package polpapntua.multimediaproject2425.models;

public class Priority {
    public Long id;
    public String name;
    public int level;

    public static final Priority DEFAULT = new Priority("Default", 1);

    public Priority(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}