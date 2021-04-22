package socialmedia;

import java.util.ArrayList;

public class GenericPost extends Post{
    private ArrayList<CommentPost> comments;

    public GenericPost() {
        super(0, null, "The original content was removed"
                + " from the system and is no longer available.");
        comments = new ArrayList<CommentPost>();
    }

    public ArrayList<CommentPost> getComments(){
        return comments;
    }
}
