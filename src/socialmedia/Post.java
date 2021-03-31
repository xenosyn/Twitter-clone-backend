package socialmedia;

public abstract class Post {
    private int id;
    private Account author;
    private String message;

    public abstract int getId();

    public String getMessage() {
        return message;
    }
}
