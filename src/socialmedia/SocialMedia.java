package socialmedia;
import java.io.IOException;
import java.util.ArrayList;

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

	}

	@Override
	public void removeAccount(String handle) throws HandleNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
		// TODO Auto-generated method stub

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
			// TODO - NOW REMOVE ENDORSEMENTS AND COMMENTS
		} else if (post instanceof CommentPost) {
			// TODO - THIS IS COMMENT
		} else {
			// TODO - THIS IS AN ENDORSEMENT POST!!!!! (╯°□°）╯︵ ┻━┻
			//  I've now calmed down, I think perhaps you can remove
			//  endorsement posts, it just won't look for comments or
			//  endorsements.
		}
		// TODO - delete post endorsements and redirect comments to genPost

		posts.remove(post);
	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		Post post = postFinder(id);
		return post.toString();
	}

	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

}
