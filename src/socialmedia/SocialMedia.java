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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WRITE STUFF HERE
 * 
 * @author
 * @version 1.0
 */
public class SocialMedia implements SocialMediaPlatform {


	ArrayList<Account> accounts = new ArrayList();
	ArrayList<Post> posts = new ArrayList();
	// Generic post for comments of deleted posts to be linked to
	GenericPost genPost = new GenericPost();

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
		return maxId++;
	}

	@Override
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		int id;
		id=idGenerator("a");
		accounts.add(new Account(id, handle, ""));
		return id;
	}

	@Override
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		int id;
		id=idGenerator("a");
		accounts.add(new Account(id, handle, description));
		return id;
	}

	@Override
	public void removeAccount(int id) throws AccountIDNotRecognisedException {
		// TODO Auto-generated method stub
		removeAccount(getAccountById(id));
	}

	@Override
	public void removeAccount(String handle)
			throws HandleNotRecognisedException {
		// TODO Auto-generated method stub
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

	@Override
	public void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		// TODO Auto-generated method stub
		// TODO - check no one has newHandle already
		getAccountByHandle(oldHandle).setHandle(newHandle);
	}

	@Override
	public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
		// TODO Auto-generated method stub
		getAccountByHandle(handle).setDescription(description);
	}

	@Override
	public String showAccount(String handle) throws HandleNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
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

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
		int endoId;
		Post post;
		endoId = idGenerator("p");
		post = postFinder(id);
		String message = "EP@" + handle + ": " + post.getMessage();
		posts.add(new EndorsementPost(endoId, getAccountByHandle(handle),
				message , post));
		return endoId;
	}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
		int commId = idGenerator("p");
		Post post = postFinder(id);
		posts.add(new CommentPost(commId, getAccountByHandle(handle), message,
				post));
		return commId;
	}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		Post post = postFinder(id);
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
		}
		posts.remove(post);
	}


	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		// TODO - exception
		Post post = postFinder(id);
		return post.toString();
	}

	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		// TODO - exceptions
		Post post = postFinder(id);

		// Check if post is endorsement
		if (post instanceof EndorsementPost) {
			throw new NotActionablePostException(); // TODO - add text
		}

		StringBuilder init = new StringBuilder();
		appendChildrenRecursively(post,init,0);
		return init;
	}

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

	@Override
	public int getNumberOfAccounts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalOriginalPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalEndorsmentPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalCommentPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMostEndorsedPost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMostEndorsedAccount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void erasePlatform() {
		// TODO Auto-generated method stub

	}

	@Override
	public void savePlatform(String filename) throws IOException {
		// TODO - check filename and file
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
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		// TODO - check filename and file
		File file = new File(filename);
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
