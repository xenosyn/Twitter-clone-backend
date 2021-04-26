package socialmedia;

/**
 * The class that represents endorsements in this social media app.
 *
 * @author Hayden
 * @author Jorel
 * @version 1.0
 */
public class EndorsementPost extends Post{
    private Post post;

    /**
     * This method is used to generate an endorsement with a given post id,
     * author and message along with post which acts as a pointer
     * to the original or comment post being endorsed.
     * @param id id of the endorsement
     * @param author Account details/ passed account object
     * @param message copy of parent post's message
     * @param post original post or comment being endorsed
     */
    public EndorsementPost(int id, Account author, String message, Post post) {
        super(id,author,message);
        this.post=post;
    }

    /**
     * Returns the parent post that this endorsement points to.
     * @return endorsed post
     */
    public Post getPost() {
        return post;
    }
}
