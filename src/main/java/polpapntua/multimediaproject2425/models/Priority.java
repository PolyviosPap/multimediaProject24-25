package polpapntua.multimediaproject2425.models;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;

public class Priority implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private BigInteger id;
    private String name;
    private int level;

    public Priority() { }

    public Priority(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Priority(BigInteger id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public BigInteger getId() { return id; }
    public void setId(BigInteger id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLevel() { return this.level; }
    public void setLevel(int level) { this.level = level; }
}