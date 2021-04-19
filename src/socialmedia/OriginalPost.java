package socialmedia;
import java.util.ArrayList;

public class OriginalPost extends Post {
    private int id;
    private Account author;
    private String message;
    private ArrayList<EndorsementPost> endorsements;
    private ArrayList<CommentPost> comments;

    public OriginalPost(int id, Account author, String message) {
        this.id = id;
        this.author = author;
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public ArrayList<CommentPost> getComments() {
        return comments;
    }

    public ArrayList<EndorsementPost> getEndorsements() {
        return endorsements;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nAccount: " + author.getHandle()
                + "\nNo. endorsements: " + endorsements.size()
                + " | No. comments: " + comments.size() + "\n" + message;
    }
}
