package sa_b_2.coms309.dungeonadventure.user;


import java.io.IOException;
import java.util.List;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.ScreenMessage;

/**
 * User object used to hold the current user's username and friend's list
 */
public class User {
    private final String username;
    private List<Friend> friends;

    public User(String username, List<Friend> friends) {
        this.username = username;
        this.friends = friends;
    }

    /**
     * Logs the user out and displays a message when done
     */
    public static void logout() {
        Constants.currentUser = null;
        Constants.autoLoggedIn = false;
        Constants.screenMessages.add(new ScreenMessage("You were logged out."));
        try {
            Constants.fileOutputStream.write("".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends){
        this.friends = friends;
    }
}
