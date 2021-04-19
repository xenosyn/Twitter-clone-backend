package socialmedia;

public class EndorsementPost extends Post{
    private int id;
    private Account author;
    private String message;
    private Post post;

    public EndorsementPost(int id, Account author, String message, Post post) {
        this.id = id;
        this.author = author;
        this.message = message;
        this.post=post;
    }

}
