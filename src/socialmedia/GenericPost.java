package socialmedia;

import java.util.ArrayList;

/**
 * The class that represents a generic post in this social media app that
 * comments can be redirected to if their parent posts have been deleted.
 *
 * @author Hayden
 * @author Jorel
 * @version 1.0
 */
public class GenericPost extends Post{
    private ArrayList<CommentPost> comments;

    /**
     * Instantiates a generic post object with an id of 0, a default message and
     * an ArrayList to hold comments whose original parent posts have been
     * deleted.
     */
    public GenericPost() {
        super(0, null, "The original content was removed"
                + " from the system and is no longer available.");
        comments = new ArrayList<CommentPost>();
    }

    /**
     * Returns the list of comments whose original parent posts have been
     * deleted.
     * @return list of comments which have no parent post
     */
    public ArrayList<CommentPost> getComments() {
        return comments;
    }
}
