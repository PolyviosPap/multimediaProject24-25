package polpapntua.multimediaproject2425.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serial;
import java.io.Serializable;

public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    @JsonIgnore
    private boolean hasBeenEdited;

    public Category() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean getHasBeenEdited() { return hasBeenEdited; }
    public void setHasBeenEdited(boolean hasBeenEdited) { this.hasBeenEdited = hasBeenEdited; }
}