package sa_b_2.coms309.dungeonadventure.network;

import android.os.AsyncTask;

import java.util.HashMap;

import sa_b_2.coms309.dungeonadventure.game.Constants;

/**
 * Removes a game invite from the given user
 */
public class RemoveGameInvite {

    /**
     * Removes a game invite
     *
     * @param username user to reject game invite from
     */
    public static void removeGameInvite(String username) {

        new AsyncFriends().execute(username);

    }

    private static class AsyncFriends extends AsyncTask<String, Void, Boolean> {

        private String username;

        @Override
        protected Boolean doInBackground(String... strings) {
            this.username = strings[0];
            HashMap<String, String> map = new HashMap<>();
            map.put("from", username);
            map.put("to", Constants.currentUser.getUsername());

            String getFriendsURL = "http://proj-309-sa-b-2.cs.iastate.edu/RemoveGameInvite.php";
            String x = HttpParse.postRequest(map, getFriendsURL);
            return x.equals("0");
        }

        @Override
        protected void onPostExecute(Boolean success) {

        }
    }
}