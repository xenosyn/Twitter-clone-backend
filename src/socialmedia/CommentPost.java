package socialmedia;

import java.util.ArrayList;

public class CommentPost extends Post {
    private Post post;
    private ArrayList<EndorsementPost> endorsements;
    private ArrayList<CommentPost> comments;

    /**
     *This method is used to generate comment post and
     *store comments and endorsements of said the post
     * it also acts as a pointer as the orginal post or comment as an input.
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
     *getter method for comments
     * @return arraylist of comments
     */
    public ArrayList<CommentPost> getComments() {
        return comments;
    }

    /**
     *getter method for endorsements
     * @return arraylist of endorsements
     */
    public ArrayList<EndorsementPost> getEndorsements() {
        return endorsements;
    }

    /**
     * This method is used to add comments to the comments arraylist in CommentPost
     * @param commentPost  for passing the commentPost object
     */
    public void addComment(CommentPost commentPost) {
        comments.add(commentPost);
    }

    /**
     *This method is used to remove comments from the comments arraylist in CommentPost
     * @param commentPost passes the commentPost object
     */
    public void removeComment(CommentPost commentPost) {
        comments.remove(commentPost);
    }

    /**
     *This method is used to add endorsements  to the endorsement arraylist in CommentPost
     * @param endorsementPost passes the EndorsementPost object
     */
    public void addEndorsement(EndorsementPost endorsementPost) {
        endorsements.add(endorsementPost);
    }

    /**
     *This method is used to remove endorsements to the endorsement arraylist in CommentPost
     * @param endorsementPost passes the EndorsementPost object
     */
    public void removeEndorsement(EndorsementPost endorsementPost) {
        endorsements.remove(endorsementPost);
    }

    /**
     *This setter method is used to update post
     * @param post passes the post object
     */
    public void setPost(Post post) {
        this.post = post;
    }

    /**
     * This  getter method gets the post
     * @return gets the post
     */
    public Post getPost() {
        return post;
    }

    /**
     *
     * @return the comment as a string to be outputted.
     */
    @Override
    public String toString() {
        return "ID: " + id + "\nAccount: " + author.getHandle()
                + "\nNo. endorsements: " + endorsements.size()
                + " | No. comments: " + comments.size() + "\n" + message;
    }
}
