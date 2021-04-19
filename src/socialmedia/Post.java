package socialmedia;

public abstract class Post {
    private int id;
    private Account author;
    private String message;

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Account getAuthor() {
        return author;
    }
}
