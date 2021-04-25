import socialmedia.*;

import java.io.IOException;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the SocialMediaPlatform interface -- note you will
 * want to increase these checks, and run it on your SocialMedia class (not the
 * BadSocialMedia class).
 *
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class SocialMediaPlatformTestApp {

	/**
	 * Test method.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("The system compiled and started the execution...");

		SocialMediaPlatform platform = new SocialMedia();
		initialise(platform);
	}

	public static void initialise(SocialMediaPlatform platform) {
		assert (platform.getNumberOfAccounts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		assert (platform.getTotalOriginalPosts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		assert (platform.getTotalCommentPosts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		assert (platform.getTotalEndorsmentPosts() == 0) : "Innitial SocialMediaPlatform not empty as required.";

		Integer id;
		try {
			id = platform.createAccount("my_handle");
			assert (platform.getNumberOfAccounts() == 1) : "number of accounts registered in the system does not match";

			platform.removeAccount(id);
			assert (platform.getNumberOfAccounts() == 0) : "number of accounts registered in the system does not match";

		} catch (IllegalHandleException e) {
			assert (false) : "IllegalHandleException thrown incorrectly";
		} catch (InvalidHandleException e) {
			assert (false) : "InvalidHandleException thrown incorrectly";
		} catch (AccountIDNotRecognisedException e) {
			assert (false) : "AccountIDNotRecognizedException thrown incorrectly";
		}

		Integer postID;
		Integer accountId;
		Integer account2Id;
		Integer endoID;
		Integer commentID;
		Integer comment2ID;
		try {
			accountId = platform.createAccount("neel");
			postID =  platform.createPost("neel","haha!");
			assert (platform.getTotalOriginalPosts() == 1) : "number of posts registered in the system does not match";
			platform.changeAccountHandle("neel","neil");
			// if throws HandleNotRecognisedException then update to handle not registered correctly
			platform.showAccount("neil");
			endoID= platform.endorsePost("neil",postID);
			assert(platform.getTotalEndorsmentPosts()==1): "number of endorsement posts registered in the system does not match";
			commentID=platform.commentPost("neil",postID," dont laugh : ( ");
			assert(platform.getTotalCommentPosts()==1): "number of comment posts registered in the system does not match";
			platform.deletePost(commentID);
			assert(platform.getTotalCommentPosts()==0): "deletion hasn't registered properly on the system";

			String correctIndividualPostString = "ID: 1\n"
					+ "Account: neil\n"
					+ "No. endorsements: 1 | No. comments: 0\n"
					+ "haha!";
			assert (platform.showIndividualPost(postID).equals(correctIndividualPostString)) : "showIndividualPost returns incorrect string";
			account2Id = platform.createAccount("sarah", "bread");
			platform.updateAccountDescription("sarah","i like bread!");
			String descriptionCheck = "ID: 2\n"
					+ "Handle: sarah\n"
					+ "Description: i like bread!\n"
					+ "Post count: 0\n"
					+ "Endorse count: 0";
			assert(platform.showAccount("sarah").equals(descriptionCheck)):" Error updating account description";

			comment2ID = platform.commentPost("sarah",postID,"french toast is yummy");
			platform.commentPost("neil",comment2ID,"I agree!");

			String correctChildrenPostString = "ID: 1\n"
					+ "Account: neil\n"
					+ "No. endorsements: 1 | No. comments: 1\n"
					+ "haha!\n"
					+ "|\n"
					+ "| > ID: 3\n"
					+ "    Account: sarah\n"
					+ "    No. endorsements: 0 | No. comments: 1\n"
					+ "    french toast is yummy\n"
					+ "    |\n"
					+ "    | > ID: 4\n"
					+ "        Account: neil\n"
					+ "        No. endorsements: 0 | No. comments: 0\n"
					+ "        I agree!\n"
					+ "        |\n"
					+ "        | > ";
			assert(platform.showPostChildrenDetails(postID).toString().equals(correctChildrenPostString)):"showChildrenPostDetails returns incorrect string";

			assert(platform.getMostEndorsedPost() == postID):"most endorsed post is not registered correctly";
			assert(platform.getMostEndorsedAccount() == accountId):"most endorsed account is not registered correctly";
			platform.savePlatform("test.ser");
			platform.erasePlatform();
			assert (platform.getNumberOfAccounts() == 0) : "SocialMediaPlatform not erased";
			assert (platform.getTotalOriginalPosts() == 0) : "SocialMediaPlatform not erased";
			assert (platform.getTotalCommentPosts() == 0) : "SocialMediaPlatform not erased";
			assert (platform.getTotalEndorsmentPosts() == 0) : "SocialMediaPlatform not erased";
			platform.loadPlatform("test.ser");
			assert(platform.getNumberOfAccounts() == 2):"SocialMediaPlatform has not loaded properly";
			System.out.println("Yay!");
		} catch (IllegalHandleException e) {
			assert (false) : "IllegalHandleException thrown incorrectly";
		} catch (InvalidHandleException e) {
			assert (false) : "InvalidHandleException thrown incorrectly";
		}  catch (HandleNotRecognisedException e) {
			assert (false) : "HandleNotRecognisedException thrown incorrectly";
		} catch (InvalidPostException e) {
			assert (false) : "InvalidPostException thrown incorrectly";
		} catch (NotActionablePostException e) {
			assert (false) : "NotActionablePostException  thrown incorrectly";
		} catch (PostIDNotRecognisedException e) {
			assert (false) : "PostIDNotRecognisedException thrown incorrectly";
		} catch (IOException | ClassNotFoundException e) {
			//assert(false):"input format error";
			e.printStackTrace();
		}

	}
	}




