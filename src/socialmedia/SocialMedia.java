package socialmedia;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class for this social media app that implements most of the
 * SocialMediaPlatform interface's methods.
 *
 * @author Hayden
 * @author Jorel
 * @version 1.0
 */
public class SocialMedia implements SocialMediaPlatform {

    ArrayList<Account> accounts;
    ArrayList<Post> posts;
    // Generic post for comments of deleted posts to be linked to
    GenericPost genPost;

    /**
     * Instantiates a social media platform object that stores accounts, posts
     * and a generic post which holds all comments that originally pointed to
     * posts that have since been deleted.
     */
    public SocialMedia() {
        accounts = new ArrayList<>();
        posts = new ArrayList<>();
        genPost = new GenericPost();
    }

    /**
     * Generates an id which is 1 higher than the existing highest one.
     * @param option option to choose either accounts (a) or posts (p)
     * @return the new id
     */
    public int idGenerator(String option) {
        int maxId = 0;
        switch (option) {
            case "a":
                for (Account acc : accounts) {
                    if (maxId < acc.getId()) {
                        maxId = acc.getId();
                    }
                }
                break;
            case "p":
                for (Post post : posts) {
                    if (maxId < post.getId()) {
                        maxId = post.getId();
                    }
                }
                break;
        }
        return ++maxId;
    }

    @Override
    public int createAccount(String handle)
            throws IllegalHandleException,InvalidHandleException {
        checkInvalidHandle(handle);
        checkIllegalHandle(handle);
        int id;
        id=idGenerator("a");
        accounts.add(new Account(id, handle, ""));
        return id;
    }

    @Override
    public int createAccount(String handle, String description)
            throws IllegalHandleException, InvalidHandleException {
        checkInvalidHandle(handle);
        checkIllegalHandle(handle);
        int id;
        id=idGenerator("a");
        accounts.add(new Account(id, handle, description));
        return id;
    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        checkAccountIdExists(id);
        removeAccount(getAccountById(id));
    }

    @Override
    public void removeAccount(String handle)
            throws HandleNotRecognisedException {
        checkHandleExists(handle);
        removeAccount(getAccountByHandle(handle));
    }

    /**
     * Removes an account from the system along with any of it's posts.
     * @param account account to remove
     */
    public void removeAccount(Account account) {
        for (Post post : posts) {
            if (post.getAuthor().equals(account)) {
                try {
                    deletePost(post.getId());
                } catch (PostIDNotRecognisedException e) {
                    // IGNORE
                }
            }
        }
        accounts.remove(account);
    }

    /**
     * Checks if an account exists with the passed id and throws an exception
     * if not.
     * @param id id to be searched for in the system
     * @throws AccountIDNotRecognisedException thrown if no account could be
     *                                         found with the passed id
     */
    public void checkAccountIdExists(int id)
            throws AccountIDNotRecognisedException {
        if(getAccountById(id) == null){
            throw new AccountIDNotRecognisedException("No account exists with"
                    + " specified id.");
        }
    }

    /**
     * Checks if an account exists with the passed handle and throws an
     * exception if not.
     * @param handle handle to be searched for in the system
     * @throws HandleNotRecognisedException thrown if no account could be found
     *                                      found with the passed handle
     */
    public void checkHandleExists(String handle)
            throws HandleNotRecognisedException {
        if(getAccountByHandle(handle)==null){
            throw new HandleNotRecognisedException("No account exists with"
                    + " specified handle.");
        }
    }

    /**
     * Checks if the passed handle already belongs to an account in the system
     * and throws an exception if it does.
     * @param handle handle to be searched for in the system
     * @throws IllegalHandleException thrown if an account already has the
     *                                passed handle
     */
    public void checkIllegalHandle(String handle) throws IllegalHandleException{
        if(getAccountByHandle(handle) != null){
            throw new IllegalHandleException("Handle already exists!");
        }
    }

    /**
     * Checks if the passed handle conforms to the following rules for handles
     * in this social media system: must not be empty or have more than 30
     * characters and must not have white spaces.
     * @param handle handle to be checked
     * @throws InvalidHandleException thrown if the handle does not conform to
     *                                the specified rules
     */
    public void checkInvalidHandle(String handle) throws InvalidHandleException{
        if(handle.length()>30 || handle.length()<1 || handle.contains(" ")){
            throw new InvalidHandleException("Invalid handle! Handle must"
                    + " contain between 1 and 30 characters and not have white"
                    + " spaces!");
        }
    }

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException,
            IllegalHandleException, InvalidHandleException {
        checkHandleExists(oldHandle);
        checkInvalidHandle(newHandle);
        checkIllegalHandle(newHandle);

        getAccountByHandle(oldHandle).setHandle(newHandle);
    }

    @Override
    public void updateAccountDescription(String handle, String description)
            throws HandleNotRecognisedException {
        checkHandleExists(handle);
        getAccountByHandle(handle).setDescription(description);
    }

    @Override
    public String showAccount(String handle)
            throws HandleNotRecognisedException {
        checkHandleExists(handle);
        Account showAcc = getAccountByHandle(handle);

        // Find post count and endorsement count
        int postCount = 0;
        int endorseCount = 0;
        for (Post pst : posts) {
            if (pst.getAuthor().equals(showAcc)) {
                postCount++;
                if (pst instanceof OriginalPost) {
                    OriginalPost op = (OriginalPost) pst;
                    endorseCount += op.getEndorsements().size();
                } else if (pst instanceof CommentPost) {
                    CommentPost cp = (CommentPost) pst;
                    endorseCount += cp.getEndorsements().size();
                }
            }
        }

        // Make strAppend
        String strAppend = "\nPost count: " + postCount + "\nEndorse count: "
                + endorseCount;

        // Append strAppend
        return getAccountByHandle(handle).toString() + strAppend;
    }

    /**
     * Returns the account object that matches the specified handle or null if
     * none match it.
     * @param handle handle of account to be found
     * @return found account object or null if none exist with the passed handle
     */
    public Account getAccountByHandle(String handle) {
        Account account = null;
        for (Account acc : accounts) {
            if (acc.getHandle().equals(handle)) {
                account = acc;
                break;
            }

        }
        return account;
    }

    /**
     * Returns the account object that matches the specified id or null if none
     * match it.
     * @param id id of account to be found
     * @return found account object or null if none exist with the passed id
     */
    public Account getAccountById(int id) {
        Account account = null;
        for (Account acc : accounts) {
            if (acc.getId() == id) {
                account = acc;
                break;
            }

        }
        return account;
    }

    /**
     * Checks if the passed post message conforms to the following rules for
     * posts in this social media system: must not be empty or contain more than
     * 100 characters.
     * @param message post message to be checked
     * @throws InvalidPostException thrown if the post message does not conform
     *                              to the specified rules
     */
    public void checkPostIsValid(String message) throws InvalidPostException {
        if (message.length() < 1 || message.length() > 100) {
            throw new InvalidPostException("Post message must be between 1 and"
                    + " 100 characters!");
        }
    }

    @Override
    public int createPost(String handle, String message)
            throws HandleNotRecognisedException, InvalidPostException {
        checkHandleExists(handle);
        checkPostIsValid(message);
        int id;
        id = idGenerator("p");
        posts.add(new OriginalPost(id, getAccountByHandle(handle), message));
        return id;
    }

    /**
     * Returns the post object associated with the passed id or null if none
     * match it.
     * @param id id of post to be found
     * @return found post object or null if none exist with the passed id
     */
    public Post postFinder(int id){
        Post post = null;
        for (Post pst:posts ){
            if(pst.getId()==id){
                post=pst;
            }
        }
        return post;
    }

    /**
     * Counts how many posts there are which are instances of the passed post
     * class in the system.
     * @param input post class to be counted
     * @return amount of posts of the specified class in the system
     */
    public int postCount(Class<?> input) {
        int c = 0;
        for (Post pst : posts) {
            if (input.isInstance(pst)) {
                c++;
            }
        }
        return c;
    }


    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException {
        checkHandleExists(handle);

        Post post = postFinder(id);
        checkPostExists(post);
        checkPostIsActionable(post);



        int endoId = idGenerator("p");
        String message = "EP@" + handle + ": " + post.getMessage();
        Account account = getAccountByHandle(handle);
        EndorsementPost newEndorsement = new EndorsementPost(endoId,
                account, message , post);

        if (post instanceof OriginalPost) {
            OriginalPost op = (OriginalPost) post;
            for (EndorsementPost ep: op.getEndorsements()) {
                if(ep.getAuthor().equals(account)) {
                    return ep.getId();
                }
            }
            op.addEndorsement(newEndorsement);
        } else if (post instanceof CommentPost) {
            CommentPost cp = (CommentPost) post;
            for (EndorsementPost ep: cp.getEndorsements()) {
                if (ep.getAuthor().equals(account)) {
                    return ep.getId();
                }
            }
            cp.addEndorsement(newEndorsement);
        }

        posts.add(newEndorsement);

        return endoId;
    }

    /**
     * Checks if the post object passes as input is able to be endorsed and
     * commented on. This is not true if it is an endorsement post.
     * @param post post object
     * @throws NotActionablePostException thrown if the passed post object is an
     *                                    endorsement post
     */
    public void checkPostIsActionable(Post post)
            throws NotActionablePostException {
        if (post instanceof EndorsementPost) {
            throw new NotActionablePostException("Endorsement posts cannot be"
                    + " endorsed or commented on.");
        }
    }

    /**
     * The method checks if the post object passed as input contains info and is
     * not null.
     * @param post - passes post Object.
     * @throws PostIDNotRecognisedException - checks if post is null, if true
     *                                        throws exception
     */
    public void checkPostExists(Post post) throws PostIDNotRecognisedException {
        if (post == null) {
            throw new PostIDNotRecognisedException("No post exists with"
                    + " specified ID.");
        }
    }


    @Override
    public int commentPost(String handle, int id, String message)
            throws HandleNotRecognisedException,
            PostIDNotRecognisedException,
            NotActionablePostException, InvalidPostException {
        checkHandleExists(handle);

        Post post = postFinder(id);

        checkPostExists(post);
        checkPostIsActionable(post);
        checkPostIsValid(message);

        int commId = idGenerator("p");
        CommentPost newComment = new CommentPost(commId,
                getAccountByHandle(handle), message, post);

        posts.add(newComment);

        if (post instanceof OriginalPost) {
            OriginalPost op = (OriginalPost) post;
            op.addComment(newComment);
        } else if (post instanceof CommentPost) {
            CommentPost cp = (CommentPost) post;
            cp.addComment(newComment);
        }
        return commId;
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        Post post = postFinder(id);
        checkPostExists(post);
        if (post instanceof OriginalPost) {
            OriginalPost originalPost = (OriginalPost) post;
            ArrayList<EndorsementPost> endorsements
                    = originalPost.getEndorsements();
            ArrayList<CommentPost> comments = originalPost.getComments();
            for ( CommentPost o: comments){
                o.setPost(genPost);
            }
            ArrayList<CommentPost> genPostComments = genPost.getComments();
            genPostComments.addAll(comments);
            posts.removeAll(endorsements);

        } else if (post instanceof CommentPost) {
            CommentPost commentPost = (CommentPost) post;
            ArrayList<EndorsementPost> endorsements
                    = commentPost.getEndorsements();
            ArrayList<CommentPost> comments = commentPost.getComments();
            for (CommentPost c : comments) {
                c.setPost(genPost);
            }
            ArrayList<CommentPost> genPostComments = genPost.getComments();
            genPostComments.addAll(comments);
            posts.removeAll(endorsements);

            removeCommentFromParent(commentPost);
        } else if (post instanceof EndorsementPost) {
            EndorsementPost endorsementPost = (EndorsementPost) post;
            removeEndorsementFromParent(endorsementPost);
        }
        posts.remove(post);
    }

    /**
     * This method uses the input of comment post and removes it from it's
     * parent post's comment ArrayList.
     * @param commentPost comment post object
     */
    public void removeCommentFromParent(CommentPost commentPost) {
        Post parent = commentPost.getPost();
        if (parent instanceof OriginalPost) {
            OriginalPost op = (OriginalPost) parent;
            op.removeComment(commentPost);
        } else if (parent instanceof CommentPost) {
            CommentPost cp = (CommentPost) parent;
            cp.removeComment(commentPost);
        }
    }

    /**
     * This method uses the input of endorsement post and removes it from it's
     * parent post's endorsement ArrayList.
     * @param endorsementPost endorsement post object
     */
    public void removeEndorsementFromParent(EndorsementPost endorsementPost) {
        Post parent = endorsementPost.getPost();
        if (parent instanceof OriginalPost) {
            OriginalPost op = (OriginalPost) parent;
            op.removeEndorsement(endorsementPost);
        } else if (parent instanceof CommentPost) {
            CommentPost cp = (CommentPost) parent;
            cp.removeEndorsement(endorsementPost);
        }
    }

    @Override
    public String showIndividualPost(int id)
            throws PostIDNotRecognisedException {
        Post post = postFinder(id);
        checkPostExists(post);
        return post.toString();
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id)
            throws PostIDNotRecognisedException, NotActionablePostException {
        Post post = postFinder(id);
        checkPostExists(post);
        checkPostIsActionable(post);

        StringBuilder init = new StringBuilder();
        appendChildrenRecursively(post,init,0,false);
        return init;
    }

    /**
     * This method recursively generates a StringBuilder containing a thread of
     * comments on a post or comment with distinct indentation for each level by
     * taking the comment/original post as input and going through the comments
     * array, appending each comment and it's children to the StringBuilder.
     * @param post passes post object
     * @param sb StringBuilder that the thread is built within
     * @param depth used to calculate spacing and format comments on posts
     *              and comments on comments recursively
     */
    public void appendChildrenRecursively(Post post, StringBuilder sb,
                                          int depth, boolean firstComment) {
        if (post instanceof OriginalPost) {
            OriginalPost originalPost = (OriginalPost) post;
            ArrayList<CommentPost> comments = originalPost.getComments();

            // Append current post string
            sb.append(post);

            boolean first = true;
            for (CommentPost o : comments) {
                appendChildrenRecursively(o,sb,depth + 1, first);
                first = false;
            }

        } else if (post instanceof CommentPost) {
            // Get indent
            String indent = " ".repeat(depth*4);
            String firstIndent = " ".repeat((depth*4) - 4);

            if (sb.charAt(sb.length() - 1) == '\n') {
                sb.append(indent);
            }

            CommentPost commentPost = (CommentPost) post;
            ArrayList<CommentPost> comments = commentPost.getComments();

            // Append current post string
            String currentInfo = post.toString();

            // Create Regex Pattern
            Pattern pattern = Pattern.compile("\n");
            // Get matcher object from pattern
            Matcher matcher = pattern.matcher(currentInfo);
            // Replace newline with indented newline
            currentInfo = matcher.replaceAll("\n" + indent);

            if (firstComment) {
                currentInfo = "\n" + firstIndent + "|\n"
                        + firstIndent + "| > " + currentInfo;
            } else {
                currentInfo = "\n\n" + firstIndent + "| > " + currentInfo;
            }

            sb.append(currentInfo);

            boolean first = true;
            for (CommentPost c : comments) {
                appendChildrenRecursively(c,sb,depth+1,first);
                first = false;
            }
        }
    }

    @Override
    public int getNumberOfAccounts() {
        return accounts.size();
    }


    @Override
    public int getTotalOriginalPosts() {
        return postCount(OriginalPost.class);
    }


    @Override
    public int getTotalEndorsmentPosts() {
        return postCount(EndorsementPost.class);
    }

    @Override
    public int getTotalCommentPosts() {
        return postCount(CommentPost.class);
    }

    @Override
    public int getMostEndorsedPost() {
        int id = -1;
        int cMax=-1;
        for (Post pst : posts){
            if (pst instanceof OriginalPost){
                OriginalPost op = (OriginalPost) pst;
                if(cMax < op.getEndorsements().size()){
                    cMax=op.getEndorsements().size();
                    id = op.getId() ;
                }
            } else if (pst instanceof CommentPost){
                CommentPost cp = (CommentPost) pst;
                if(cMax < cp.getEndorsements().size()){
                    cMax=cp.getEndorsements().size();
                    id = cp.getId() ;
                }
            }
        }
        return id;
    }

    @Override
    public int getMostEndorsedAccount() {
        LinkedHashMap<Account, Integer> endorsedAccounts
                = new LinkedHashMap<>();

        for (Post pst : posts) {
            if (pst instanceof EndorsementPost) {
                endorsedAccounts.merge(pst.getAuthor(), 1, Integer::sum);
            }
        }

        int id= -1;
        int maxEndorseAcc = -1;
        for (Entry<Account, Integer> entry : endorsedAccounts.entrySet()) {
            if (maxEndorseAcc < entry.getValue()) {
                maxEndorseAcc = entry.getValue();
                id = entry.getKey().getId();
            }
        }
        return id;
    }

    @Override
    public void erasePlatform() {
        accounts.clear();
        posts.clear();
        genPost.getComments().clear();
    }

    @Override
    public void savePlatform(String filename) throws IOException {
        File file = new File(filename);

        try (ObjectOutputStream out
                     = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(file)))) {
            // Serialise accounts list
            out.writeObject(accounts);
            // Serialise posts list
            out.writeObject(posts);
            // Serialise genPost
            out.writeObject(genPost);
        }
    }

    @Override
    public void loadPlatform(String filename)
            throws IOException, ClassNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new IOException("No file exists with the given pathname!");
        }

        ArrayList<Post> posts;
        ArrayList<Account> accounts;
        GenericPost genPost;

        try (ObjectInputStream in
                     = new ObjectInputStream(new BufferedInputStream(
                new FileInputStream(file)))) {
            // Deserialise accounts list
            Object object = in.readObject();
            if (object instanceof ArrayList) {
                accounts = (ArrayList<Account>) object;
            } else {
                throw new ClassNotFoundException("The loaded object is not an"
                        + " ArrayList!");
            }

            // Deserialise posts list
            object = in.readObject();
            if (object instanceof ArrayList) {
                posts = (ArrayList<Post>) object;
            } else {
                throw new ClassNotFoundException("The loaded object is not an"
                        + " ArrayList!");
            }

            // Deserialise genPost
            object = in.readObject();
            if (object instanceof GenericPost) {
                genPost = (GenericPost) object;
            } else {
                throw new ClassNotFoundException("The loaded object is not a"
                        + " GenericPost!");
            }

            // Load platform
            this.accounts = accounts;
            this.posts = posts;
            this.genPost = genPost;
        } catch (ClassCastException e) {
            throw new ClassNotFoundException("The input file is not valid!", e);
        }
    }

}
