package socialmedia;

public class EndorsementPost extends Post{
    private Post post;

    public EndorsementPost(int id, Account author, String message, Post post) {
        super(id,author,message);
        this.post=post;
    }

    public Post getPost() {
        return post;
    }
}
