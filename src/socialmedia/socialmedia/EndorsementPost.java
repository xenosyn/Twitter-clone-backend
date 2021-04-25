package socialmedia;

public class EndorsementPost extends Post{
    private Post post;

    /**
     *This method is used to generate endorsement with given post id ,
     * account and message along with post which acts as a pointer
     * to the orginal post
     * @param id id of the endorsement
     * @param author Account details/ passed account object
     * @param message comment/post string
     * @param post original post or comment being endorsed
     */
    public EndorsementPost(int id, Account author, String message, Post post) {
        super(id,author,message);
        this.post=post;
    }

    /**
     *
     * @return gets the post
     */
    public Post getPost() {
        return post;
    }
}
