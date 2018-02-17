package sa_b_2.coms309.dungeonadventure.network;


import android.os.AsyncTask;

import java.util.HashMap;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.ScreenMessage;

/**
 * Removes the given user from the current users's friend list
 */
public class RemoveFriend {

    /**
     * Removes the given user from the current users's friend list
     *
     * @param username user to be removed
     */
    public static void removeFriend(String username) {

        new AsyncFriends().execute(username);
    }

    private static class AsyncFriends extends AsyncTask<String, Void, Boolean> {

        private String username;

        @Override
        protected Boolean doInBackground(String... strings) {
            this.username = strings[0];
            HashMap<String, String> map = new HashMap<>();
            map.put("remove", username);
            map.put("user", Constants.currentUser.getUsername());

            String getFriendsURL = "http://proj-309-sa-b-2.cs.iastate.edu/RemoveFriend.php";
            String x = HttpParse.postRequest(map, getFriendsURL);
            return x.equals("0");
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Constants.screenMessages.add(new ScreenMessage("Failed to remove friend"));
            } else {
                Constants.currentUser.setFriends(GetFriends.getFriends(Constants.currentUser.getUsername()));
            }
        }
    }
}