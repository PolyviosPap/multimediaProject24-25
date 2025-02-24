package polpapntua.multimediaproject2425.models;

import java.io.Serial;
import java.io.Serializable;

public class Priority implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private int level;

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