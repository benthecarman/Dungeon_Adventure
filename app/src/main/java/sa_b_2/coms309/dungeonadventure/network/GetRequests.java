package sa_b_2.coms309.dungeonadventure.network;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.FriendRequestScreenMessage;

/**
 * Gets the friend requests to the current User
 */
public class GetRequests {

    /**
     * Gets the friend requests to the current User
     */
    public static void getRequests() {

        AsyncRequests asyncRequests = new AsyncRequests();
        asyncRequests.execute(Constants.currentUser.getUsername());
    }

    private static class AsyncRequests extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", strings[0]);

            String getRequestsURL = "http://proj-309-sa-b-2.cs.iastate.edu/GetFriendRequests.php";
            return HttpParse.postRequest(map, getRequestsURL);
        }

        @Override
        protected void onPostExecute(String s) {
            TypeToken<List<FriendRequestMsg>> token = new TypeToken<List<FriendRequestMsg>>() {
            };
            List<FriendRequestMsg> list = new Gson().fromJson(s, token.getType());

            List<String> names = new LinkedList<>();
            for (FriendRequestMsg f : list)
                names.add(FriendRequestMsg.GetUser(f.getFromID()));

            for (String s1 : names) {
                Constants.screenMessages.add(new FriendRequestScreenMessage(s1));
            }
        }
    }
}
