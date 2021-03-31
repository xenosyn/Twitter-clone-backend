package socialmedia;

public class Account {
    private int id;
    private String handle;
    private String description;

    public Account(int id, String handle,String description) {
        this.id = id;
        this.handle = handle;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }
}
