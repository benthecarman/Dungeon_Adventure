package sa_b_2.coms309.dungeonadventure.network;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Server message object that holds a friend request
 */

public class FriendRequestMsg {

    private int toID, fromID;

    public FriendRequestMsg(int to, int from){
        toID = to;
        fromID = from;
    }

    /**
     * Get's the username of a user from their accountID
     *
     * @param id User's account ID
     * @return user's username
     */
    public static String GetUser(int id){
        GetUser g = new GetUser();
        try {
            String result = g.execute(id).get();
            if(result.equals("Not connected to the internet")||result.equals("Something Went Wrong")||result.equals("Server Error"))
                return null;
            return result;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getToID() {
        return toID;
    }

    public int getFromID() {
        return fromID;
    }

    private static class GetUser extends AsyncTask<Integer, Void, String>{

        @Override
        protected String doInBackground(Integer... ints) {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", ints[0].toString());

            String getUserURL = "http://proj-309-sa-b-2.cs.iastate.edu/GetUser.php";
            return HttpParse.postRequest(map, getUserURL);
        }
    }
}
