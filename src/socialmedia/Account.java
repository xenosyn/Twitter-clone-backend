package socialmedia;
import java.io.Serializable;

/**
 * The class that represents accounts in this social media app.
 *
 * @author Hayden
 * @author Jorel
 * @version 1.0
 */
public class Account implements Serializable{
    private int id;
    private String handle;
    private String description;

    /**
     * Instantiates an account object.
     *
     * @param id unique id of the account
     * @param handle unique handle that the account can be referenced by
     * @param description description of the account
     */
    public Account(int id, String handle,String description) {
        this.id = id;
        this.handle = handle;
        this.description = description;
    }

    /**
     * Returns id associated with this account.
     * @return account id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the handle associated with this account
     * @return account handle
     */
    public String getHandle() {
        return handle;
    }

    /**
     * Sets the handle of this account.
     * @param handle new handle to be allocated to this account
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Sets the description of this account.
     * @param description new description to be allocated to this account
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * toString override method which provides more detail about the account
     * object.
     * @return account information string
     */
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Handle: " + handle + '\n' +
                "Description: " + description;
}
}
