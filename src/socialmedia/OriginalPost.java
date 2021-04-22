package socialmedia;
import java.util.ArrayList;

public class OriginalPost extends Post {
    private ArrayList<EndorsementPost> endorsements;
    private ArrayList<CommentPost> comments;

    public OriginalPost(int id, Account author, String message) {
        super(id, author, message);
        endorsements = new ArrayList<EndorsementPost>();
        comments = new ArrayList<CommentPost>();
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
