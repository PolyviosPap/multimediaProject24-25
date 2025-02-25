package polpapntua.multimediaproject2425.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;

public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private BigInteger id;
    private String name;
    @JsonIgnore
    private boolean hasBeenEdited;

    public Category() { }

    public Category(BigInteger id, String name, boolean hasBeenEdited) {
        this.id = id;
        this.name = name;
        this.hasBeenEdited = hasBeenEdited;
    }

    public BigInteger getId() { return id; }
    public void setId(BigInteger id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean getHasBeenEdited() { return hasBeenEdited; }
    public void setHasBeenEdited(boolean hasBeenEdited) { this.hasBeenEdited = hasBeenEdited; }
}