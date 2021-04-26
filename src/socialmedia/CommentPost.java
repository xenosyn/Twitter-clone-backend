package socialmedia;

import java.util.ArrayList;

/**
 * The class that represents comments in this social media app.
 *
 * @author Hayden
 * @author Jorel
 * @version 1.0
 */
public class CommentPost extends Post {
    private Post post;
    private ArrayList<EndorsementPost> endorsements;
    private ArrayList<CommentPost> comments;

    /**
     * This method is used to generate a comment post and create empty lists
     * for endorsements and comments which are directed at it. Also sets post
     * which stores a reference to the parent post of this comment.
     * @param id  id of comment
     * @param author Account details/ passed account object
     * @param message comment string
     * @param post original post or comment being replied to
     */
    public CommentPost(int id, Account author, String message, Post post) {
        super(id, author, message);
        this.post = post;
        endorsements = new ArrayList<EndorsementPost>();
        comments = new ArrayList<CommentPost>();
    }

    /**
     * Getter method for comments list.
     * @return ArrayList of comments
     */
    public ArrayList<CommentPost> getComments() {
        return comments;
    }

    /**
     * Getter method for endorsements list.
     * @return ArrayList of endorsements
     */
    public ArrayList<EndorsementPost> getEndorsements() {
        return endorsements;
    }

    /**
     * This method is used to add comments to the comments ArrayList in this
     * CommentPost.
     * @param commentPost for passing the commentPost object
     */
    public void addComment(CommentPost commentPost) {
        comments.add(commentPost);
    }

    /**
     * This method is used to remove comments from the comments ArrayList in
     * this CommentPost.
     * @param commentPost passes the commentPost object
     */
    public void removeComment(CommentPost commentPost) {
        comments.remove(commentPost);
    }

    /**
     * This method is used to add endorsements to the endorsement ArrayList in
     * this CommentPost.
     * @param endorsementPost passes the EndorsementPost object
     */
    public void addEndorsement(EndorsementPost endorsementPost) {
        endorsements.add(endorsementPost);
    }

    /**
     * This method is used to remove endorsements from the endorsement ArrayList
     * in this CommentPost.
     * @param endorsementPost passes the EndorsementPost object
     */
    public void removeEndorsement(EndorsementPost endorsementPost) {
        endorsements.remove(endorsementPost);
    }

    /**
     * This setter method is used to update post.
     * @param post passes the post object
     */
    public void setPost(Post post) {
        this.post = post;
    }

    /**
     * This getter method gets the post.
     * @return gets the post
     */
    public Post getPost() {
        return post;
    }

    /**
     * toString override method which provides more detail about the comment
     * post object.
     * @return comment post information string
     */
    @Override
    public String toString() {
        return "ID: " + super.getId() + "\nAccount: "
                + super.getAuthor().getHandle()
                + "\nNo. endorsements: " + endorsements.size()
                + " | No. comments: " + comments.size() + "\n"
                + super.getMessage();
    }
}
