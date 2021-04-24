package socialmedia;
import java.io.Serializable;

public abstract class Post implements Serializable {
    // TODO - make private and use getters and setters
    int id;
    Account author;
    String message;

    Post(int id, Account author, String message) {
        this.id = id;
        this.author = author;
        this.message = message;
    }

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
