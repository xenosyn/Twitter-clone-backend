package socialmedia;
import java.util.ArrayList;

/**
 * The class that represents original (top-level) posts in this social media
 * app.
 *
 * @author Hayden
 * @author Jorel
 * @version 1.0
 */
public class OriginalPost extends Post {
    private ArrayList<EndorsementPost> endorsements;
    private ArrayList<CommentPost> comments;

    /**
     * Instantiates an original post object.
     * @param id unique id of the post
     * @param author account that created the post
     * @param message message body of the post
     */
    public OriginalPost(int id, Account author, String message) {
        super(id, author, message);
        endorsements = new ArrayList<EndorsementPost>();
        comments = new ArrayList<CommentPost>();
    }

    /**
     * Returns the list of comments on this original post.
     * @return comments list
     */
    public ArrayList<CommentPost> getComments() {
        return comments;
    }

    /**
     * Returns the list of endorsements on this original post.
     * @return endorsements list
     */
    public ArrayList<EndorsementPost> getEndorsements() {
        return endorsements;
    }

    /**
     * Adds a comment to the list of comments on this original post.
     * @param commentPost comment to be added
     */
    public void addComment(CommentPost commentPost) {
        comments.add(commentPost);
    }

    /**
     * Removes a comment from the list of comments on this original post.
     * @param commentPost comment to be removed
     */
    public void removeComment(CommentPost commentPost) {
        comments.remove(commentPost);
    }

    /**
     * Adds an endorsement to the list of endorsements on this original post.
     * @param endorsementPost endorsement to be added
     */
    public void addEndorsement(EndorsementPost endorsementPost) {
        endorsements.add(endorsementPost);
    }

    /**
     * Removes an endorsement from the list of endorsements on this original
     * post.
     * @param endorsementPost endorsement to be removed
     */
    public void removeEndorsement(EndorsementPost endorsementPost) {
        endorsements.remove(endorsementPost);
    }

    /**
     * toString override method which provides more detail about the original
     * post object.
     * @return original post information string
     */
    @Override
    public String toString() {
        return "ID: " + id + "\nAccount: " + author.getHandle()
                + "\nNo. endorsements: " + endorsements.size()
                + " | No. comments: " + comments.size() + "\n" + message;
    }
}
