package socialmedia;
import java.io.Serializable;

/**
 * The class that represents all types of posts in this social media app.
 *
 * @author Hayden
 * @author Jorel
 * @version 1.0
 */
public abstract class Post implements Serializable {
    // TODO - make private and use getters and setters
    int id;
    Account author;
    String message;

    /**
     * Aids in instantiating a child of this class by assigning values for id,
     * author and message.
     * @param id id of the post
     * @param author author of the post
     * @param message message body of the post
     */
    Post(int id, Account author, String message) {
        this.id = id;
        this.author = author;
        this.message = message;
    }

    /**
     * Returns the id of the post object.
     * @return post id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the message body of the post object.
     * @return post message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the author of the post.
     * @return post author
     */
    public Account getAuthor() {
        return author;
    }
}
