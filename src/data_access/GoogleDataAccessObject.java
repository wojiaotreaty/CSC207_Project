package data_access;


/**
 * Uses GoogleCalendar API.
 */
public class GoogleDataAccessObject {

    private String userToken;



    /**
     * Checks if credentials are valid. If so, obtains the token associated with this user.
     * NOTE: this part has to do with authorization & authentication and is kind of confusing
     * it'll take a bit to figure out. IT DOES NOT WORK RIGHT NOW.
     * @param gmail
     * @param password
     * @return True, if login successful; False, if gmail or password is incorrect
     */
    public boolean loginWithCredentials(String gmail, String password){
        return true;
    }
}
