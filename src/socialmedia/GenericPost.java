package socialmedia;

import java.util.ArrayList;

public class GenericPost extends Post{
    private int id;
    private ArrayList<CommentPost> comments;
    private final String message = "The original content was removed"
            + " from the system and is no longer available.";

    public GenericPost() {
        id = 0;
    }

    public int getId() {
        return id;
    }
}
