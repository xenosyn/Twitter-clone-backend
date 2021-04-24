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
 * Main class for this social media platform that implements most of the
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

    public SocialMedia(){
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
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        checkInvalidHandle(handle);
        checkIllegalHandle(handle);
        int id;
        id=idGenerator("a");
        accounts.add(new Account(id, handle, ""));
        return id;
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
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

    public void checkAccountIdExists(int id) throws AccountIDNotRecognisedException {
        if(getAccountById(id) == null){
            throw new AccountIDNotRecognisedException("No account exists with"
                    + " specified id.");
        }
    }

    public void checkHandleExists(String handle) throws HandleNotRecognisedException {
        if(getAccountByHandle(handle)==null){
            throw new HandleNotRecognisedException("No account exists with"
                    + " specified handle.");
        }
    }

    public void checkIllegalHandle(String handle) throws IllegalHandleException{
        if(getAccountByHandle(handle) != null){
            throw new IllegalHandleException("Handle already exists!");
        }
    }

    public void checkInvalidHandle(String handle) throws InvalidHandleException{
        if(handle.length()>30 || handle.length()<1 || handle.contains(" ")){
            throw new InvalidHandleException("Invalid handle! Handle must"
                    + " contain between 1 and 30 characters and not have white"
                    + " spaces!");
        }
    }

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        checkHandleExists(oldHandle);
        checkInvalidHandle(newHandle);
        checkIllegalHandle(newHandle);

        getAccountByHandle(oldHandle).setHandle(newHandle);
    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        checkHandleExists(handle);
        getAccountByHandle(handle).setDescription(description);
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
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

    public void checkPostIsValid(String message) throws InvalidPostException {
        if (message.length() < 1 || message.length() > 100) {
            throw new InvalidPostException("Post message must be between 1 and"
                    + " 100 characters!");
        }
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        checkHandleExists(handle);
        checkPostIsValid(message);
        int id;
        id = idGenerator("p");
        posts.add(new OriginalPost(id, getAccountByHandle(handle), message));
        return id;
    }

    public Post postFinder(int id){
        Post post = null;
        for (Post pst:posts ){
            if(pst.getId()==id){
                post=pst;
            }
        }
        return post;
    }

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
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
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

    public void checkPostIsActionable(Post post)
            throws NotActionablePostException {
        if (post instanceof EndorsementPost) {
            System.out.println(post.getClass());
            throw new NotActionablePostException("Endorsement posts cannot be"
                    + " endorsed or commented on.");
        }
    }

    /**
     * The method checks if the post passed as input contains info and is not null.
     * @param post - passes post Object.
     * @throws PostIDNotRecognisedException - checks if post is null , if true throws exception
     */
    public void checkPostExists(Post post) throws PostIDNotRecognisedException {
        if (post == null) {
            throw new PostIDNotRecognisedException("No post exists with"
                    + " specified ID.");
        }
    }

    /**
     * The method generates a comment with the given handle and message to a post by the id that is passed as input
     * @param handle  of the account commenting a post.
     * @param id      of the post being commented.
     * @param message the comment post message.
     * @return the ID of the comment created.
     * @throws HandleNotRecognisedException  checks the handle passed has an account id , throws exception if false
     * @throws PostIDNotRecognisedException   checks if post is null , if true throws exception
     * @throws NotActionablePostException  checks if post is a comment or original post to be able to comment
     * @throws InvalidPostException  checks if post contains a message or if message is too big
     */
    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
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

    /**
     *This method removes the post from
     * @param id ID of post to be removed.
     * @throws PostIDNotRecognisedException  checks if post is null , if true throws exception
     */
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
            ArrayList<EndorsementPost> endorsements = commentPost.getEndorsements();
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
     *
     * @param commentPost
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
     *
     * @param endorsementPost
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

    /**
     *
     * @param id of the post to be shown.
     * @return
     * @throws PostIDNotRecognisedException
     */
    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        Post post = postFinder(id);
        checkPostExists(post);
        return post.toString();
    }

    /**
     *
     * @param id of the post to be shown.
     * @return
     * @throws PostIDNotRecognisedException
     * @throws NotActionablePostException
     */
    @Override
    public StringBuilder showPostChildrenDetails(int id)
            throws PostIDNotRecognisedException, NotActionablePostException {
        Post post = postFinder(id);
        checkPostExists(post);
        checkPostIsActionable(post);

        StringBuilder init = new StringBuilder();
        appendChildrenRecursively(post,init,0);
        return init;
    }

    /**
     *
     * @param post
     * @param sb
     * @param depth
     */
    public void appendChildrenRecursively(Post post, StringBuilder sb, int depth) {
        if (post instanceof OriginalPost) {
            OriginalPost originalPost = (OriginalPost) post;
            ArrayList<CommentPost> comments = originalPost.getComments();

            // Append current post string
            sb.append(post.toString() + "\n|\n| > ");

            for (CommentPost o : comments) {
                appendChildrenRecursively(o,sb,depth + 1);
            }

        } else if (post instanceof CommentPost) {
            CommentPost commentPost = (CommentPost) post;
            ArrayList<CommentPost> comments = commentPost.getComments();

            // Append current post string
            String currentInfo = post.toString() + "\n|\n| > ";
            // Get indent
            String indent = " ".repeat(depth*4);

            // Create Regex Pattern
            Pattern pattern = Pattern.compile("\n");
            // Get matcher object from pattern
            Matcher matcher = pattern.matcher(currentInfo);
            // Replace newline with indented newline
            sb.append(matcher.replaceAll("\n" + indent));

            for (CommentPost c : comments) {
                appendChildrenRecursively(c,sb,depth+1);
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public int getNumberOfAccounts() {
        return accounts.size();
    }

    /**
     *
     * @return
     */
    @Override
    public int getTotalOriginalPosts() {
        return postCount(OriginalPost.class);
    }

    /**
     *
     * @return
     */
    @Override
    public int getTotalEndorsmentPosts() {
        return postCount(EndorsementPost.class);
    }

    /**
     *
     * @return
     */
    @Override
    public int getTotalCommentPosts() {
        return postCount(CommentPost.class);
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
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

    /**
     *
     */
    @Override
    public void erasePlatform() {
        accounts.clear();
        posts.clear();
        genPost.getComments().clear();
    }


    /**
     *
     * @param filename location of the file to be saved
     * @throws IOException
     */
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

    /**
     *
     * @param filename location of the file to be loaded
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
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
