package API;

import java.util.ArrayList;

public class UserReg {
    /* Classes */
    private static UserReg userReg;
    private ArrayList<User> theUsers = new ArrayList<>();

    /* Constructor */
    private UserReg(){
        //All users lost when program is terminated. Perhaps export to CSV (or similar) upon exit and load in again via constructor upon restart.
    }

    /* Singleton */
    public static UserReg getUserReg()
    {
        if(userReg == null)
        {
            userReg = new UserReg();
        }
        return userReg;
    }

    /* See if the user exists already */
    public boolean isAUser(String theUser,String thePassword){
        /* If size is 0 then there are no users and therefore no match */
        if (theUsers.size()>0){
            /* Cycle through and check for a match */
            for (int i = 0; i<theUsers.size();i++){
                /* Compare index to passed data */
                if (theUsers.get(i).getUserName().equals(theUser) && theUsers.get(i).getUserPassword().equals(thePassword)){
                    /* The user exists */
                    return true;
                }
            }
        }
        /* The user does't exist */
        return false;
    }

    /* Get user index */
    public Integer getIndex(String theUser, String thePassword) {
        /* If size is 0 then there are no users and therefore no match */
        if (theUsers.size() > 0) {
            /* Cycle through and check for a match */
            for (int i = 0; i < theUsers.size(); i++) {
                /* Compare index to passed data */
                if (theUsers.get(i).getUserName().equals(theUser)  && theUsers.get(i).getUserPassword().equals(thePassword)) {
                    /* Return the index */
                    return i;
                }
            }
        }
        return null;
    }

    /* Add a new user */
    public void addAUser(String theUser,String thePassword) {
        theUsers.add(new User(theUser,thePassword));
    }

    /* Encapsulate */
    public ArrayList<User> getTheUsers() {
        return theUsers;
    }
}
