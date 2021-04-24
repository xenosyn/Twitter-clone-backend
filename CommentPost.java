package socialmedia;

import java.util.ArrayList;

public class CommentPost extends Post {
    private Post post;
    private ArrayList<EndorsementPost> endorsements;
    private ArrayList<CommentPost> comments;

    public CommentPost(int id, Account author, String message, Post post) {
        super(id, author, message);
        this.post = post;
        endorsements = new ArrayList<EndorsementPost>();
        comments = new ArrayList<CommentPost>();
    }

    public ArrayList<CommentPost> getComments() {
        return comments;
    }

    public ArrayList<EndorsementPost> getEndorsements() {
        return endorsements;
    }

    public void addComment(CommentPost commentPost) {
        comments.add(commentPost);
    }

    public void removeComment(CommentPost commentPost) {
        comments.remove(commentPost);
    }

    public void addEndorsement(EndorsementPost endorsementPost) {
        endorsements.add(endorsementPost);
    }

    public void removeEndorsement(EndorsementPost endorsementPost) {
        endorsements.remove(endorsementPost);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nAccount: " + author.getHandle()
                + "\nNo. endorsements: " + endorsements.size()
                + " | No. comments: " + comments.size() + "\n" + message;
    }
}
