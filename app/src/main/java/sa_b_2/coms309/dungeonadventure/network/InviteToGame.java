package sa_b_2.coms309.dungeonadventure.network;


import android.os.AsyncTask;

import java.util.HashMap;

import sa_b_2.coms309.dungeonadventure.game.Constants;

/**
 * Invites the given user to a game
 */
public class InviteToGame {

    /**
     * Invites the given user to a game
     *
     * @param username user to be invited
     */
    public static void inviteToGame(String username) {

        new Invite().execute(username);
    }

    private static class Invite extends AsyncTask<String, Void, Boolean> {

        private String username;

        @Override
        protected Boolean doInBackground(String... strings) {
            this.username = strings[0];
            HashMap<String, String> map = new HashMap<>();
            map.put("to", username);
            map.put("from", Constants.currentUser.getUsername());

            String getFriendsURL = "http://proj-309-sa-b-2.cs.iastate.edu/InviteToGame.php";
            String x = HttpParse.postRequest(map, getFriendsURL);
            return x.equals("0");
        }
    }
}
