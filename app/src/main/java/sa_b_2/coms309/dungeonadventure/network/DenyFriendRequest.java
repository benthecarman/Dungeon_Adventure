package sa_b_2.coms309.dungeonadventure.network;


import android.os.AsyncTask;

import java.util.HashMap;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.ScreenMessage;

/**
 * Denies a friend request from the given username
 */
public class DenyFriendRequest {
    /**
     * Denies a Friend Request from the given username
     *
     * @param username username of friend request to deny
     * @return if denying was successful
     */
    public static boolean denyFriend(String username) {

        new AsyncFriends().execute(username);

        return true;
    }

    private static class AsyncFriends extends AsyncTask<String, Void, Boolean> {

        private String username;

        @Override
        protected Boolean doInBackground(String... strings) {
            this.username = strings[0];
            HashMap<String, String> map = new HashMap<>();
            map.put("from", username);
            map.put("to", Constants.currentUser.getUsername());

            String getFriendsURL = "http://proj-309-sa-b-2.cs.iastate.edu/DenyFriend.php";
            String x = HttpParse.postRequest(map, getFriendsURL);
            return x.equals("0");
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Constants.screenMessages.add(new ScreenMessage("Failed to deny request from " + username));
            }
        }
    }
}