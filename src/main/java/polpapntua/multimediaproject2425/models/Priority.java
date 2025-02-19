package polpapntua.multimediaproject2425.models;

public class Priority {
    private Long id;
    private String name;
    private int level;

    public static final Priority DEFAULT = new Priority("Default", 1);

    public Priority() { }

    public Priority(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLevel() { return this.level; }
    public void setLevel(int level) { this.level = level; }
}