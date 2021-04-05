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

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
