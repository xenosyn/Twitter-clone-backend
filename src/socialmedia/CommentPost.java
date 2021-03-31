package socialmedia;

import java.util.ArrayList;

public class CommentPost extends Post {
    private int id;
    private Account author;
    private String message;
    private Post post;
    private ArrayList<EndorsementPost> endorsements;
    private ArrayList<CommentPost> comments;

    public CommentPost(int id, Account author, String message, Post post) {
        this.id = id;
        this.author = author;
        this.message = message;
        this.post = post;
    }

    public int getId() {
        return id;
    }
}
